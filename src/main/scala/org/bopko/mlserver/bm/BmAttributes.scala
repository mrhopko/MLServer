package org.bopko.mlserver.bm

object BmAttributes {

  // Attribute that computes the max balance of all accounts
  val maxBalance = Calculator { app =>
    Attribute(
      "maxBalance",
      app.account.map(_.history.map(_.balance).max).max)
  }

  // Attribute that computes the min balance of all accounts
  val minBalance = Calculator { app =>
    Attribute("minBalance", app.account.map(_.history.map(_.balance).min).min)
  }

  // Attribute that computes the average balance of all accounts
  val avgBalance = Calculator { app =>
    Attribute("avgBalance",
      app.account.map(_.history.map(_.balance).sum).sum / app.account.map(_.history.length).sum)
  }

}
