package org.bopko.mlserver.bm

import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

case class Application(
                      appRef: String,
                      account: Array[Account],
                      attributes: Array[Attribute]
                      ) {
  def toJson: String = write(this)(Application.formats)
}

object Application {

  implicit val formats = Serialization.formats(NoTypeHints)
  def fromJson(json: String): Application = {
    read[Application](json)
  }

  def random: Application = {
    Application(
      appRef = Math.random().toString,
      account = Array.range(1, (Math.random() * 10).toInt + 2).map(_ => Account.random),
      attributes = Array()
    )
  }

}
