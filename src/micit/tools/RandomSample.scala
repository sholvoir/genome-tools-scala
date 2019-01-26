package micit.tools

import java.io.File
import micit.generic.TableFile

/**
  * random sample a table file
  * Created by sovar on 7/13/16.
  */
object RandomSample {
  def main(args: Array[String]): Unit = {
    val usage = "Usage: scala micit.tools.RandomSample infile outfile remainRate"
    if (args.length < 3) println(usage) else {
      val infile = new File(args(0))
      val outfile = new File(args(1))
      val percent = args(2).toDouble
      TableFile.conditionFilter(infile)(outfile)(_ => _ => math.random < percent)
    }
  }
}
