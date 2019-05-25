import javax.inject._
import play.api.http._
import play.api.mvc._
import common.model.Error._
import common.model._
import play.api.i18n._
import play.api.libs.json._

import scala.concurrent._

@Singleton
class ErrorHandler @Inject()(messagesApi: MessagesApi) extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    implicit val messagesRequest: MessagesRequest[Option[Nothing]] = new MessagesRequest(request.withBody(None), messagesApi)
    Future.successful(
      statusCode match {
        case 400 =>Results.BadRequest(Json.toJson(Error.BadRequest))
        case 404 => Results.NotFound(Json.toJson(Error.NotFound))
        case 405 => Results.MethodNotAllowed(Json.toJson(Error.MethodNotAllowed))
        case _ => Results.InternalServerError(Json.toJson(Error.SERVER_ERROR))
      }
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    implicit val messagesRequest: MessagesRequest[Option[Nothing]] = new MessagesRequest(request.withBody(None), messagesApi)
    Future.successful(
      Results.InternalServerError(Json.toJson(ServerError))
    )
  }
}