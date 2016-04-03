package UI

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
trait UIElement {
  val pos: Point

  def isIn(p: Point): Boolean

  def paint(g: Graphics2D, k: Double = 1, camera: Point = new Point(0, 0)): Unit

  def getPointForArc(second: Point): Point
}
