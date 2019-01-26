package micit.tools
import java.io.PrintWriter

import scala.io.Source

/**
  * Created by sovar on 9/6/16.
  */
object Temp1 {
  def main(args: Array[String]): Unit = {
    val rx = """^\d+\.\s+([\w' -\.]+)\[.*""".r
    for (i <- 3 to 5) {
      val out = new PrintWriter(s"Collins$i")
      var index = 0
      try {
        for (line <- Source.fromFile(s"Collins$i.txt").getLines()) {
          index += 1
          line match {
            case rx(word) => out.println(word)
            case _ => println(s"$index error")
          }
        }
      } finally out.close()
    }
  }
}
