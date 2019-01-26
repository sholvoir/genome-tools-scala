package micit.tools

import java.io.{File, PrintWriter}

import micit.generic.{Iterators, Options, TableFile}

/**
  * Created by sovar on 11/6/16.
  */
object InsertN {
  def main(args: Array[String]): Unit = {
    val options = Options.parse(args)
    if (options.isEmpty || options.contains("--help")) Help.insertN() else {
      val delimiter = options.getOrElse("-delimiter", "\\s")
      val rsid = options.getOrElse("-rsid", "rsID")
      val pcn = options.getOrElse("-pcn", "phys_position")
      val insert = options.getOrElse("-insert", "N")
      val legopt = Options.parse(options("-r").split(',').map(_.split('=')))
      val inopt = Options.parse(options("-i").split(',').map(_.split('=')))
      val outopt = Options.parse(options.getOrElse("-o", "").split(',').map(_.split('=')))

      val legIt = TableFile.read(new File(legopt(""))) { h =>
        val ldelimiter = legopt.getOrElse("delimiter", delimiter)
        line => {
          val row = line.split(ldelimiter)
          row(1).toInt -> row(0)
        }
      }

      val idelimiter = inopt.getOrElse("delimiter", delimiter)
      val irsid = inopt.getOrElse("rsid", rsid)
      val ipcn = inopt.getOrElse("pcn", pcn)
      val infilename = inopt("")
      var icolumns = Seq.empty[String]
      val iit = TableFile.read(new File(infilename)) { h =>
        icolumns = h.split(idelimiter).toSeq
        val posi = h.split(idelimiter).zipWithIndex.toMap.apply(ipcn)
        line => {
          val row = line.split(idelimiter)
          row(posi).toInt -> row
        }
      }

      val odelimiter = outopt.getOrElse("delimiter", "\t")
      val optoutfilename = {
        val f = infilename.split("\\.")
        if (f.length > 1) {
          f(f.length - 2) = f(f.length - 2) + "_WithN"
          f.mkString(".")
        } else infilename + "_WithN"
      }
      val outfilename = outopt.getOrElse("", optoutfilename)
      val outfile = new PrintWriter(outfilename)

      try {
        outfile.println(icolumns.mkString(odelimiter))
        for ((inp, leg) <- Iterators.unionZip(iit)(legIt)(null)(null)(_._1 - _._1)) {
          if (leg == null) println(s"Not in ref\t${inp._1}")
          else if (inp == null) outfile.println(icolumns.map { col =>
            if (col == irsid) leg._2 else if (col == ipcn) leg._1.toString else insert
          }.mkString(odelimiter))
          else outfile.println(inp._2.mkString(odelimiter))
        }
      } finally outfile.close()
    }
  }
}
