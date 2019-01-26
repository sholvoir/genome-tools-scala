package micit.tools

import java.io.File

import micit.generic.TableFile
import micit.genome.GenomeFunc._

/**
  * Created by sovar on 9/12/16.
  * Usage: scala micit.tools.PlotGenome file pcn columnName1 columnName2
  */
object PlotGenome {
  def main(args: Array[String]) {
    try {
      val points = TableFile.read(new File(args(0))) { header =>
        val columns = header.split("\\s").zipWithIndex.toMap
        val posi = columns(args(1))
        val coli1 = columns(args(2))
        val coli2 = columns(args(3))
        line => {
          val rows = line.split("\\s")
          val pos = rows(posi).toInt
          val allele = (rows(coli1)(0), rows(coli2)(0))
          val show = if (isnx(allele)) 1 else if (isHete(allele)) 0 else 0
          (pos, show)
        }
      }
      //XYBarChart(points).show()
    } catch { case _: Throwable => Console.err.println("Usage: scala micit.tools.PlotGenome file,pcn,columnName1,columnName2")}
  }
}
