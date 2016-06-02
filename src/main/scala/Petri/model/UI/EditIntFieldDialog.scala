package Petri.model.UI

import swing._

/**
  * Created by Kirill on 04.04.2016.
  */

class EditIntFieldDialog(val edit: (Int) => Boolean, buttonText: String = "Create",
                         fieldLabel: String = "Counts", titleCustom: String = "Place", defaultValue: Int = 0) extends Dialog {
  val counter = new TextField
  counter.text = defaultValue.toString

  preferredSize = new Dimension(200, 120)

  title = titleCustom
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

    add(new Label(fieldLabel) {border=Swing.EtchedBorder(Swing.Lowered) },
      constraints(0, 0, gridWidth = 2))
    add(counter, constraints(2, 0, gridWidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(Button(buttonText) {
      if (edit(util.Try(counter.text.toInt).getOrElse(0))) {
        close()
      }
    }, constraints(2, 4, fill=GridBagPanel.Fill.Horizontal))
    add(Button("Cancel") { close() }, constraints(1, 4, fill=GridBagPanel.Fill.Horizontal))
    //        Dialog.showMessage(this, "Wrong username or password!", "Login Error", Dialog.Message.Error)
  }
}
