// @GENERATOR:play-routes-compiler
// @SOURCE:D:/Project/bbs/bbs-api/conf/routes
// @DATE:Sun Jan 13 22:45:55 JST 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
