package bbs.adapters.output

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Posts(meta: Meta, posts: Seq[Post])

object Posts {
  implicit val writes: Writes[Posts] = (
    (JsPath \ "meta").write[Meta] and
      (JsPath \ "posts").write[Seq[Post]]
    ) (unlift(Posts.unapply))
}
