package model

/**
  * Created by Neikila on 30.03.2016.
  */
class Place (id: Int, var counter: Int = 0) extends Element(id) {
  def ++ (amount: Int = 1) = {
    val temp = counter
    counter += amount
    temp
  }

  def -- (amount: Int = 1) = {
    val temp = counter
    counter -= amount
    temp
  }

  def == (place: Place) = {
    id == place.id
  }
}
