package micit.tools

import java.io.{File, PrintWriter}

import micit.generic.{Iterators, TableFile}

/**
  * Created by sovar on 7/13/16.
  * Snp file merge in according to position.
  */
object SnpMerge {
  private def getConfig(arg: String) = {
    val args = arg.split(",")
    val file = new File(args(0))
    if (args.length > 1) (file, args(0), args(1)) else (file, "rsID", "position")
  }

  private def getSnpIt(arg: String) = {
    val (infile, rcn, pcn) = getConfig(arg)
    var iids = Seq.empty[String]
    val snps = TableFile.read(infile) { header =>
      val cols = header.split("\\s")
      val colm = cols.zipWithIndex.toMap
      iids = cols.filter(_.startsWith("NA"))
      val rsidi = colm(rcn)
      val posi = colm(pcn)
      val iidsis = iids.map(colm(_))
      line => {
        val row = line.split("\\s")
        (row(rsidi), row(posi).toInt, iidsis.map(row(_)))
      }
    }
    var dsnp = "N"
    for (_ <- 0 until iids.length - 1) dsnp += "\tN"
    (iids.mkString("\t"), snps, dsnp)
  }

  def main(args: Array[String]) {
    val usage = "scala micit.tools.SnpMerge infile1[,rsidColumnName,positionColumnName] infile2[,rsidColumnName,positionColumnName] outfile[,rsidColumnName,positionColumnName]"
    if (args.length < 3) println(usage) else {
      val (outfile, rsidcn3, poscn3) = getConfig(args(2))
      val (iids1, snps1, dsnp1) = getSnpIt(args(0))
      val (iids2, snps2, dsnp2) = getSnpIt(args(1))
      val out = new PrintWriter(outfile)
      try {
        out.println(s"$rsidcn3\t$poscn3\t$iids1\t$iids2")
        for ((snp1, snp2) <- Iterators.unionZip(snps1)(snps2)(null)(null)(_._2 - _._2)) {
          if (snp1 != null && snp2 != null)
            out.println(s"${snp1._1}\t${snp1._2}\t${snp1._3.mkString("\t")}\t${snp2._3.mkString("\t")}")
          else if (snp1 != null) out.println(s"${snp1._1}\t${snp1._2}\t${snp1._3.mkString("\t")}\t$dsnp2")
          else if (snp2 != null) out.println(s"${snp2._1}\t${snp2._2}\t$dsnp1\t${snp2._3.mkString("\t")}")
        }
      } finally out.close()
    }
  }
}
