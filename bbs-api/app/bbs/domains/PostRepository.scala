package bbs.domains

import java.time.Instant
import java.util.UUID

import anorm._
import javax.inject._
import play.api.db._

@Singleton
class PostRepository @Inject()(db: Database) {

  def add[R](post: Post)(f: Post => Instant => Instant => R): R = db.withConnection { implicit c =>
    val now = Instant.now()
    SQL"""
      INSERT INTO
        posts (id, poster_id, comment, created, modified)
      VALUES
        (${post.id},${post.posterId}, ${post.comment}, ${now}, ${now})"""
      .execute()
    f(post)(now)(now)
  }

  def nextId(): String = UUID.randomUUID().toString

}
