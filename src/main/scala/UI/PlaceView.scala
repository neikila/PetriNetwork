package UI

import java.awt.{Font, Color}

import model.Place

import scala.swing.{Graphics2D, Point}

/**
  * Created by neikila.
  */
class PlaceView (val place: Place, var color: Color = Color.BLUE, var pos: Point = new Point(0, 0)) {
  val radius: Int = 40

  def paint(g: Graphics2D) = {

    g.setColor(color)
    g.fillOval(pos.x, pos.y, 2 * radius, 2 * radius)

    g.setColor(Color.BLACK)
    g.setFont(PlaceView.serifFont)
    val fontMetrics = g.getFontMetrics
    val marksAmount = place.counter.toString

    val rect = fontMetrics.getStringBounds(marksAmount, g)

    g.drawString(marksAmount,
      pos.x + radius - rect.getWidth / 2 toInt,
      pos.y + radius + rect.getHeight / 2 toInt
    )
  }
}

object PlaceView {
  val serifFont = new Font("Serif", Font.BOLD, 24)
}
