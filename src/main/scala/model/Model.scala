package model

import model.TransactionApplyResult.TransactionApplyResult

/**
  * Created by neikila.
  */
class Model (val places: List[Place], val transactions: List[Transaction], val arcs: List[Arc]) {

  val (arcsPlace2Tr: List[P2T], arcsTr2Place: List[T2P]) = arcs.partition(_.direction.equals(Directions.Place2Transaction))

  val mapT2PArc = arcsTr2Place.groupBy(_.from)
  val mapP2TArc = arcsPlace2Tr.groupBy(_.to)

  def nextByPriority(shouldPrintToLog: Boolean = false) = {
    val activeTransactions = transactions.filter(isEnoughMarks)

    if (shouldPrintToLog) {
      println()
      println("All applicable transactions")
      activeTransactions.foreach(tr => println(s"Transaction[${tr.id}] with priority ${tr.priority}"))
    }

    if (activeTransactions.isEmpty)
      TransactionApplyResult.NoApplicableTransactions
    else {
      applyTransaction(activeTransactions.head)
    }
  }

  def nextByTransactionId(id: Int) = {
    transactions.find(_.id == id) match {
      case Some(tr: Transaction) =>
        if (isEnoughMarks(tr))
          applyTransaction(tr)
        else
          TransactionApplyResult.NotEnoughMarks
      case _ =>
        TransactionApplyResult.NoTransactionWithSuchID
    }
  }

  def applyTransaction(transaction: Transaction): TransactionApplyResult = {
    util.Try(mapP2TArc(transaction)).getOrElse(List[P2T]()).foreach(p2t => p2t.from -- p2t.amount)
    util.Try(mapT2PArc(transaction)).getOrElse(List[T2P]()).foreach(t2p => t2p.to ++ t2p.amount)
    TransactionApplyResult.Success
  }

  def isEnoughMarks(tr: Transaction): Boolean = {
    util.Try(mapP2TArc(tr)).getOrElse(List[P2T]()).par.groupBy(_.from).map {
      case (place, listP2T) => (place, listP2T.foldLeft(0)(_ + _.amount))
    } forall { case (place, requiredMarksAmount) =>
      place.counter >= requiredMarksAmount
    }
  }

  def printInit(): Unit = {

    places.foreach((place) =>
      println(s"Place[${place.id}]. Amount of marks - ${place.counter}"))

    println()

    transactions.foreach((tr) =>
      println(s"Transaction[${tr.id}]. Description:\n${tr.description.getOrElse("No description given")}"))

    println()

    mapP2TArc.foreach(tuple => {
      val (tr, arcs) = tuple
      println(s"Transaction[${tr.id}] with priority = ${tr.priority}")
      arcs.foreach(arc => {
        println(s"Arc from Place[${arc.from.id}]. It has ${arc.from.counter} marks")
      })
    })

    println()

    mapT2PArc.foreach(tuple => {
      val (tr, arcs) = tuple
      println(s"Transaction[${tr.id}] with priority = ${tr.priority}")
      arcs.foreach(arc => {
        println(s"Arc to Place[${arc.to.id}]. It has ${arc.to.counter} marks")
      })
    })
  }

  def printState(): Unit = {
    for (place <- places) {
      println(s"Place[${place.id}]. Counter = ${place.counter}")
    }
  }
}