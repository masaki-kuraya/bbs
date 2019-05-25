package bbs.adapters

import anorm.SqlParser._
import anorm._
import javax.inject._
import play.api.db._

@Singleton
class BbsQueryService @Inject()(db: Database) {

  val parser: RowParser[String => output.Post] = str("posts.id") ~
    str("poster_id") ~
    str("name").? ~
    str("comment") ~
    date("created") ~
    date("modified") map {
    case id ~ posterId ~ posterName ~ comment ~ created ~ modified => defaultName =>
      val user = output.User(posterId, posterName.getOrElse(defaultName))
      output.Post(id, user, comment, created.toInstant.toEpochMilli, modified.toInstant.toEpochMilli)
  }

  def findOfRange(limit: Long, offset: Long)(implicit defaultName: String): Seq[output.Post] = db.withConnection { implicit c =>
    SQL"""
      SELECT
        posts.id,
        poster_id,
        name,
        comment,
        created,
        modified
      FROM
        posts
        LEFT OUTER JOIN users
          ON users.id = poster_id
      ORDER BY created DESC
      LIMIT $limit OFFSET $offset"""
      .as(parser.*)
      .map(f => f(defaultName))
  }

  def count(): Long = db.withConnection { implicit c =>
    SQL"""SELECT COUNT(*) FROM posts""".as(scalar[Long].single)
  }

}
