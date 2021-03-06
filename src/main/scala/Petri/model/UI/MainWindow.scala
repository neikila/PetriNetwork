package Petri.model.UI

import java.io.File

import Petri.model.XML.{XMLComplex, XMLModel, XMLView}
import Petri.model.Model

import scala.concurrent.{ExecutionContext, Future}
import scala.swing._
import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by neikila.
  */
class MainWindow (model: Model) extends MainFrame {

  title = "Petri net"

  preferredSize = new Dimension(640, 480)

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


    val petriView = new PetriNetCanvas(model, None)
    val textField = new TextField { columns = 32 }
    val saveFileBtn: Button = Button("Save project") {
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
          XMLComplex.saveProject(fileChooser.selectedFile, petriView)
        } onComplete {
          case Success(_) =>
            println(s"Filename = ${fileChooser.selectedFile.getCanonicalPath}")
            repaint()
          case Failure(e) => println(e.getLocalizedMessage)
        }
      }
    }

    val openFileBtn: Button = Button("Open project") {
      val fileChooser = new FileChooser()
      if (new File(XMLComplex.defaultProjectsDirectory).exists())
        fileChooser.peer.setCurrentDirectory(new File(XMLComplex.defaultProjectsDirectory))
      else
        fileChooser.peer.setCurrentDirectory(new File("."))
      fileChooser.showOpenDialog(grid)
      if (fileChooser.selectedFile != null) {
        val filename = fileChooser.selectedFile.getName
        textField.text = filename
        Future {
          XMLComplex.openProject(fileChooser.selectedFile, petriView)
          petriView.model.enableActTransaction()
        } onComplete {
          case Success(_) =>
            println(s"Filename = ${fileChooser.selectedFile.getCanonicalPath}")
            repaint()
          case Failure(error) =>
            println("A error has occured: " + error.getMessage)
        }
      }
    }

    add(Button("Next by priority") {
      Future {
        petriView.model.nextByPriority()
        petriView.model.enableActTransaction()
      } onComplete {
        case Success(_) =>
          petriView.update()
        case Failure(error) =>
          println("A error has occured: " + error.getMessage)
      }
    }, constraints(2, 0))
    add(saveFileBtn, constraints(2, 1, fill = GridBagPanel.Fill.Horizontal))
    add(openFileBtn, constraints(2, 2, fill = GridBagPanel.Fill.Horizontal))
    add(textField, constraints(0, 0, weightX=1.0, fill=GridBagPanel.Fill.Horizontal))
    add(petriView, constraints(0, 1, gridHeight=3, weightY = 1,
        fill=GridBagPanel.Fill.Both))
    add(Button("Close") { sys.exit(0) },
      constraints(0, 4, gridWidth=3, fill=GridBagPanel.Fill.Horizontal))
  }
}

object MainWindow {
  def apply() = {
    val ui = new MainWindow(new Model)
    ui.visible = true
    ui
  }
}
