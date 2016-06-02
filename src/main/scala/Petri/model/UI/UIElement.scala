package Petri.model.UI

import java.awt.Color

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
trait UIElement {
  val pos: Point
  var color: Color

  def isIn(p: Point): Boolean

  def paint(g: Graphics2D, k: Double = 1, camera: Point = new Point(0, 0)): Unit

  def getPointForArc(second: Point): Point

  def id: Int
}
