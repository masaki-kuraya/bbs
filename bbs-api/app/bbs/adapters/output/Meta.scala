package bbs.adapters.output

import play.api.libs.json._

case class Meta(total: Long)

object Meta {
  implicit val writes: Writes[Meta] = Json.writes[Meta]
}
