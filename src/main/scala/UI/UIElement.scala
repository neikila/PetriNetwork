package UI

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
trait UIElement {
  val pos: Point

  def isIn(p: Point): Boolean

  def paint(g: Graphics2D): Unit
}
