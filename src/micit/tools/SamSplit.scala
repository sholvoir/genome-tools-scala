package micit.tools

import java.io.File

import micit.generic.Options
import micit.genome.{ChromosomeID, Sam}

/**
  * Split a sam file
  * -i input directory defaut value is "sam", -o output directory defaut value is "sam", -n rname column index usually is 2
  */
object SamSplit {
  def main(args: Array[String]) {
    val options = Options.parse(args)
    if (options.contains("--help")) Help.SamSplit() else {
      for (infile <- new File(options.getOrElse("", ".")).listFiles().filter { x => x.isFile && x.getName.endsWith(".sam") }) {
        println(s"Processing ${infile.getName}")
        Sam.fileSplit(infile)
      }
    }
  }
}