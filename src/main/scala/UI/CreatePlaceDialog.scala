package UI

import swing._
import scala.swing.BorderPanel.Position._

/**
  * Created by Kirill on 04.04.2016.
  */

class CreatePlaceDialog(val create: (Int) => Boolean) extends Dialog {
  val counter = new TextField
  counter.text = "0"

  preferredSize = new Dimension(200, 120)

  title = "Place"
  modal = true

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

    add(new Label("Counts") {border=Swing.EtchedBorder(Swing.Lowered) },
      constraints(0, 0))
    add(counter, constraints(2, 0, gridWidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(Button("Create") {
      if (create(util.Try(counter.text.toInt).getOrElse(0))) {
        close()
      }
    }, constraints(1, 4, gridWidth=2, fill=GridBagPanel.Fill.Horizontal))
    //        Dialog.showMessage(this, "Wrong username or password!", "Login Error", Dialog.Message.Error)
  }
}
