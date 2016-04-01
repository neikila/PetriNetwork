package UI

import java.awt.{Rectangle, Color}

import model.Transaction

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class TransactionView (val transaction: Transaction, val pos: Point = new Point(0, 0)) extends UIElement {
  val width = 6
  val height = 20
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
}
