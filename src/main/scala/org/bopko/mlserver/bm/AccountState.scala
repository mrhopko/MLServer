package org.bopko.mlserver.bm

case class AccountState(
                       date: Int,
                       belongsTo: Long,
                       balance: Long,
                       status: String
                         ) {

}

object AccountState {
  // Create an AccountState with random values
  def random: AccountState = {
    AccountState(
      0,
      (Math.random() * 1000).toLong,
      (Math.random() * 1000).toLong,
      "active"
    )
  }
}