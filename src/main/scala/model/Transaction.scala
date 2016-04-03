package model

/**
  * Created by Neikila on 30.03.2016.
  */
class Transaction (id: Int, val priority: Int, val description: Option[String]) extends Element (id) {
  var isPossible = false
}
