package UI

import java.awt.{Font, Color}
import java.awt.Color._

import model.Model

import scala.swing.event._
import scala.swing.{Point, Graphics2D, Component}

/**
  * Created by neikila.
  */
class PetriNetCanvas (val model: Model) extends Component {
  import PetriNetCanvas._

  val placeViews = model.places.map(new PlaceView(_, nextColor))

  placeViews.indices.foreach(index => {
    val pos = placeViews(index).pos
    val rad = placeViews(index).radius
    val delta = 20
    pos.move(index * 2 * rad + rad + index * delta, rad)
  })

  override def paintComponent(g : Graphics2D) {
    val d = size
    g.setColor(Color.white)
    g.fillRect(0,0, d.width, d.height)

    target match {
      case Some(targetPV) =>
        placeViews.foreach(placeView => {
          if (!placeView.equals(targetPV))
            placeView.paint(g)
        })
        targetPV.paint(g)
      case _ => placeViews.foreach(_.paint(g))
    }
  }

  def update() = {
    revalidate
    repaint
  }

  def updateImmediate(g: Graphics2D) = {
    paintComponent(g)
  }

  listenTo(mouse.clicks)
  listenTo(mouse.moves)
  reactions += {
    case MousePressed(_, p, _, _, _) => mousePressedHandler(p)
    case MouseReleased(_, p, _, _, _) => println(s"Mouse released at ${p.x}, ${p.y}")
    case MouseDragged(_, p, _) => mouseDraggedHandler(p)
  }

  var target: Option[PlaceView] = None
  def mousePressedHandler(p: Point) = {
    println(s"Mouse clicked at ${p.x}, ${p.y}")
    target = placeViews.find(_.isIn(p))
    println(s"Target = $target")
  }

  def mouseDraggedHandler(p: Point) = {
    target match {
      case Some(placeView: PlaceView) =>
        println(s"Dragged to point ${p.x}, ${p.y}")
        placeView.pos.move(p.x, p.y)
      case _ => None
    }
    update()
  }
}

object PetriNetCanvas {

  val colors: List[Color] = {
    BLUE :: RED :: CYAN :: GREEN :: ORANGE ::Nil
  }
  var last = 0

  def nextColor = {
    last = (last + 1) % colors.length
    colors(last)
  }
}


