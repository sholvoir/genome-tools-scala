package micit.tools

import java.io.File

import micit.generic.TableFile

import scala.io.{Source, StdIn}

/**
  * Created by sovar on 9/14/16.
  */
object Temp2 {
  def main(args: Array[String]): Unit = {
    val std = TableFile.read(new File("standardhaplotype.txt")) { header =>
      val columns = header.split("\\s").zipWithIndex.toMap
      val pcni = columns("phys_position")
      val iidis = Seq("NA11919_A","NA11919_B").map(columns(_))
      line => {
        val row = line.trim.split("\\s")
        val pos = row(pcni).toInt
        (pos, iidis.map(row(_)(0)).mkString(""))
      }
    }.toMap

    var total = 0
    var err = 0
    for (line <- Source.stdin.getLines()) {
      val row = line.trim.split("\\s")
      val pos = row(4).toInt
      val s = std(pos)
      val allele = row(9)
      total += 1
      if (s != allele) err += 1
    }

    println(s"$err / $total")
  }
}
