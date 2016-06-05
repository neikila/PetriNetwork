package UIDiplom.dialogs

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

  title = "Настройки модели"
  modal = true

  contents = new BorderPanel {
    layout(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5,5,5,5)

      contents += new Label("Количество экспериментов")
      contents += experimentAmount
      contents += new Label("Количество потоков")
      contents += threadsAmount
      contents += new Label("Время моделирования")
      contents += deadline
      contents += new Label("Вывод в консоль")
      contents += consoleOutput
      contents += new Label("Показ отладочных сообщений")
      contents += debugMessageOn
    }) = Center

    preferredSize = new Dimension(300, 250)

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

  pack()
  centerOnScreen()
  open()
}
