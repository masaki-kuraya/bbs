package bbs.adapters.output

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Post(id: String, poster: User, comment: String, created: Long, modified: Long)

object Post {
  implicit val writes: Writes[Post] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "poster").write[User] and
      (JsPath \ "comment").write[String] and
      (JsPath \ "created").write[Long] and
      (JsPath \ "modified").write[Long]
    ) (unlift(Post.unapply))
}
