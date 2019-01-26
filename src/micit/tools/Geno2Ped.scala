package micit.tools

import java.io.{File, PrintWriter}

import micit.generic.{Options, TableFile}

import scala.collection.mutable.ListBuffer
/**
  * Created by sovar on 11/4/16.
  */
object Geno2Ped {
  def main(args: Array[String]): Unit = {
    val options = Options.parse(args)
    if (options.isEmpty || options.contains("--help")) Help.geno2Ped() else {
      val chr = options("-chr")
      val delimiter = options.getOrElse("-delimiter", "\\s")
      val rsid = options.getOrElse("-rsid", "rsID")
      val pcn = options.getOrElse("-pcn", "phys_position")
      val genofilename = options("-g")
      val gmapfilename = options("-m")
      val optoutfilename = {
        val p = genofilename.split("\\.")
        if (p.length > 1) p.init.mkString(".") else genofilename
      }
      val outfilename = options.getOrElse("-o", optoutfilename)

      val mapfile = new PrintWriter(outfilename + ".map")
      val pedfile = new PrintWriter(outfilename + ".ped")

      val gmapit = TableFile.read(new File(gmapfilename)) { h =>
        line => {
          val row = line.split(delimiter)
          (row(0).toInt, row(2))
        }
      }

      var header = Seq.empty[String]
      val positions = ListBuffer.empty[Int]
      var columnm = Map.empty[String, Int]
      var rsidi = 0

      val genoit = TableFile.read(new File(genofilename)){ h =>
        header = h.split(delimiter)
        columnm = header.zipWithIndex.toMap
        rsidi = columnm(rsid)
        val posi = columnm.apply(pcn)
        line => {
          val row = line.split(delimiter)
          val pos = row(posi).toInt
          positions.append(pos)
          pos -> row
        }
      }

      var geno = Map.empty[Int, Array[String]]
      try geno = (for ((g, m) <- genoit.zip(gmapit)) yield {
        if (g._1 != m._1) throw new Exception("not align")
        else mapfile.println(s"$chr\t${g._2(rsidi)}\t${m._2}\t${m._1}")
        g
      }).toMap finally mapfile.close()

      try header.filter(_.startsWith("NA")).foreach { iid =>
        val iidi = columnm(iid)
        pedfile.print(s"$iid $iid 0 0 1 0")
        for (pos <- positions) {
          pedfile.print(" ")
          pedfile.print(geno(pos)(iidi).mkString(" "))
        }
        pedfile.println()
      } finally pedfile.close()
    }
  }
}
