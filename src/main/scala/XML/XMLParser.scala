package XML

import java.io.File

import model._

import scala.xml.XML

/**
  * Created by Neikila on 30.03.2016.
  */
class XMLParser (val file: File){
  import XMLParser._

  val xml = XML.loadFile(file)

  def places = {
    (xml \\ PLACES \ PLACE).map((place) =>
      new Place(
        (place \ ID).text.toInt,
        util.Try((place \ COUNT).text.toInt).getOrElse(0)
      )
    ).toList
  }

  def transactions = {
    (xml \\ TRANSACTIONS \ TRANSACTION).map((transaction) =>
      new Transaction(
        (transaction \ ID).text.toInt, {
          if ((transaction \ DESC).length == 0)
            None
          else
            Some((transaction \ DESC).text)
        }
      )
    ).toList
  }

  def arcs(places: Seq[Place], transactions: Seq[Transaction]) = {
    (xml \\ ARCS \ ARC).map((arc) => {
      val from = (arc \ FROM).text.toInt
      val to = (arc \ TO).text.toInt
      (arc \ DIRECTION).text match {
        case TRANSACTION2PLACE => new T2P(
          transactions.find((tr) => tr.id == from).get,
          places.find((place) => place.id == to).get,
          util.Try((arc \ AMOUNT).text.toInt).getOrElse(1)
        )
        case PLACE2TRANSACTION => new P2T(
          places.find((place) => place.id == from).get,
          transactions.find((tr) => tr.id == to).get,
          util.Try((arc \ AMOUNT).text.toInt).getOrElse(1)
        )
      }
    }).toList
  }
}

object XMLParser {
  val ID = "id"
  val DESC = "description"
  val TRANSACTION = "transaction"
  val TRANSACTIONS = "transactions"
  val PLACE = "place"
  val PLACES = "places"
  val COUNT = "count"
  val ARCS = "arcs"
  val ARC = "arc"
  val DIRECTION = "direction"
  val PLACE2TRANSACTION = "p2t"
  val TRANSACTION2PLACE = "t2p"
  val AMOUNT = "amount"
  val FROM = "from"
  val TO = "to"
}

