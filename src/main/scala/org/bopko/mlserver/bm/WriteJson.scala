package org.bopko.mlserver.bm

import java.io.PrintWriter

object WriteJson {

  val application = Application.random
  val json = application.toJson
  new PrintWriter("app.json") { write(json); close }

}
