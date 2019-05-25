package bbs.adapters

import bbs.adapters.BbsController._
import common.model.Error._
import common.model.ErrorDetail._
import javax.inject._
import play.api.libs.json._
import play.api.mvc._

@Singleton
class BbsController @Inject()(mcc: MessagesControllerComponents, bbsQueryService: BbsQueryService, bbsCommandService: BbsCommandService, authService: AuthService) extends MessagesAbstractController(mcc) {

  implicit val defaultName: String = "匿名"

  def getPosts(limit: Option[Long], offset: Option[Long]) = Action { implicit request =>
    validToken(authService) match {
      case true =>
        val lim = limit.getOrElse(10L)
        val off = offset.getOrElse(0L)
        val limitError = if (lim < 0) Some(NegativeNumber("limit")) else None
        val offsetError = if (off < 0) Some(NegativeNumber("offset")) else None
        val errors = Seq(limitError, offsetError).filter(_ != None).map(_.get)
        errors match {
          case Nil =>
            val posts: output.Posts = {
              val meta = output.Meta(bbsQueryService.count())
              val items = bbsQueryService.findOfRange(lim, off)
              output.Posts(meta, items)
            }
            Ok(Json.toJson(posts))
          case _ => BadRequest(Json.toJson(ValidationError(errors)))
        }
      case false => Unauthorized(Json.toJson(UnauthorizedError)).withHeaders(WWW_AUTHENTICATE -> "Bearer realm=\"bbs\"")
    }
  }

  def postComment() = Action(parse.json) { implicit request =>
    validToken(authService) match {
      case true =>
        request.body.validate[input.Post].map { post =>
          Created(Json.toJson(bbsCommandService.post(post)))
        }.recoverTotal { e =>
          BadRequest(Json.toJson(ValidationError(e)))
        }
      case false => Unauthorized(Json.toJson(UnauthorizedError)).withHeaders(WWW_AUTHENTICATE -> "Bearer realm=\"bbs\"")
    }
  }

}

object BbsController {

  def validToken(authService: AuthService)(implicit req: RequestHeader): Boolean = {
    req.headers.get("Authorization").flatMap(parseAuthHeader) match {
      case Some(token) => authService.authenticate(token)
      case None => false
    }
  }

  def parseAuthHeader(authHeader: String): Option[String] = authHeader.split("""\s""") match {
    case Array("Bearer", token) ⇒ Some(token)
    case _ ⇒ None
  }

}