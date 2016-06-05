package UIDiplom.dialogs

import storageModel.storageDetails.{Rack, Section}

import swing._
import scala.swing.BorderPanel.Position._
import java.awt

import storageModel.Storage

/**
  * Created by Neikila on 05.06.2016.
  */

class CreateRackDialog extends Dialog {
  var rack: Option[Rack] = None

  val rackSize = new TextField
  val maxWeight = new TextField
  val levels = new TextField
  val direction = new TextField

  title = "Создание стеллажа"
  modal = true

  contents = new BorderPanel {
    layout(new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(5,5,5,5)

      contents += new Label("Размер")
      contents += rackSize
      contents += new Label("Предельный вес")
      contents += maxWeight
      contents += new Label("Количество уровней")
      contents += levels
      contents += new Label("Направление доступа")
      contents += direction
    }) = Center

    preferredSize = new Dimension(200, 200)

    layout(new FlowPanel(FlowPanel.Alignment.Right)(
      Button("Применить") {
        if (isCorrect) {
          val tmp = rackSize.text.split(Array(',', ';', ' ', ':'))
          rack = if (direction.text.length != 0) {
            val possibleDirection = Section.Direction.valueOf(direction.text)
            Some(new Rack(new awt.Point(0, 0), new awt.Point(tmp(0).toInt, tmp(1).toInt),
              levels.text.toInt, maxWeight.text.toInt, Storage.box, possibleDirection))
          }
          else
            Some(new Rack(new awt.Point(0, 0), new awt.Point(tmp(0).toInt, tmp(1).toInt),
              levels.text.toInt, maxWeight.text.toInt, Storage.box))
          println(rack.get)
          close()
        } else {
          Dialog.showMessage(this, "Wrong data", "Wrong data", Dialog.Message.Error)
        }
      }
    )) = South
  }

  def isCorrect: Boolean = {
    if (direction.text.length == 0)
      true
    else {
      try {
        val possibleDirection = Section.Direction.valueOf(direction.text)
        true
      } catch {
        case e : Exception => false
      }
    }
  }// here comes you login logic

  centerOnScreen()
  open()
}
