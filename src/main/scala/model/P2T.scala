package model

/**
  * Created by Neikila on 30.03.2016.
  */
class P2T (val from: Place, val to: Transaction, val amount: Int = 1) extends Arc (Directions.Place2Transaction){
}
