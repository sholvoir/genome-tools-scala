package micit.tools

import java.io.File

import micit.generic.{Iterators, Options, TableFile}

/**
  * Reference file merge.
  * Created by sovar on 7/13/16.
  * Usage: scala micit.tools.RefMerge -i refFileName1,rsidColumnName,positionColumnName:refFileName2,rsidColumnName,positionColumnName... -o outFileName
  */
object RefMerge {
  def main(args: Array[String]): Unit = {
    if (args.length == 0)
      println("Usage: scala micit.tools.RefMerge -i refFileName1,rsidColumnName,positionColumnName:refFileName2,rsidColumnName,positionColumnName... -o outFileName")
    else {
      val conf = Options.parse(args)
      val refconfs = conf("-i").split(":").map(_.split(","))
      var iids = Seq.empty[String]
      val refits = refconfs.map { refconf =>
        TableFile.read(new File(refconf(0))) { header =>
          val cols = header.split("\\s")
          val colm = cols.zipWithIndex.toMap
          val rsidi = colm(refconf(1))
          val posi = colm(refconf(2))
          val iidsn = cols.filter(_.startsWith("NA"))
          val iidsis = iidsn.map(colm(_))
          iids ++:= iidsn
          line => {
            val row = line.split("\\s")
            (row(rsidi), row(posi).toInt, iidsis.map(row(_)(0)))
          }
        }
      }
      val snps = Iterators.zip(refits).map(p => (p.head._1, p.head._2, p.foldLeft[Seq[Char]](Nil)(_ ++: _._3)))
      val outfile = new File(conf("-o"))
      val header = s"${refconfs(0)(1)}\t${refconfs(0)(2)}\t" + iids.mkString("\t")
      TableFile.write(outfile)(header)(snps)(snp => "%s\t%d\t%s".format(snp._1, snp._2, snp._3.mkString("\t")))
    }
  }
}
