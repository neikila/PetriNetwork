package UI


import java.awt.Color

import scala.collection.mutable
import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class ArcView(val from: UIElement, val to: UIElement) {
  val points = mutable.MutableList[Point]()

  def paint(g: Graphics2D, k: Double = 1) = {
    val from = this.from.getPointForArc(this.to.pos)
    val to = this.to.getPointForArc(this.from.pos)
    g.setColor(Color.BLACK)
    drawLine(from.x / k, from.y / k, to.x / k, to.y / k, g)

    drawLine(to, from, 15, g, k)
    drawLine(to, from, -15, g, k)
  }

  def drawLine(from: Point, to: Point, degree: Double, g: Graphics2D, k: Double = 1): Unit = {
    val vector = Helper.vector(from, Helper.vectorDiv(from, to, ArcView.arrowLength / to.distance(from)))
    val temp = Helper.rotate(vector, degree)
    drawLine(from.x / k, from.y / k, (from.x + temp.x) / k, (from.y + temp.y) / k, g)
  }

  def drawLine(x1: Double, y1: Double, x2: Double, y2: Double, g: Graphics2D): Unit = {
    g.drawLine(x1.toInt, y1.toInt, x2.toInt, y2.toInt)
  }
}

object ArcView {
  val arrowLength: Double = 40
}
