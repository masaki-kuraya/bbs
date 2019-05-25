package auth.adapters

import java.util.Base64

import auth.domains._
import common.model.Error._
import common.model.ErrorDetail
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

@Singleton
class AuthController @Inject()(
    mcc: MessagesControllerComponents,
    authCommandService: AuthCommandService,
    authQueryService: AuthQueryService)
  extends MessagesAbstractController(mcc) {

  def getAuth(): Action[AnyContent] = Action { implicit request =>
    AuthController.userInfo(authQueryService) match {
      case Some(user) =>
        val token = authQueryService.findToken(user.id).getOrElse {
          authCommandService.generateToken(user.id)
        }
        Ok(Json.toJson(output.Token(token, user)))
      case None =>
        Unauthorized(Json.toJson(UnauthorizedError)).withHeaders(WWW_AUTHENTICATE -> "Basic realm=\"bbs\"")
    }
  }

  def postUsers(): Action[JsValue] = Action(parse.json) { implicit request =>
    request.body.validate[input.User].map { user =>
      authCommandService.signUp(user) match {
        case Right(user) => Created(Json.toJson(user))
        case Left(AlreadyRegistered) => BadRequest(Json.toJson(ValidationError(ErrorDetail.AlreadyRegistered("email"))))
        case Left(_) => InternalServerError(Json.toJson(ServerError))
      }
    }.recoverTotal { e =>
      BadRequest(Json.toJson(ValidationError(e)))
    }
  }
}

object AuthController {

  def parseAuthHeader(authHeader: String): Option[Credentials] =
    authHeader.split("""\s""") match {
      case Array("Basic", userAndPass) ⇒
        new String(Base64.getDecoder.decode(userAndPass), "UTF-8").split(":") match {
          case Array(user, password) ⇒ Some(Credentials(user, password))
          case _ ⇒ None
        }
      case _ ⇒ None
    }

  def validateUser(authQueryService: AuthQueryService)(c: Credentials): Option[output.User] = {
    authQueryService.authenticate(c.email, c.password)
  }

  def userInfo(authQueryService: AuthQueryService)(implicit req: RequestHeader): Option[output.User] = {
    req.headers.get("Authorization").flatMap(parseAuthHeader).flatMap(validateUser(authQueryService))
  }

}