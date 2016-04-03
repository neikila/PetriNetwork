package UI

import java.io.File

import XML.XMLView
import _root_.model.Model

import scala.concurrent.{Future, ExecutionContext}
import scala.swing._
import scala._
import Swing._

import ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * Created by neikila.
  */
class MainWindow (var model: Model) extends MainFrame {

  title = "Petri net"
  val petriView = new PetriNetCanvas(model, Some(new File("out", "test.xml")))

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

    add(new Label("Label @ (0,0)") {border=Swing.EtchedBorder(Swing.Lowered) },
      constraints(0, 0, gridHeight=2, fill=GridBagPanel.Fill.Both))
    add(Button("Next by priority") {
      val f = Future {
        model.nextByPriority(true)
      }

      f onComplete {
        case Success(_) => petriView.update(); println("Next finished")
        case Failure(error) => println("A error has occured: " + error.getMessage)
      }
      println(new XMLView(petriView).xmlView.toString())
    }, constraints(2, 0))

    val textField = new TextField { columns = 32 }

    val openFileBtn: Button = Button("Open file") {
      val fileChooser = new FileChooser()
      fileChooser.peer.setCurrentDirectory(new File("out"))
      fileChooser.showSaveDialog(grid)
      if (fileChooser.selectedFile != null) {
        val filename = fileChooser.selectedFile.getName
        textField.text = filename
        println(s"Filename = ${fileChooser.selectedFile.getCanonicalPath}")
        new XMLView(petriView).save(fileChooser.selectedFile)
        repaint()
      }
    }

    add(openFileBtn, constraints(2, 1, fill = GridBagPanel.Fill.Horizontal))
    add(textField, constraints(1, 0, weightX=1.0, fill=GridBagPanel.Fill.Horizontal))
    add(petriView,
      constraints(1, 1, gridHeight=3, weightY = 1,
        fill=GridBagPanel.Fill.Both))
    add(Button("Close") { sys.exit(0) },
      constraints(0, 4, gridWidth=3, fill=GridBagPanel.Fill.Horizontal))
  }
}

object MainWindow {
  def apply(model: Model) = {
    val ui = new MainWindow(model)
    ui.visible = true
    ui
  }
}
