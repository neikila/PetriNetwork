package UI


import java.awt.Color

import scala.collection.mutable
import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class ArcView(val from: UIElement, val to: UIElement) {
  val points = mutable.MutableList[Point]()

  def paint(g: Graphics2D, k: Double = 1, camera: Point = new Point(0, 0)) = {
    val from = this.from.getPointForArc(this.to.pos)
    val to = this.to.getPointForArc(this.from.pos)
    g.setColor(Color.BLACK)
    drawLine(from.x / k, from.y / k, to.x / k, to.y / k, g, camera)

    drawLine(to, from, ArcView.arrowDegree, g, k, camera)
    drawLine(to, from, -ArcView.arrowDegree, g, k, camera)
  }

  def drawLine(from: Point, to: Point, degree: Double,
               g: Graphics2D, k: Double = 1, camera: Point = new Point(0, 0)): Unit = {
    val vector = Helper.vector(from, Helper.vectorDiv(from, to, ArcView.arrowLength / to.distance(from)))
    val temp = Helper.rotate(vector, degree)
    drawLine(from.x / k, from.y / k, (from.x + temp.x) / k, (from.y + temp.y) / k, g, camera)
  }

  def drawLine(x1: Double, y1: Double, x2: Double, y2: Double, g: Graphics2D, camera: Point): Unit = {
    g.drawLine(x1.toInt - camera.x, y1.toInt - camera.y, x2.toInt - camera.x, y2.toInt - camera.y)
  }
}

object ArcView {
  val arrowLength: Double = 40
  val arrowDegree = 15
}
