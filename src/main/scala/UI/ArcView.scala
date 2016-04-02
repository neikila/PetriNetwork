package UI


import java.awt.Color

import scala.collection.mutable
import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class ArcView(val from: UIElement, val to: UIElement) {
  val points = mutable.MutableList[Point]()

  def paint(g: Graphics2D) = {
    val from = this.from.getPointForArc(this.to.pos)
    val to = this.to.getPointForArc(this.from.pos)
    g.setColor(Color.BLACK)
    g.drawLine(from.x, from.y, to.x, to.y)

    drawLine(to, from, 15, g)
    drawLine(to, from, -15, g)
  }

  def drawLine(from: Point, to: Point, degree: Double, g: Graphics2D) = {
    val vector = Helper.vector(from, Helper.vectorDiv(from, to, ArcView.arrowLength / to.distance(from)))
    val temp = Helper.rotate(vector, degree)
    g.drawLine(from.x, from.y, from.x + temp.x, from.y + temp.y)
  }
}

object ArcView {
  val arrowLength: Double = 40
}
