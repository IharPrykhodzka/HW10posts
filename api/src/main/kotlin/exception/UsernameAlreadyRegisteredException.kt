package exception

import java.lang.RuntimeException

class UsernameAlreadyRegisteredException(message: String) : RuntimeException(message)