import java.io.File

import UI.MainWindow
import _root_.XML.XMLComplex
import model._

import scala.collection.immutable.HashMap
/**
  * Created by Neikila on 30.03.2016.
  */
class Main {

}

object Main {
  def main(args: Array[String]): Unit = {
    val file = new File(XMLComplex.defaultProjectsDirectory)
    require(file.exists && file.isDirectory, s"Please, create directory ${file.getAbsolutePath}")
    MainWindow()
  }
}
