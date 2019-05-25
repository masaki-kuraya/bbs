package bbs.adapters

import auth.adapters._
import javax.inject._
@Singleton
class AuthService @Inject()(authQueryService: AuthQueryService) {

  def authenticate (token: String): Boolean = authQueryService.authenticate(token) match {
    case Some(_) => true
    case None => false
  }

}
