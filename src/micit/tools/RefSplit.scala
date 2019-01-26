package micit.tools

import java.io.File
import micit.generic.{Options, TableFile}

/**
  * in according to one column to split a ref file.
  * -i inputfile -o output directory -n columnIndex
  * Created by sovar on 8/1/16.
  */
object RefSplit {
  def main(args: Array[String]) {
    val usage = "scala micit.tools.RefSplit -i ref.txt -o ref -n 0"
    val options = Options.parse(args)
    val infile = new File(options.getOrElse("-i", "ref.txt"))
    val outdir = new File(options.getOrElse("-o", "ref"))
    val index = options.getOrElse("-n", "0").toInt
    if (!outdir.exists()) outdir.mkdirs()
    TableFile.rowSplit(infile)(outdir)(line => line.split("\t")(index))
  }
}
