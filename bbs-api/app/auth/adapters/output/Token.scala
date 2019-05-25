package auth.adapters.output

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Token(token: String, user: User)

object Token {
  implicit val writes: Writes[Token] = (
    (JsPath \ "token").write[String] and
      (JsPath \ "user").write[User]
    ) (unlift(Token.unapply))
}
