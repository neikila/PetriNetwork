package UI

import scala.swing.Point
import math.{sin, cos, toRadians}

/**
  * Created by neikila.
  */
object Helper {

  def vectorDiv(from: Point, to: Point, k: Double): Point = {
    vectorDiv(from, to, k, from.distance(to))
  }

  def vectorDiv(from: Point, to: Point, k: Double, dist: Double): Point = {
    new Point(
      (from.x + (to.x - from.x) * k).toInt,
      (from.y + (to.y - from.y) * k).toInt
    )
  }

  def rotate(vector: Point, degrees: Double) = {
    new Point(
      (vector.x * cos(toRadians(degrees)) - vector.y * sin(toRadians(degrees))).toInt,
      (vector.x * sin(toRadians(degrees)) + vector.y * cos(toRadians(degrees))).toInt
    )
  }

  def vector(from: Point, to: Point) = {
    new Point(
      to.x - from.x,
      to.y - from.y
    )
  }
}
