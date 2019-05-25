package auth.domains

import java.util.UUID

import anorm.SqlParser._
import anorm._
import javax.inject._
import play.api.db._

@Singleton
class UserRepository @Inject()(db: Database, passwordEncoder: PasswordEncoder) {

  def add[R](user: User): Either[UserError, User] = db.withConnection { implicit c =>
    exists(user: User) match {
      case true => Left(AlreadyRegistered)
      case false => {
        val hashedPassword = passwordEncoder.hashPassword(user.password)
        SQL"""
          INSERT INTO
            users (id, name, email, password)
          VALUES
            (${user.id},${user.name}, ${user.email}, ${hashedPassword})"""
          .execute()
        Right(user)
      }
    }
  }

  def exists(user: User): Boolean = db.withConnection { implicit c =>
    SQL"""
      SELECT
        EXISTS (
          SELECT
            *
          FROM
            users
          WHERE
            id = ${user.id}
            OR email = ${user.email}
        )"""
      .as(scalar[Long].map(_ > 0).single)
  }

  def nextId(): String = UUID.randomUUID().toString

}
