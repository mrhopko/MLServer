package org.bopko.mlserver.bm

import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

case class Account(
                    accountType: String,
                    history: List[AccountState],
                    lastUpdate: Int
                  ) {


  def toJson: String = {
    write(this)(Account.formats)
  }

}

object Account {

  implicit val formats = Serialization.formats(NoTypeHints)

  // convert Account from Json
  def fromJson(json: String): Account = {
    implicit val formats = Serialization.formats(NoTypeHints)
    read[Account](json)
  }

  //create a random Account
  def random: Account = {
    Account(
      accountType = "credit",
      history = List.range(1,13).map(_ => AccountState.random),
      lastUpdate = 0
    )
  }

}