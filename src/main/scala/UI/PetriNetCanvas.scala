package UI

import java.awt.{Font, Color}
import java.awt.Color._

import model.Model

import scala.swing.event.{MouseDragged, MouseMoved, MouseReleased, MouseClicked}
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
    pos.move(index * 2 * rad, 0)
  })

  override def paintComponent(g : Graphics2D) {
    val d = size
    g.setColor(Color.white)
    g.fillRect(0,0, d.width, d.height)

    placeViews.foreach(placeView => {
      if (!placeView.equals(target))
        placeView.paint(g)
    })

    target.paint(g)
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
    case MouseClicked(_, p, _, _, _) => mouseClickHandler(p)
    case MouseReleased(_, p, _, _, _) => println(s"Mouse released at ${p.x}, ${p.y}")
    case MouseDragged(_, p, _) => mouseDraggedHandler(p)
  }
  var target: PlaceView = placeViews.head
  def mouseClickHandler(p: Point) = {
    println(s"Mouse clicked at ${p.x}, ${p.y}")
    target = placeViews.head
  }

  def mouseDraggedHandler(p: Point) = {
    println(s"Dragged to point ${p.x}, ${p.y}")
    target.pos.move(p.x - target.radius, p.y - target.radius)
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


