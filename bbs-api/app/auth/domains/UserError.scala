package auth.domains

sealed trait UserError

case object AlreadyRegistered extends UserError
