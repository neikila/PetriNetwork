import java.io.File

import UI.MainWindow
import _root_.XML.{XMLModel, XMLModel$}
import model._

import scala.collection.immutable.HashMap
/**
  * Created by Neikila on 30.03.2016.
  */
class Main {

}

object Main {
  def main(args: Array[String]): Unit = {
    MainWindow(new Model())
  }
}
