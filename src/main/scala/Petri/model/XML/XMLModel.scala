package Petri.model.XML

import java.io.{File, FileOutputStream}

import Petri.model._

import scala.xml.XML

/**
  * Created by Neikila on 30.03.2016.
  */
class XMLModel(val file: File){
  import XMLModel._

  lazy val xml = XML.loadFile(file)
  lazy val places = {
    (xml \\ PLACES \ PLACE).map((place) =>
      new Place(
        (place \ ID).text.toInt,
        util.Try((place \ COUNT).text.toInt).getOrElse(0)
      )
    ).toList
  }

  lazy val transactions = {
    (xml \\ TRANSACTIONS \ TRANSACTION).map((transaction) =>
      new Transaction(
        (transaction \ ID).text.toInt,
        util.Try((transaction \ PRIORITY).text.toInt).getOrElse(0),
        {
          if ((transaction \ DESC).length == 0)
            None
          else
            Some((transaction \ DESC).text)
        }
      )
    ).toList
  }

  lazy val arcs = {
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

  def save(model: Model) = {
    val prettyPrinter = new scala.xml.PrettyPrinter(80, 2)
    val prettyXml = prettyPrinter.format(createXml(model))
    file.createNewFile()
    util.Try({
      val out = new FileOutputStream(file)
      out.write(prettyXml.getBytes)
      out.close()
    })
  }

  def createXml(model: Model) = {
    <net>
      <places>
        {model.places.map(toXML _)}
      </places>
      <transactions>
        {model.transactions.map(toXML _)}
      </transactions>
      <arcs>
        {model.arcs.groupBy({
        case p2t: P2T => p2t.to
        case t2p: T2P => t2p.from}).toList.sortBy({case (tr, _) => tr.id})
        .foldLeft(List[Arc]())({
          case (res, (_, list)) => res ::: list.sortBy(_.direction == Directions.Transaction2Place)
        }).map(toXML _)}
      </arcs>
    </net>
  }
}

object XMLModel {
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
  val PRIORITY = "priority"

  def toXML(place: Place) = {
    <place>
      <id>{place.id}</id>
      <count>{place.counter}</count>
    </place>
  }

  def toXML(tr: Transaction) = {
    <transaction>
      <id>{tr.id}</id>
      {if (tr.description.isDefined) <description>{tr.description.get}</description>}
      <priority>{tr.priority.toString}</priority>
    </transaction>
  }

  def toXML(arc: Arc) = {
    <arc>{
      arc match {
        case p2t: P2T =>
          <from>{p2t.from.id}</from>
          <to>{p2t.to.id}</to>
          <direction>p2t</direction>
        case t2p: T2P =>
          <from>{t2p.from.id}</from>
            <to>{t2p.to.id}</to>
            <direction>t2p</direction>
      }}
    </arc>
  }
}

