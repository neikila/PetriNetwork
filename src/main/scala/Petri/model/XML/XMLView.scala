package Petri.model.XML

import java.awt.Color
import java.io.{File, FileOutputStream}

import Petri.model.UI.{PetriNetCanvas, PlaceView, TransactionView, UIElement}

import scala.swing.Point
import scala.xml._

/**
  * Created by neikila.
  */
class XMLView (var petriNetCanvas: PetriNetCanvas) {
  import XMLView._

  def save(file: File) = {
    val prettyPrinter = new scala.xml.PrettyPrinter(80, 2)
    val prettyXml = prettyPrinter.format(xmlView)
    file.createNewFile()
    util.Try({
      val out = new FileOutputStream(file)
      out.write(prettyXml.getBytes)
      out.close()
    })
  }

  def xmlView = {
    <document>
      <places>
        {petriNetCanvas.placeViews.map(makeXMLFrom(_, PLACE))}
      </places>
      <transactions>
        {petriNetCanvas.trViews.map(makeXMLFrom(_, TRANSACTION))}
      </transactions>
    </document>
  }

  def parse(file: File) = {
    val xml = XML.loadFile(file)
    (xml \\ "places" \ PLACE).foreach(parseUICollection(_, petriNetCanvas.placeViews))
    (xml \\ "transactions" \ TRANSACTION).foreach(parseUICollection(_, petriNetCanvas.trViews))
  }
}

object XMLView {
  val PLACE = "place"
  val TRANSACTION = "transaction"

  def pos(pos: Point) = {
      <pos/> % Attribute(None, "x", Text(pos.x.toString), Null) %
        Attribute(None, "y", Text(pos.y.toString), Null)
  }

  def color(color: Color) = {
      <color r={color.getRed.toString} g={color.getGreen.toString} b={color.getBlue.toString}/>
  }

  def makeXMLFrom(el: UIElement, label: String = "UIElement"): Elem = {
    <labelToReplace>
      <id>{el.id}</id>
      { pos(el.pos) }
      { color(el.color) }
    </labelToReplace>.copy(label = label)
  }

  def parseUIElemnt(node: Node, elem: UIElement) = {
    elem.pos.move((node \ "pos" \ "@x").text.toInt, (node \ "pos" \ "@y").text.toInt)
    elem.color = new Color(
      (node \ "color" \ "@r").text.toInt,
      (node \ "color" \ "@g").text.toInt,
      (node \ "color" \ "@b").text.toInt
    )
  }

  def parseUICollection(node: Node, col: List[UIElement]) = {
    col.find(_.id == (node \ "id").text.toInt) match {
      case Some(placeView: UIElement) => parseUIElemnt(node, placeView)
      case _ => println("Error in xml")
    }
  }
}
