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
    val from = this.from.pos
    val to = this.to.pos
    g.setColor(Color.GREEN)
    g.drawLine(from.x, from.y, to.x, to.y)
  }
}
