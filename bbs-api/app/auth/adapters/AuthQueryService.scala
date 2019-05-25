package auth.adapters

import anorm.SqlParser._
import anorm._
import auth.domains.PasswordEncoder
import javax.inject._
import play.api.db._

@Singleton
class AuthQueryService @Inject()(db: Database, passwordEncoder: PasswordEncoder) {

  val parser: RowParser[output.User] = str("id") ~
    str("name") ~
    str("email") map {
    case id ~ name ~ email => output.User(id, name, email)
  }

  val parserTuple: RowParser[(String, String, String, String)] = str("id") ~
    str("name") ~
    str("email") ~
    str("password")  map {
    case id ~ name ~ email ~ password => (id, name, email, password)
  }

  def authenticate(email: String, password: String): Option[output.User] = db.withConnection { implicit c =>
    SQL"""
      SELECT
        id,
        name,
        email,
        password
      FROM
        users
      WHERE
        email = $email"""
      .as(parserTuple.singleOpt)
      .flatMap { t =>
        if (passwordEncoder.matchPassword(password, t._4)) {
          Some(output.User(t._1, t._2, t._3))
        } else {
          None
        }
      }
  }

  def authenticate(token: String): Option[output.User] = db.withConnection { implicit c =>
    SQL"""
      SELECT
        id,
        name,
        email
      FROM
        users
        INNER JOIN tokens
          ON users.id = tokens.user_id
      WHERE
        token = $token"""
      .as(parser.singleOpt)
  }

  def findToken(userId: String): Option[String] = db.withConnection{ implicit c =>
    SQL"""
      SELECT
        token
      FROM
        tokens
      WHERE
        user_id = $userId"""
      .as(scalar[String].singleOpt)
  }

//  def findById(id: String): Option[output.User] = db.withConnection { implicit c =>
//    SQL"""
//      SELECT
//        id,
//        name,
//        email
//      FROM
//        users
//      WHERE
//        id = $id"""
//      .as(parser.singleOpt)
//  }



}
