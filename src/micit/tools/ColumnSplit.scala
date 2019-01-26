package micit.tools

import micit.generic._
import java.io.File

/**
  * column splite a table file.
  * Created by sovar on 3/9/16.
  */
object ColumnSplit {
  def main(args: Array[String]): Unit = {
    val options = Options.parse(args)
    if (options.isEmpty || options.contains("--help")) Help.columnSplit() else {
      val readDelimiter = options.getOrElse("-r", "\t")
      val writeDelimiter = options.getOrElse("-w", "\t")
      val infile = new File(options("-i"))
      val outfiles = options("-o").split(":").map { arg =>
        val ar = arg.split(",")
        ar.head -> ar.tail.toIndexedSeq
      }.toMap
      TableFile.columnSplit(readDelimiter)(writeDelimiter)(infile)(outfiles)
    }
  }
}