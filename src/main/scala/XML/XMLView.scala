package XML

import java.awt.Color
import java.io.{FileOutputStream, File}

import UI.{TransactionView, PlaceView, PetriNetCanvas}

import scala.swing.Point
import scala.xml._

/**
  * Created by neikila.
  */
class XMLView (val petriNetCanvas: PetriNetCanvas) {
  import XMLView._

  def save(file: File) = {
    val xml =
      <document>
        <places>
          {petriNetCanvas.placeViews.map(el => el.asInstanceOf[PlaceView]).map(el =>
          <place>
            <id>{el.place.id}</id>
            { pos(el.pos) }
            { color(el.color) }
          </place>
        )}
        </places>
        <transactions>
          {petriNetCanvas.trViews.map(el => el.asInstanceOf[TransactionView]).map(el =>
          <transaction>
            <id>{el.transaction.id}</id>
            { pos(el.pos) }
            { color(el.color) }
          </transaction>
        )}
        </transactions>
      </document>

    val prettyPrinter = new scala.xml.PrettyPrinter(80, 2)
    val prettyXml = prettyPrinter.format(xml)
    file.createNewFile()
    util.Try({
      val out = new FileOutputStream(file)
      out.write(prettyXml.getBytes)
      out.close()
    })
  }
}

object XMLView {
  def pos(pos: Point) = {
      <pos/> % Attribute(None, "x", Text(pos.x.toString), Null) %
        Attribute(None, "y", Text(pos.y.toString), Null)
  }

  def color(color: Color) = {
      <color/> % Attribute(None, "r", Text(color.getRed.toString), Null) %
        Attribute(None, "g", Text(color.getGreen.toString), Null) %
        Attribute(None, "b", Text(color.getBlue.toString), Null)
  }
}
