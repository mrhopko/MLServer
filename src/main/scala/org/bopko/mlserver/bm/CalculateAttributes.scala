package org.bopko.mlserver.bm

object CalculateAttributes {

  def calc(application: Application): Application =
    application.copy(
      attributes = Array(
        BmAttributes.avgBalance.calc(application),
        BmAttributes.maxBalance.calc(application),
        BmAttributes.minBalance.calc(application))
    )

}
