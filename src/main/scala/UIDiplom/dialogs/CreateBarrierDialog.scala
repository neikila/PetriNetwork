package UIDiplom.dialogs

import java.awt

import storageModel.Storage
import storageModel.storageDetails.{Barrier, Rack, Section}

import scala.swing.BorderPanel.Position._
import scala.swing._

/**
  * Created by Neikila on 05.06.2016.
  */

class CreateBarrierDialog extends Dialog {
  var barrier: Option[Barrier] = None

  val barrierSize = new TextField

  title = "Создание преграды"
  modal = true

  contents = new BorderPanel {
    layout(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5,5,5,5)

      contents += new Label("Size")
      contents += barrierSize
    }) = Center

    preferredSize = new Dimension(200, 85)

    layout(new FlowPanel(FlowPanel.Alignment.Right)(
      Button("Применить") {
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

  centerOnScreen()
  open()
}
