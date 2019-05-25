package bbs.adapters

import bbs.domains._
import javax.inject._

@Singleton
class BbsCommandService @Inject()(postRepository: PostRepository) {

  def post(post: input.Post): output.Post = {
    val id = postRepository.nextId()
    postRepository.add(Post(id, post.posterId, post.comment)) {
      post =>
        created =>
          modified =>
            output.Post(post.id, output.User(post.posterId, ""), post.comment, created.toEpochMilli, modified.toEpochMilli)
    }
  }

}
