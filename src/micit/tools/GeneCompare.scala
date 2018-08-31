package micit.tools

import java.io.File

import micit.generic.TableFile

/**
  * Created by sovar on 9/9/16.
  * Usage: scala micit.tools.GeneCompare file1,pcn,columnName1,columnName2... file2,pcn,columnName1,comlumnName2...
  */
object GeneCompare {
  def main(args: Array[String]) {
    try {
      val arg1 = args(0).split(",")
      val arg2 = args(1).split(",")
      val file1 = TableFile.read(new File(arg1(0))) { header =>
        val columns = header.split("\\s").zipWithIndex.toMap
        val pcni = columns(arg1(1))
        val coli = arg1.tail.tail.map(columns(_))
        line => {
          val rows = line.split("\\s")
          (rows(pcni).toInt, coli.map(rows(_)).mkString(""))
        }
      }
      val file2 = TableFile.read(new File(arg2(0))) { header =>
        val columns = header.split("\\s").zipWithIndex.toMap
        val pcni = columns(arg2(1))
        val coli = arg2.tail.tail.map(columns(_))
        line => {
          val rows = line.split("\\s")
          (rows(pcni).toInt, coli.map(rows(_)).mkString(""))
        }
      }
      var err = 0
      for (((pos1, s1), (pos2, s2)) <- file1.zipAll(file2, "", "")) {
        if (s1 != s2) {
          err += 1
          println(s"$pos1 $s1 $s2")
        }
      }
      println(s"Error: $err")
    } catch { case _: Throwable => Console.err.println("Usage: scala micit.tools.GeneCompare file1,pcn,columnName1,columnName2... file2,pcn,columnName1,comlumnName2...")}
  }
}
