package Petri.model

import Petri.model.TransactionApplyResult.TransactionApplyResult

/**
  * Created by neikila.
  */
class Model (var places: List[Place], var transactions: List[Transaction], var arcs: List[Arc]) {
  def this() = {
    this(List[Place](), List[Transaction](), List[Arc]())
  }

  var mapT2PArc = Map[Transaction, List[T2P]]()
  var mapP2TArc = Map[Transaction, List[P2T]]()

  def initMaps(): Unit = {
    val (arcsPlace2Tr: List[P2T], arcsTr2Place: List[T2P]) =
      arcs.partition(_.direction.equals(Directions.Place2Transaction))

    mapT2PArc = arcsTr2Place.groupBy(_.from)
    mapP2TArc = arcsPlace2Tr.groupBy(_.to)
  }
  initMaps()

  def addPlace(counter: Int = 0) = {
    val place = util.Try(places.maxBy(_.id)).getOrElse(0) match {
      case place: Place => new Place(place.id + 1, counter)
      case 0 => new Place(0, counter)
    }
    places = places :+ place
    place
  }

  def addP2T(from: Place, to: Transaction) = {
    val p2t = new P2T(from, to)
    val group = util.Try(mapP2TArc(to)).getOrElse(List[P2T]())
    if (group.nonEmpty)
      mapP2TArc -= to
    mapP2TArc += (to -> (group :+ p2t))
    arcs = arcs :+ p2t
    p2t
  }

  def addT2P(from: Transaction, to: Place) = {
    val t2p = new T2P(from, to)
    val group = util.Try(mapT2PArc(from)).getOrElse(List[T2P]())
    if (group.nonEmpty)
      mapT2PArc -= from
    mapT2PArc += (from -> (group :+ t2p))
    arcs = arcs :+ t2p
    t2p
  }

  def remove(place: Place) = {
    places = places.filter(_.id != place.id)
    val (toRemove, left) = arcs.partition({
      case p2t: P2T => p2t.from == place
      case t2p: T2P => t2p.to == place
    })
    arcs = left
    if (toRemove.nonEmpty)
      initMaps()
  }

  def remove(tr: Transaction) = {
    transactions = transactions.filter(_.id != tr.id)
    val (toRemove, left) = arcs.partition({
      case p2t: P2T => p2t.to == tr
      case t2p: T2P => t2p.from ==  tr
    })
    arcs = left
    if (toRemove.nonEmpty)
      initMaps()
  }

  def addTransaction(priority: Int = 0, desc: String = "") = {
    val transaction = util.Try(transactions.maxBy(_.id)).getOrElse(0) match {
      case transaction: Transaction => new Transaction(transaction.id + 1, priority, Some(desc))
      case 0 => new Transaction(0, priority, Some(desc))
    }
    transactions = transactions :+ transaction
    transaction
  }

  def activeTransactions = transactions.filter(tr =>
    isEnoughMarks(tr) && (mapT2PArc.contains(tr) || mapP2TArc.contains(tr)))

  def nextByPriority(shouldPrintToLog: Boolean = false) = {
    val actTr = activeTransactions

    if (shouldPrintToLog) {
      println()
      println("All applicable transactions")
      actTr.foreach(tr => {
        tr.isPossible = true
        println(s"Transaction[${tr.id}] with priority ${tr.priority}")
      }
      )
    }

    if (actTr.isEmpty)
      TransactionApplyResult.NoApplicableTransactions
    else
      applyTransaction(actTr.head)
  }

  def enableActTransaction() = {
    transactions.foreach(_.isPossible = false)
    activeTransactions.foreach(_.isPossible = true)
  }

  def nextByTransactionId(id: Int) = {
    transactions.find(_.id == id) match {
      case Some(tr: Transaction) =>
        nextWith(tr)
      case _ =>
        TransactionApplyResult.NoTransactionWithSuchID
    }
  }

  def nextWith(tr: Transaction) = {
    if (isEnoughMarks(tr))
      applyTransaction(tr)
    else
      TransactionApplyResult.NotEnoughMarks
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
