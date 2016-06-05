package UIDiplom.dialogs

import java.awt

import storageModel.Storage
import storageModel.storageDetails.{Rack, Section}

import scala.swing.BorderPanel.Position._
import scala.swing._

/**
  * Created by Neikila on 05.06.2016.
  */

class ModelSettingsDialog extends Dialog {

  val experimentAmount = new TextField
  val threadsAmount = new TextField
  val deadline = new TextField
  val consoleOutput = new TextField
  val debugMessageOn = new TextField

  title = "Modelling settings"
  modal = true

  contents = new BorderPanel {
    layout(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5,5,5,5)

      contents += new Label("Experiment amount")
      contents += experimentAmount
      contents += new Label("Threads using")
      contents += threadsAmount
      contents += new Label("Deadline")
      contents += deadline
      contents += new Label("Console output")
      contents += consoleOutput
      contents += new Label("Show debug message")
      contents += debugMessageOn
    }) = Center

    preferredSize = new Dimension(300, 250)

    layout(new FlowPanel(FlowPanel.Alignment.Right)(
      Button("Create") {
        if (isCorrect) {
          close()
        } else {
          Dialog.showMessage(this, "Wrong data", "Wrong data", Dialog.Message.Error)
        }
      }
    )) = South
  }

  def isCorrect: Boolean = {
    true
  }// here comes you login logic

  pack()
  centerOnScreen()
  open()
}
