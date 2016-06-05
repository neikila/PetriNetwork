package UIDiplom

import java.awt.{BasicStroke, Color}
import java.awt.Color._
import java.io.File
import javax.swing.SwingUtilities

import Petri.model.XML.XMLView
import UIDiplom.dialogs.{CreateBarrierDialog, CreateRackDialog}
import resource.XMLStorageParser
import storageModel.{Model, Storage}
import storageModel.storageDetails.{Barrier, Rack}

import scala.swing.event._
import scala.swing.{Action, Component, Graphics2D, Menu, MenuItem, Point, PopupMenu}
import scala.collection.JavaConversions._

/**
  * Created by neikila.
  */
class StorageViewCanvas(var file: Option[File] = None) extends Component {
  import StorageViewCanvas._

  var k: Double = 0.04
  var camera: Point = new Point(0, 0)

  var storageSettings: Option[XMLStorageParser] = None

  val fat = new BasicStroke(3)
  val thin = new BasicStroke(1)

  lazy val racks : List[Rack] = storageSettings.get.getRacks.toList
  lazy val barriers : List[Barrier] = storageSettings.get.getBarriers.toList
  lazy val points : List[java.awt.Point] = storageSettings.get.getWallPoints.toList

  override def paintComponent(g : Graphics2D) {
    val d = size
    g.setColor(Color.white)
    g.fillRect(0,0, d.width, d.height)

    storageSettings match {
      case Some(settings) =>
        // Закрасить серым фон
        drawField(g)

        // Отобразить все барьеры
        drawBarriers(g)

        // Отобразить все стеллажи
        drawRacks(g)

        // Отрисовать сетку
        drawMesh(g, Storage.box)

        // Отрисовать границы
        drawBounds(g)

      case _ =>
        println("model is null")
    }
  }

  def drawBarriers(g : Graphics2D): Unit = {
    g.setColor(RED)
    barriers.foreach(b => {
      val xs = b.getPolygon.xpoints.toList.map(x => (x * k - camera.x).toInt).toArray
      val ys = b.getPolygon.ypoints.toList.map(y => (y * k - camera.y).toInt).toArray
      g.fillPolygon(xs, ys, b.getPolygon.npoints)
    })
  }

  def drawRacks(g : Graphics2D): Unit = {
    g.setColor(GREEN)
    racks.foreach(r => {
      val p = scalePointAndMove(r.getCoordinate)
      val s = scalePoint(r.getSize)
      g.fillRect(p.x, p.y, s.x, s.y)
    })
  }

  def drawMesh(g : Graphics2D, box : java.awt.Point): Unit = {
    val d = size
    g.setColor(BLACK)
    val offsetX = camera.x % (box.x * k)
    var x = -offsetX.toInt
    var i = 0
    while (x < d.width) {
      g.drawLine(x - 1, 0, x - 1, d.height)
      i += 1
      x = (i * box.x * k - offsetX).toInt
    }


    val offsetY = camera.y % (box.y * k)
    var y = -offsetY.toInt
    i = 0
    while (y < d.height) {
      g.drawLine(0, y - 1, d.width, y - 1)
      i += 1
      y = (i * box.y * k - offsetY).toInt
    }
  }

  def drawBounds(g : Graphics2D): Unit = {
    val xs = points.map(p => (k * p.x).toInt - camera.x).toArray
    val ys = points.map(p => (k * p.y).toInt - camera.y).toArray

    g.setColor(CYAN)
    g.setStroke(fat)
    g.drawPolygon(xs, ys, points.length)
    g.setStroke(thin)
  }

  def drawField(g : Graphics2D): Unit = {
    val xs = points.map(p => (k * p.x).toInt - camera.x).toArray
    val ys = points.map(p => (k * p.y).toInt - camera.y).toArray

    g.setColor(LIGHT_GRAY)
    g.fillPolygon(xs, ys, points.length)
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
    case e: MouseDragged => mouseDraggedHandler(e)
    case e: MouseWheelMoved => wheelRotationHandler(e)
  }

  var clickedPoint: Option[Point] = None
  val createMenu = new PopupMenu {
    contents += new Menu("Create") {
      val rack = new MenuItem(Action("Rack") {
        val dialog = new CreateRackDialog
      })

      val barrier = new MenuItem(Action("Barrier") {
        val dialog = new CreateBarrierDialog
      })

      contents += rack
      contents += barrier
    }
  }

  val cancelMenu = new PopupMenu {
    val cancel = new MenuItem(Action("Cancel") {
//      arcCreation.toNone()
//      update()
    })
    cancel.tooltip_=("Cancel arc creation")

    contents += cancel
  }

  val UIElementMenu = new PopupMenu {
//    val edit = new MenuItem(Action("Edit") {
//      lastSelected match {
//        case Some(placeView: PlaceView) =>
//          val dialog = new EditIntFieldDialog(counter => {
//            if (counter >= 0) {
//              placeView.place.counter = counter
//              Petri.model.enableActTransaction()
//              update()
//              true
//            } else false
//          }, "Apply", defaultValue = placeView.place.counter)
//
//          dialog.centerOnScreen()
//          dialog.open()
//        case Some(tr: TransactionView) =>
//          val dialog = new EditIntFieldDialog(priority => {
//            if (priority >= 0) {
//              tr.transaction.priority = priority
//              true
//            } else false
//          }, "Apply", "Priority", "Transition", tr.transaction.priority)
//
//          dialog.centerOnScreen()
//          dialog.open()
//        case _ =>
//      }
//    })
//    edit.tooltip_=("Counter for places. Priority for transitions")

    val remove = new MenuItem(Action("Remove") {
//      lastSelected match {
//        case Some(placeView: PlaceView) =>
//          placeViews = placeViews.filter(_.id != placeView.id)
//          Petri.model.remove(placeView.place)
//          arcViews = getArcsViewFromModel
//        case Some(tr: TransactionView) =>
//          trViews = trViews.filter(_.id != tr.id)
//          Petri.model.remove(tr.transaction)
//          arcViews = getArcsViewFromModel
//        case _ =>
//          println("WTF")
//      }
//      Petri.model.enableActTransaction()
//      update()
    })
//    remove.tooltip_=("Cascade mod")
//
//    contents += edit
//    contents += remove
  }

    //noinspection AccessorLikeMethodIsUnit
    def toNone() = {
//      from match {
//        case Some(place: PlaceView) => place.color = placeDefColor
//        case Some(tr: TransactionView) => tr.color = transactionDefColor
//        case _ =>
//      }
//      from = None
//      to = None
//      isActive = false
//    }
  }

  def mouseClickHandler(e: MouseClicked) = {
//    if (arcCreation.isActive && lastSelected.isDefined && SwingUtilities.isLeftMouseButton(e.peer)) {
//      arcCreation.from match {
//        case None =>
//          arcCreation.from = lastSelected
//          arcCreation.from.get.color = selectedColor
//          update()
//        case _ =>
//          arcCreation.to = lastSelected
//          arcCreation.arc match {
//            case Some(arcView: ArcView) =>
//              arcViews = arcViews :+ arcView
//              arcCreation.toNone()
//              Petri.model.enableActTransaction()
//              update()
//            case _ =>
//              println("Impossible to create an arc with such params")
//              // TODO Error message
//          }
//      }
//    }
  }

//  var target: Option[UIElement] = None
//  var lastSelected: Option[UIElement] = None
  var firstPressed = -1.0

  def mousePressedHandler(e: MousePressed) = {
    lastDragPoint = new Point(e.point)
//    target = (placeViews ::: trViews).find(_.isIn(toWorld(e.point)))
//    lastSelected = target
    firstPressed = java.lang.System.currentTimeMillis
  }

  var target: Option[Any] = None

  def mouseReleasedHandler(e: MouseReleased) = {
    if (java.lang.System.currentTimeMillis - firstPressed < 300) {
      if (SwingUtilities.isRightMouseButton(e.peer)) {
        clickedPoint = Some(e.point)
        target match {
          case Some(element) =>
            UIElementMenu.show(this, e.point.x, e.point.y)
          case _ =>
            createMenu.show(this, e.point.x, e.point.y)
        }
      }
    }
    target = None
    update()
  }

  var lastDragPoint: Point = new Point(0, 0)
  def mouseDraggedHandler(e: MouseDragged) = {
    if (SwingUtilities.isLeftMouseButton(e.peer))
      println("WTF")
//      target match {
//        case Some(element: UIElement) =>
//          element.pos.translate(((e.point.x - lastDragPoint.x) * k).toInt, ((e.point.y - lastDragPoint.y) * k).toInt)
//        case _ =>
//      }
    if (SwingUtilities.isRightMouseButton(e.peer)) {
      camera.translate(lastDragPoint.x - e.point.x, lastDragPoint.y - e.point.y)
    }
    lastDragPoint = e.point
    update()
  }

  def wheelRotationHandler(e: MouseWheelMoved) = {
    if (k >= 1) {
      if (k == 1 && e.rotation < 0)
        k += e.rotation.toDouble / 10
      else
        k += e.rotation
    } else {
      if (k > 0.2)
        k += (e.rotation.toDouble / 10)
      else if (k > 0.03)
        k += (e.rotation.toDouble / 50)
      else if (e.rotation > 0)
        k += (e.rotation.toDouble / 50)
    }
    update()
  }

  def toWorld(p: Point) = {
    new Point(
      ((p.x + camera.x) * k).toInt,
      ((p.y + camera.y) * k).toInt
    )
  }

  def scalePointAndMove(p: java.awt.Point) = {
    new Point(
      (p.x * k).toInt - camera.x,
      (p.y * k).toInt - camera.y
    )
  }

  def scalePoint(p: java.awt.Point) = {
    new Point(
      (p.x * k).toInt,
      (p.y * k).toInt
    )
  }
}

object StorageViewCanvas {
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


