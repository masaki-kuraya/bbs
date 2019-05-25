package auth.domains

import java.util.UUID

import anorm._
import javax.inject._
import play.api.db.Database

@Singleton
class TokenGenerator @Inject()(db: Database) {

  def generateToken(userId: String): String = db.withConnection { implicit c =>
    val token = UUID.randomUUID.toString
    SQL"""
      INSERT INTO
        tokens (token, user_id)
      VALUES
        ($token, $userId)"""
      .execute()
    token
  }

}
