package UIDiplom

import java.io.File

import Petri.model.XML.XMLComplex
import UIDiplom.dialogs.ModelSettingsDialog
import main.Launcher

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.swing._
import scala.util.{Failure, Success}

/**
  * Created by neikila.
  */
class MainWindow () extends MainFrame {

  title = "Модель склада"
  var launcher: Launcher = null
  def model = launcher.getModel

  preferredSize = new Dimension(1000, 750)

  contents = new GridBagPanel { grid =>
    def constraints(x: Int, y: Int,
                    gridWidth: Int = 1, gridHeight: Int = 1,
                    weightX: Double = 0.0, weightY: Double = 0.0,
                    fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None): Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridWidth
      c.gridheight = gridHeight
      c.weightx = weightX
      c.weighty = weightY
      c.fill = fill
      c
    }

    val storageView = new StorageViewCanvas()
    val textField = new TextField { columns = 32 }
    val saveFileBtn: Button = Button("Сохранить проект") {
      val fileChooser = new FileChooser()
      if (new File(XMLComplex.defaultProjectsDirectory).exists())
        fileChooser.peer.setCurrentDirectory(new File(XMLComplex.defaultProjectsDirectory))
      else
        fileChooser.peer.setCurrentDirectory(new File("."))
      fileChooser.showSaveDialog(grid)
      if (fileChooser.selectedFile != null) {
        val filename = fileChooser.selectedFile.getName
        textField.text = filename
        Future {
//          XMLComplex.saveProject(fileChooser.selectedFile, petriView)
        } onComplete {
          case Success(_) =>
            println(s"Filename = ${fileChooser.selectedFile.getCanonicalPath}")
            repaint()
          case Failure(e) => println(e.getLocalizedMessage)
        }
      }
    }

    val openFileBtn: Button = Button("Открыть проект") {
      val fileChooser = new FileChooser()
      if (new File(XMLComplex.defaultProjectsDirectory).exists())
        fileChooser.peer.setCurrentDirectory(new File(XMLComplex.defaultProjectsDirectory))
      else
        fileChooser.peer.setCurrentDirectory(new File("."))
      fileChooser.showOpenDialog(grid)
      Future {
        launcher = new Launcher()
        launcher.readSettings(new Array[String](0))
      } onComplete {
        case Success(_) =>
          storageView.storageSettings = Some(launcher.getSettings.getStorageSettings)
          storageView.update()
          println("Open and drew storage")
//          println(s"Filename = ${fileChooser.selectedFile.getCanonicalPath}")
          repaint()
        case Failure(error) =>
          println("A error has occured: " + error.getMessage)
      }
//      if (fileChooser.selectedFile != null) {
//        val filename = fileChooser.selectedFile.getName
//        textField.text = filename
//        Future {
//
//        } onComplete {
//          case Success(_) =>
//            println(s"Filename = ${fileChooser.selectedFile.getCanonicalPath}")
//            repaint()
//          case Failure(error) =>
//            println("A error has occured: " + error.getMessage)
//        }
//      }
    }

    val startModellingBtn = Button("Запустить моделирование") {
        Future {
          launcher.init()
          launcher.start()
        } onComplete {
          case Success(_) =>
            storageView.update()
          case Failure(error) =>
            println("A error has occured: " + error.getMessage)
        }
      }


    add(saveFileBtn, constraints(2, 0, fill = GridBagPanel.Fill.Horizontal))
    add(openFileBtn, constraints(2, 1, fill = GridBagPanel.Fill.Horizontal))
    add(startModellingBtn, constraints(2, 2, fill = GridBagPanel.Fill.Horizontal))
    add(Button("Настройки моделлирования") { println("Modelling settings"); new ModelSettingsDialog },
      constraints(2, 3, fill=GridBagPanel.Fill.Horizontal))

    add(textField, constraints(0, 0, weightX=1.0, fill=GridBagPanel.Fill.Horizontal))
    add(storageView, constraints(0, 1, gridHeight=4, weightY = 1,
        fill=GridBagPanel.Fill.Both))

//    add(Button("View settings") { println("View settings") },
//      constraints(2, 6, fill=GridBagPanel.Fill.Horizontal))
  }
}

object MainWindow {
  def apply() = {
    val ui = new MainWindow()
    ui.visible = true
    ui
  }
}
