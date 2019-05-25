package bbs.adapters.input

import play.api.libs.json._

case class Post(posterId: String, comment: String)

object Post {
  implicit val reads: Reads[Post] = Json.reads[Post]
}
