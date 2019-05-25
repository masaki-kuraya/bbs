package auth.adapters.input

import play.api.libs.json._

case class User(name: String, email: String, password: String)

object User {
  implicit val reads: Reads[User] = Json.reads[User]
}
