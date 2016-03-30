import java.io.File

import scala.xml.XML
import _root_.XML.XMLParser
import model.Directions
import model.{P2T, T2P, Transaction}

import scala.collection.immutable.HashMap
/**
  * Created by Neikila on 30.03.2016.
  */
class Main {

}

object Main {
  def main(args: Array[String]) = {
    val file = new File("out/scheme.xml")
    val parser = new XMLParser(file)
    parser.places.foreach((place) =>
      println(s"Place[${place.id}]. Amount of marks - ${place.counter}"))
    println()
    parser.transactions.foreach((tr) =>
      println(s"Transaction[${tr.id}]. Description:\n${tr.description.getOrElse("No description given")}"))
    println()
    val arcs = parser.arcs(parser.places, parser.transactions)
    val (temp1, temp2) = arcs.partition(_.direction.equals(Directions.Place2Transaction))

    val mapT2PArc = temp2.par.map(_.asInstanceOf[T2P]).groupBy(_.from)
    val mapP2TArc = temp1.par.map(_.asInstanceOf[P2T]).groupBy(_.to)

    mapP2TArc.foreach(tuple => {
      val (tr, arcs) = tuple
      println(s"Transaction[${tr.id}]")
      arcs.foreach(arc => {
        println(s"Arc from Place[${arc.from.id}]. It has ${arc.from.counter} marks")
      })
    })

    mapT2PArc.foreach(tuple => {
      val (tr, arcs) = tuple
      println(s"Transaction[${tr.id}]")
      arcs.foreach(arc => {
        println(s"Arc to Place[${arc.to.id}]. It has ${arc.to.counter} marks")
      })
    })
  }
}
