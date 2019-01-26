package micit.tools

import java.io.{File, PrintWriter}

import micit.generic.TableFile

/**
  * Created by sovar on 11/4/16.
  * Usage: scala micit.tools.Haplo2Geno haploFileName
  */
object Haplo2Geno {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("scala micit.tools.Haplo2Geno haploFileName")
    } else {
      val haplo = args(0)
      val temp = haplo.split("\\.")
      val geno = new PrintWriter(if (temp.length > 1) {
        temp(temp.length - 1) = "geno"
        temp.mkString(".")
      } else haplo + ".geno")
      var header = ""
      val haploit = TableFile.read(new File(haplo)) { h =>
        header = h
        line => line.split("\\s")
      }
      val suffixa = "_A"
      val suffixb = "_B"
      val columns = header.split("\\s")
      val columnm = columns.zipWithIndex.toMap
      val temp1 = columns.groupBy(_.startsWith("NA"))
      try {
        geno.print(temp1(false).mkString("\t"))
        geno.print("\t")
        val others = temp1(false).map(columnm(_))
        val iids = temp1(true).map(_.replace(suffixa, "").replace(suffixb, "")).distinct
        val iidis = iids.map(x => (columnm(x + suffixa), columnm(x + suffixb)))
        geno.println(iids.mkString("\t"))
        for (row <- haploit) {
          geno.print(others.map(row(_)).mkString("\t"))
          geno.print("\t")
          geno.println(iidis.map(x => row(x._1) + row(x._2)).mkString("\t"))
        }
      } finally geno.close()
    }
  }
}
