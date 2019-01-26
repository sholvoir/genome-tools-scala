package micit.tools

import java.io.File

import micit.generic.TableFile
import micit.genome.GenomeFunc._
import scala.collection.mutable

/**
  * Created by sovar on 9/16/16.
  */
object Temp3 {
  def main(args: Array[String]): Unit = {
    val mafs = TableFile.read(new File("refHaplos.txt")) { header =>
      val columns = header.split("\\s")
      val columnm = columns.zipWithIndex.toMap
      val pcni = columnm("phys_position")
      val iidis = columns.filter(_.startsWith("NA")).map(columnm(_))
      line => {
        val row = line.trim.split("\\s")
        val pos = row(pcni).toInt
        val allele = iidis.map(row(_)(0)).mkString("")
        val temp1 = mutable.Map.empty[Char, Int]
        for (haplo <- allele) temp1(haplo) = temp1.getOrElse(haplo, 0) + 1
        val temp = temp1.toSeq.sortBy(-_._2)
        val maf = temp.length match {
          case 0 => 0
          case 1 => 0
          case _ => temp.tail.head._2.toDouble / allele.length
        }
        (pos, maf)
      }
    }.toMap

    val haplo = TableFile.read(new File("haplotype.txt")) { header =>
      line => {
        val row = line.trim.split("\\s")
        (row(1).toInt, row(2)(0))
      }
    }.toMap

    val geno = TableFile.read(new File("genotype.txt")) { header =>
      line => {
        val row = line.trim.split("\\s")
        (row(1).toInt, (row(2)(0), row(2)(1)))
      }
    }.toMap

    val p1 = mafs.keySet -- haplo.keySet
    val p2 = geno.filter(p => isHomo(p._2))
    val p3 = p1 -- p2.keySet
    val ss = p3.filter(mafs(_) < 0.49)

    TableFile.rowSplit(new File("refHaplos.txt"))(new File("../hifi2")) { line =>
      val pos = line.trim.split("\\s")(1).toInt
      if (!ss.contains(pos)) "refHaplos.txt" else "other1.txt"
    }

    TableFile.rowSplit(new File("genotype.txt"))(new File("../hifi2")) { line =>
      val pos = line.trim.split("\\s")(1).toInt
      if (!ss.contains(pos)) "genotype.txt" else "other2.txt"
    }

    TableFile.rowSplit(new File("haplotype.txt"))(new File("../hifi2")) { line =>
      val pos = line.trim.split("\\s")(1).toInt
      if (!ss.contains(pos)) "haplotype.txt" else "other3.txt"
    }

    TableFile.rowSplit(new File("standardhaplotype.txt"))(new File("../hifi2")) { line =>
      val pos = line.trim.split("\\s")(1).toInt
      if (!ss.contains(pos)) "standardhaplotype.txt" else "other4.txt"
    }
  }
}
