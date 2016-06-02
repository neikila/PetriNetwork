package Petri.model

import Petri.model.Directions.Directions

/**
  * Created by Neikila on 30.03.2016.
  */
sealed abstract class Arc (val direction: Directions){
}

class P2T (val from: Place, val to: Transaction, val amount: Int = 1) extends Arc (Directions.Place2Transaction){
}

class T2P (val from:  Transaction, val to: Place, val amount: Int = 1) extends Arc (Directions.Transaction2Place) {
}
