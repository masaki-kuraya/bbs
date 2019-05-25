package auth.adapters.output

import play.api.libs.json._

case class User(id: String, name: String, email: String)

object User {
  implicit val writes: Writes[User] = Json.writes[User]
}
