package Petri.model.XML

import java.io.{File, FileOutputStream}

import Petri.model.UI.PetriNetCanvas
import Petri.model.Model

import scala.xml.XML

/**
  * Created by Kirill on 03.04.2016.
  */
object XMLComplex {

  def saveProject(file: File, petriView: PetriNetCanvas) = {
    val prettyPrinter = new scala.xml.PrettyPrinter(80, 2)
    val cleanName = file.getName.split("\\.")(0)
    val modelFile = cleanName + "Model.xml"
    val viewFile = cleanName + "View.xml"
    val prettyXml = prettyPrinter.format({
      <complex>
        <model>{modelFile}</model>
        <view>{viewFile}</view>
      </complex>
    })
    file.createNewFile()
    util.Try({
      val out = new FileOutputStream(file)
      out.write(prettyXml.getBytes)
      out.close()
    })

    new XMLView(petriView).save(new File(file.getParentFile, viewFile))
    new XMLModel(new File(file.getParentFile, modelFile)).save(petriView.model)
  }

  def openProject(file: File, petriView: PetriNetCanvas) = {
    val xml = XML.loadFile(file)
    val parser = new XMLModel(new File(file.getParentFile, (xml \\ "Petri/model").text))

    val petriNet = new Model(
      parser.places,
      parser.transactions.sortBy(_.priority).reverse,
      parser.arcs
    )
    petriNet.printState()

    petriView.initView(petriNet, new File(file.getParentFile, (xml \\ "view").text))
  }

  val defaultProjectsDirectory = "projects"
}
