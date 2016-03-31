import java.io.File

import scala.xml.XML
import _root_.XML.XMLParser
import model._

import scala.collection.immutable.HashMap
/**
  * Created by Neikila on 30.03.2016.
  */
class Main {

}

object Main {
  def main(args: Array[String]): Unit = {
    val file = new File("out/scheme.xml")
    val parser = new XMLParser(file)

    val petriNet = new Model(
      parser.places,
      parser.transactions.sortBy(_.priority).reverse,
      parser.arcs
    )

    petriNet.printState()

    while (petriNet.nextByPriority(true).equals(TransactionApplyResult.Success)) {
      petriNet.printState()
    }
  }
}
