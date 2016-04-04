package UI

import java.awt.{Font, Color}

import model.Place

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class PlaceView (val place: Place, override var color: Color = Color.BLUE, override val pos: Point = new Point(0, 0)) extends UIElement {

  def pos(k: Double): Point =
    new Point((pos.x / k).toInt, (pos.y / k).toInt)

  override def paint(g: Graphics2D, k: Double = 1, camera: Point = new Point(0, 0)) = {

    g.setColor(color)
    g.fillOval(pos(k).x - radius(k).toInt - camera.x, pos(k).y - radius(k).toInt - camera.y,
      2 * radius(k).toInt, 2 * radius(k).toInt)

    g.setColor(Color.BLACK)
    g.setFont(PlaceView.serifFont(k))
    val fontMetrics = g.getFontMetrics
    val marksAmount = place.counter.toString

    val rect = fontMetrics.getStringBounds(marksAmount, g)

    g.drawString(marksAmount,
      (pos(k).x - rect.getWidth / 2 - camera.x).toInt,
      (pos(k).y + rect.getHeight / 4 - camera.y).toInt
    )
  }

  override def isIn(p: Point) = {
    pos.distanceSq(p) <= radius() * radius()
  }

  override def getPointForArc(second: Point): Point = {
    val k = radius() / pos.distance(second)
    Helper.vectorDiv(pos, second, k)
  }

  override def id: Int = place.id

  def radius(k: Double = 1) = {
    PlaceView.radius / k
  }
}

object PlaceView {
  val radius: Int = 40
  val serifFont = new Font("Serif", Font.BOLD, 24)
  def serifFont(k: Double) = new Font("Serif", Font.BOLD, (24 / k).toInt)
}
