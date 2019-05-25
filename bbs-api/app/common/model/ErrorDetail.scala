package common.model

import play.api.i18n._
import play.api.libs.json._

case class ErrorDetail(code: String, message: String, field: Option[String])

object ErrorDetail {

  val FIELD_NOT_EXISTS = "FIELD_NOT_EXISTS"
  val EXPECTED_STRING = "EXPECTED_STRING"
  val ALREADY_REGISTERED = "ALREADY_REGISTERED"
  val NEGATIVE_NUMBER = "NEGATIVE_NUMBER"
  val UNEXPECTED_ERROR = "UNEXPECTED_ERROR"

  def FieldNotExists(filed: String)(implicit provider: MessagesProvider) = ErrorDetail(FIELD_NOT_EXISTS, Some(filed))

  def ExpectedString(filed: String)(implicit provider: MessagesProvider) = ErrorDetail(EXPECTED_STRING, Some(filed))

  def AlreadyRegistered(filed: String)(implicit provider: MessagesProvider) = ErrorDetail(ALREADY_REGISTERED, Some(filed))

  def NegativeNumber(filed: String)(implicit provider: MessagesProvider) = ErrorDetail(NEGATIVE_NUMBER, Some(filed))

  def UnexpectedError(filed: String)(implicit provider: MessagesProvider) = ErrorDetail(UNEXPECTED_ERROR, Some(filed))

  def apply(code: String, field: Option[String])(implicit provider: MessagesProvider): ErrorDetail = new ErrorDetail(code, Messages(keys(code)), field)

  def keys(code: String) = code match {
    case FIELD_NOT_EXISTS => "error.detail.validation.fieldNotExists"
    case EXPECTED_STRING => "error.detail.validation.expectedString"
    case ALREADY_REGISTERED => "error.detail.validation.alreadyRegistered"
    case NEGATIVE_NUMBER => "error.detail.validation.negativeNumber"
    case _ => "error.detail.unexpectedError"
  }

  implicit val writes: Writes[ErrorDetail] = Json.writes[ErrorDetail]

}
