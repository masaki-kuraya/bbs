package common.model

import play.api.i18n._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import ErrorDetail._

case class Error(code: String, message: String, errors: Seq[ErrorDetail])

object Error {

  val VALIDATION_ERROR = "VALIDATION_ERROR"
  val UNAUTHORIZED_ERROR = "UNAUTHORIZED"
  val BAD_REQUEST = "BAD_REQUEST"
  val NOT_FOUND = "NOT_FOUND"
  val METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED"
  val SERVER_ERROR = "SERVER_ERROR"

  def ValidationError(error: ErrorDetail)(implicit provider: MessagesProvider): Error = Error(VALIDATION_ERROR, Seq(error))

  def ValidationError(errors: Seq[ErrorDetail])(implicit provider: MessagesProvider): Error = Error(VALIDATION_ERROR, errors)

  def ValidationError(errManager: JsError)(implicit provider: MessagesProvider): Error = {
    val detail = errManager.errors.flatMap { errors =>
      val field = errors._1.toString
      errors._2.map {_.message match {
          case "error.path.missing" => FieldNotExists(field)
          case "error.expected.jsstring" => ExpectedString(field)
          case _ => UnexpectedError(field)
        }
      }
    }
    ValidationError(detail)
  }

  def UnauthorizedError(implicit provider: MessagesProvider) : Error= Error(UNAUTHORIZED_ERROR, Nil)

  def ServerError(implicit provider: MessagesProvider) : Error= Error(SERVER_ERROR, Nil)

  def BadRequest(implicit provider: MessagesProvider) : Error= Error(BAD_REQUEST, Nil)

  def NotFound(implicit provider: MessagesProvider) : Error= Error(NOT_FOUND, Nil)

  def MethodNotAllowed(implicit provider: MessagesProvider) : Error= Error(METHOD_NOT_ALLOWED, Nil)

  def apply(code: String)(implicit provider: MessagesProvider): Error = Error(code, Messages(keys(code)), Nil)

  def apply(code: String, errors: Seq[ErrorDetail])(implicit provider: MessagesProvider): Error = Error(code, Messages(keys(code)), errors)

  def keys(code: String) = code match {
    case VALIDATION_ERROR => "error.validation"
    case UNAUTHORIZED_ERROR => "error.unauthorized"
    case BAD_REQUEST => "error.badRequest"
    case NOT_FOUND => "error.notFound"
    case METHOD_NOT_ALLOWED => "error.methodNotAllowed"
    case SERVER_ERROR => "error.serverError"
    case _ => "error.unexpectedError"
  }

  implicit val writes: Writes[Error] = (
    (JsPath \ "code").write[String] and
      (JsPath \ "message").write[String] and
      (JsPath \ "errors").write[Seq[ErrorDetail]]
    ) (unlift(Error.unapply))

}

