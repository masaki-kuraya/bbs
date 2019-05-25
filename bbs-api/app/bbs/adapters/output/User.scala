package bbs.adapters.output

import play.api.libs.json._

case class User(id: String, name: String)

object User {
  implicit val locationWrites: Writes[User] = Json.writes[User]
}
