package UI

import java.awt.{Color, Font}
import java.awt.Color._
import java.io.File
import javax.swing.{JPopupMenu, SwingUtilities}

import XML.XMLView
import model.{Model, P2T, T2P, TransactionApplyResult}

import scala.swing.event._
import scala.swing.{Action, Button, Component, Graphics2D, Menu, MenuItem, Point, PopupMenu, RadioMenuItem}

/**
  * Created by neikila.
  */
class PetriNetCanvas (var model: Model, var file: Option[File] = None) extends Component {
  import PetriNetCanvas._

  var k: Double = 1
  var camera: Point = new Point(size.width / 2, size.height / 2)

  var placeViews: List[UIElement] = model.places.map(new PlaceView(_, placeDefColor))
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
    model.arcs.map({
      case (p2t: P2T) =>
        new ArcView(
          placeViews.find(_.id == p2t.from.id).get,
          trViews.find(_.id == p2t.to.id).get
        )
      case (t2p: T2P) =>
        new ArcView(
          trViews.find(_.id == t2p.from.id).get,
          placeViews.find(_.id == t2p.to.id).get
        )
    })

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
    case e: MouseClicked => mouseClickHandler(e)
    case e: MouseReleased => mouseReleasedHandler(e)
    case MouseDragged(_, p, _) => mouseDraggedHandler(p)
    case e: MouseWheelMoved => wheelRotationHandler(e)
  }

//  val dialog = new CreatePlaceDialog(counter => {
//    if (counter >= 0) {
//      placeViews = placeViews :+ new PlaceView(
//        model.addPlace(counter),
//        nextColor,
//        clickedPoint.get)
//      update()
//      true
//    } else false
//  })
//
//  dialog.centerOnScreen()
//  dialog.open()

  var clickedPoint: Option[Point] = None
  val createMenu = new PopupMenu {
    contents += new Menu("Create default") {
      val place = new MenuItem(Action("Place") {
        placeViews = placeViews :+ new PlaceView(
          model.addPlace(),
          placeDefColor,
          clickedPoint.get)
        update()
      })

      val transaction = new MenuItem(Action("Transaction") {
        trViews = trViews :+ new TransactionView(
          model.addTransaction(),
          clickedPoint.get)
        update()
      })

      var arc = new MenuItem(Action("Arc") {
        arcCreation.isActive = true
      })
//      place.tooltip_=("Show dialog to create a place in clicked point")

      contents += place
      contents += transaction
      contents += arc
    }
  }

  val arcCreation = new ArcCreation
  val cancelMenu = new PopupMenu {
    val edit = new MenuItem(Action("Cancel") {
      arcCreation.toNone()
      update()
    })
    edit.tooltip_=("Cancel arc creation")

    contents += edit
  }

  val onPlaceMenu = new PopupMenu {
    val edit = new MenuItem(Action("Edit") {
    })
    edit.tooltip_=("Still not implemented =(")

    contents += edit
  }

  class ArcCreation {
    var isActive = false
    var from: Option[UIElement] = None
    var to: Option[UIElement] = None

    def arc: Option[ArcView] = {
      from match {
        case Some(place: PlaceView) =>
          to match {
            case Some(tr: TransactionView) =>
              model.addP2T(place.place, tr.transaction)
              Some(new ArcView(place, tr))
            case _ => None
          }
        case Some(tr: TransactionView) =>
          to match {
            case Some(place: PlaceView) =>
              model.addT2P(tr.transaction, place.place)
              Some(new ArcView(tr, place))
            case _ => None
          }
        case _ =>
          None
      }
    }

    //noinspection AccessorLikeMethodIsUnit
    def toNone() = {
      from match {
        case Some(place: PlaceView) => place.color = placeDefColor
        case Some(tr: TransactionView) => tr.color = transactionDefColor
        case _ =>
      }
      from = None
      to = None
      isActive = false
    }
  }

  def mouseClickHandler(e: MouseClicked) = {
    if (arcCreation.isActive && lastSelected.isDefined && SwingUtilities.isLeftMouseButton(e.peer)) {
      arcCreation.from match {
        case None =>
          arcCreation.from = lastSelected
          arcCreation.from.get.color = selectedColor
          update()
        case _ =>
          arcCreation.to = lastSelected
          arcCreation.arc match {
            case Some(arcView: ArcView) =>
              arcViews = arcViews :+ arcView
              arcCreation.toNone()
              model.enableActTransaction()
              update()
            case _ =>
              println("Impossible to create an arc with such params")
              // TODO Error message
          }
      }
    }
  }

  var target: Option[UIElement] = None
  var lastSelected: Option[UIElement] = None
  var firstPressed = -1.0

  def mousePressedHandler(e: MousePressed) = {
    target = (placeViews ::: trViews).find(_.isIn(e.point))
    lastSelected = target
    if (SwingUtilities.isLeftMouseButton(e.peer)) {
      firstPressed = java.lang.System.currentTimeMillis
    }
    if (SwingUtilities.isRightMouseButton(e.peer)) {
      clickedPoint = Some(e.point)
      if (arcCreation.isActive)
        cancelMenu.show(this, e.point.x, e.point.y)
      else
        target match {
          case Some(placeView: PlaceView) =>
          case Some(trView: TransactionView) =>
          case _ =>
            createMenu.show(this, e.point.x, e.point.y)
        }
    }
  }

  def mouseReleasedHandler(e: MouseReleased) = {
    if (!arcCreation.isActive && java.lang.System.currentTimeMillis - firstPressed < 300)
      target match {
        case Some(tr: TransactionView) =>
          if (tr.transaction.isPossible)
            model.nextWith(tr.transaction) match {
              case TransactionApplyResult.Success =>
                model.enableActTransaction()
                update()
              case _ =>
            }
        case _ =>
      }
    target = None
    update()
  }

  def mouseDraggedHandler(p: Point) = {
    target match {
      case Some(element: UIElement) =>
        element.pos.move(p.x, p.y)
      case _ =>
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
    BLUE :: RED :: CYAN :: DARK_GRAY :: ORANGE ::Nil
  }
  var last = 0

  def nextColor = {
    last = (last + 1) % colors.length
    colors(last)
  }

  val placeDefColor = CYAN
  val transactionDefColor = BLACK
  val selectedColor = ORANGE
}


