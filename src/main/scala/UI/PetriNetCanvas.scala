package UI

import java.awt.{Color, Font}
import java.awt.Color._
import java.io.File
import javax.swing.SwingUtilities

import XML.XMLView
import model.Model

import scala.swing.event._
import scala.swing.{Component, Graphics2D, Point}

/**
  * Created by neikila.
  */
class PetriNetCanvas (var model: Model, var file: Option[File] = None) extends Component {
  import PetriNetCanvas._

  var k: Double = 1
  var camera: Point = new Point(size.width / 2, size.height / 2)

  var placeViews: List[UIElement] = model.places.map(new PlaceView(_, nextColor))
  var trViews: List[UIElement] = model.transactions.map(new TransactionView(_))
  var arcViews: List[ArcView] = getArcsViewFromModel

  def initView(): Unit =
    file match {
      case Some(file: File) =>
        val xmlView = new XMLView(this)
        xmlView.parse(file)
      case _ =>
        val delta = 20
        placeViews.indices.foreach(index => {
          val pos = placeViews(index).pos
          val rad = PlaceView.radius
          pos.move(index * 2 * rad + rad + index * delta, rad)
        })
        trViews.indices.foreach(index => {
          trViews(index).pos.move(
            PlaceView.radius * 2 * (index + 1) + delta * index + delta / 2,
            2 * PlaceView.radius + TransactionView.height / 2
          )
        })
    }

  def initView(model: Model, file: File): Unit = {
    this.model = model
    this.file = Some(file)
    placeViews = model.places.map(new PlaceView(_, nextColor))
    trViews = model.transactions.map(new TransactionView(_))
    arcViews = getArcsViewFromModel
    initView()
  }

  initView()

  def getArcsViewFromModel =
    model.arcsPlace2Tr.map(p2t =>
      new ArcView(
        placeViews.find(_.asInstanceOf[PlaceView].place.id == p2t.from.id).get,
        trViews.find(_.asInstanceOf[TransactionView].transaction.id == p2t.to.id).get
      )
    ) :::
      model.arcsTr2Place.map(t2p => new ArcView(
        trViews.find(_.asInstanceOf[TransactionView].transaction.id == t2p.from.id).get,
        placeViews.find(_.asInstanceOf[PlaceView].place.id == t2p.to.id).get)
      )

  override def paintComponent(g : Graphics2D) {
    val d = size
    g.setColor(Color.white)
    g.fillRect(0,0, d.width, d.height)

    arcViews.foreach(_.paint(g, k))

    target match {
      case Some(targetPV) =>
        (placeViews ::: trViews).foreach(view => {
          if (!view.equals(targetPV))
            view.paint(g, k, camera)
        })
        targetPV.paint(g, k, camera)
      case _ =>
        (placeViews ::: trViews).foreach(_.paint(g, k ,camera))
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
  listenTo(mouse.wheel)
  reactions += {
    case e: MousePressed => mousePressedHandler(e)
    case MouseReleased(_, p, _, _, _) => target = None; update(); println(s"Mouse released at ${p.x}, ${p.y}")
    case MouseDragged(_, p, _) => mouseDraggedHandler(p)
    case e: MouseWheelMoved => wheelRotationHandler(e)
  }

  var target: Option[UIElement] = None
  def mousePressedHandler(e: MousePressed) = {
    println(s"Mouse clicked at ${e.point.x}, ${e.point.y}. ${e.modifiers}")
    println(SwingUtilities.isLeftMouseButton(e.peer))
    println(SwingUtilities.isRightMouseButton(e.peer))
    target = (placeViews ::: trViews).find(_.isIn(e.point))
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

  def wheelRotationHandler(e: MouseWheelMoved) = {
    if (k >= 1) {
      if (k == 1 && e.rotation < 0)
        k += e.rotation.toDouble / 10
      else
        k += e.rotation.toDouble / 4
    } else {
      if (k > 0.2 && e.rotation < 0 || e.rotation > 0)
        k += (e.rotation.toDouble / 10)
    }
    println(s"Wheel moved: ${e.rotation}. k = $k")
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


