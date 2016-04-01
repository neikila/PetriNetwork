package UI

import java.awt.{Rectangle, Color}

import model.Transaction

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class TransactionView (val transaction: Transaction, override val pos: Point = new Point(0, 0)) extends UIElement {
  import TransactionView._

  var color = Color.BLACK

  def rectangle = new Rectangle(pos.x - width / 2, pos.y - height / 2, width, height)

  override def paint(g: Graphics2D): Unit = {
    g.setColor(color)
    val rectangle = this.rectangle
    g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height)
  }

  override def isIn(p: Point): Boolean = {
    rectangle.contains(p)
  }

  override def getPointForArc(second: Point): Point = {
    val rect = rectangle
    val dist = pos.distance(second)
    getPoint(second, dist, (0.0 to dist by 0.5).find(k => {
      !rect.contains(getPoint(second, dist, k))
    }).getOrElse(dist))
//    vectorDiv(pos, second, dist, (0.0 to dist by 0.5).find(k => {
//      !rect.contains(vectorDiv(pos, second, dist, k))
//    }).getOrElse(dist))
  }

  def getPoint(second: Point, dist: Double, k: Double) = {
    new Point((pos.x + (second.x - pos.x) * k / dist).toInt, (pos.y + (second.y - pos.y) * k / dist).toInt)
  }
}

object TransactionView {
  val width = 20
  val height = 60
}
