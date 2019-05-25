package auth.domains

import com.github.t3hnar.bcrypt._
import javax.inject.Singleton

import scala.util.Try

@Singleton
class PasswordEncoder {

  def hashPassword(password: String): String = password.bcrypt

  def matchPassword(password: String, hashed: String): Boolean = password.isBcrypted(hashed)

}
