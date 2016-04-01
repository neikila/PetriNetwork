package UI

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
trait UIElement {
  def isIn(p: Point): Boolean

  def paint(g: Graphics2D): Unit
}
