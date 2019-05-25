package auth.adapters

import auth.domains._
import javax.inject._

@Singleton
class AuthCommandService @Inject()(userRepository: UserRepository, tokenGenerator: TokenGenerator) {

  def signUp(user: input.User): Either[UserError, output.User] = {
    val id = userRepository.nextId()
    userRepository.add(User(id, user.name, user.email, user.password)).map { u =>
      output.User(u.id, u.name, u.email)
    }
  }

  def generateToken(userId: String): String = tokenGenerator.generateToken(userId)

}
