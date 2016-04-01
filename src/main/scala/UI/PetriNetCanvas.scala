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

  val placeViews: List[UIElement] = model.places.map(new PlaceView(_, nextColor))

  val delta = 20
  placeViews.indices.foreach(index => {
    val pos = placeViews(index).pos
    val rad = PlaceView.radius
    pos.move(index * 2 * rad + rad + index * delta, rad)
  })

  val trViews: List[UIElement] = model.transactions.map(new TransactionView(_))
  trViews.indices.foreach(index => {
    val pos = trViews(index).pos
    pos.move(PlaceView.radius * 2 * (index + 1) + delta * index + delta / 2, 2 * PlaceView.radius + TransactionView.height / 2)
  })

  override def paintComponent(g : Graphics2D) {
    val d = size
    g.setColor(Color.white)
    g.fillRect(0,0, d.width, d.height)

    target match {
      case Some(targetPV) =>
        (placeViews ::: trViews).foreach(view => {
          if (!view.equals(targetPV))
            view.paint(g)
        })
        targetPV.paint(g)
      case _ =>
        (placeViews ::: trViews).foreach(_.paint(g))
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

  var target: Option[UIElement] = None
  def mousePressedHandler(p: Point) = {
    println(s"Mouse clicked at ${p.x}, ${p.y}")
    target = (placeViews ::: trViews).find(_.isIn(p))
    println(s"Target = $target")
  }

  def mouseDraggedHandler(p: Point) = {
    target match {
      case Some(element: UIElement) =>
        println(s"Dragged to point ${p.x}, ${p.y}")
        element.pos.move(p.x, p.y)
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


