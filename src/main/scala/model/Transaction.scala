package model

/**
  * Created by Neikila on 30.03.2016.
  */
class Transaction (id: Int, var priority: Int, val description: Option[String]) extends Element (id) {
  var isPossible = false

  def == (transaction: Transaction) = {
    transaction.id == id
  }
}
