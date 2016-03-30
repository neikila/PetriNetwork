package model

/**
  * Created by Neikila on 30.03.2016.
  */
class T2P (val from:  Transaction, val to: Place, val amount: Int = 1) extends Arc (Directions.Transaction2Place) {

}
