package micit.tools

import java.io.{File, PrintWriter}

import micit.generic.{Iterators, Options, TableFile, Generic}
import micit.genome.GenomeFunc._

/**
  * Created by sovar on 11/6/16.
  * -pcn position_b36 -m genetic_map_chr1_b36.txt -r pop=CEU,grp=EUR,file=all/chr1_ceu.phased,pcn=phys_position:pop=JPT,grp=ASN,file=all/chr1_jpt_chb.phased:pop=YRI,grp=AFR,file=all/chr1_yri.phased -o all/all
  * -m genetic_map_chr1_b36.txt -r pop=CEU,grp=EUR,file=matched/chr1_ceu.phased:pop=YRI,grp=AFR,file=matched/chr1_yri.phased,pcn=position_b36 -o matched/matched
  * -pcn position_b36 -m genetic_map_chr1_b36.txt -r pop=CEU,grp=EUR,pcn=phys_position,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_ceu.phased:pop=CHD,grp=ASN,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_chd.unr.phased:pop=GIH,grp=USA,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_gih.unr.phased:pop=JPT,grp=ASN,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_jpt_chb.unr.phased:pop=LWK,grp=AFR,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_lwk.unr.phased:pop=MEX,grp=AMR,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_mex.phased:pop=MKK,grp=MKK,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_mkk.phased:pop=TSI,grp=EUR,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_tsi.unr.phased:pop=YRI,grp=AFR,file=Max/hapmap3_r2_b36_fwd.consensus.qc.poly.chr1_yri.phased -o Max/max
  * -pcn position_b36 -m genetic_map_chr1_b36.txt -r pop=JPT,grp=ASN,file=unmatched/chr1_jpt_chb.phased:pop=YRI,grp=AFR,file=unmatched/chr1_yri.phased -o unmatched/unmatched
  */
object Ref2HapLS {
  case class Refx(pop: String, grp: String, file: File, delimiter: String, rsid: String, pcn: String) {
    var columnm: Map[String, Int] = _
    var iter: Iterator[(Int, (String, Array[Char]))] = _
    var iids: Array[String] = _
  }
  case class Legend(rsid: String, pos: Int, a0: Char, a1: Char)

  def main(args: Array[String]): Unit = {
    val options = Options.parse(args)
    if (options.isEmpty || options.contains("--help")) Help.ref2HapLS() else {
      val delimiter = options.getOrElse("-delimiter", "\\s")
      val rsid = options.getOrElse("-rsid", "rsID")
      val pcn = options.getOrElse("-pcn", "phys_position")
      val suffixa = options.getOrElse("-suffix-a", "_A")
      val suffixb = options.getOrElse("-suffix-b", "_B")
      val mopt = Options.parse(options("-m").split(',').map(_.split('=')))
      var mheader = ""
      val gmap = TableFile.read(new File(mopt(""))) { header =>
        mheader = header
        val mdlmt = mopt.getOrElse("delimiter", delimiter)
        line => line.split(mdlmt)(0).toInt -> line
      }
      val refs = options("-r").split(":").map { o =>
        val ropt = Options.parse(o.split(',').map(_.split('=')))
        Refx(ropt("pop"), ropt("grp"), new File(ropt("file")), ropt.getOrElse("delimiter", delimiter),
          ropt.getOrElse("rsid", rsid), ropt.getOrElse("pcn", pcn))
      }
      val outopt = Options.parse(options("-o").split(',').map(_.split('=')))
      val outfilename = outopt("")
      val t = outopt.getOrElse("delimiter", " ")
      val hapfile = new PrintWriter(outfilename + ".haplotypes")
      val legfile = new PrintWriter(outfilename + ".legend")
      val samfile = new PrintWriter(outfilename + ".sample")
      val mapfile = new PrintWriter(outfilename + ".txt")

      try {
        samfile.println(Seq("sample", "population", "group", "sex").mkString(t))
        refs.foreach { refx =>
          refx.iter = TableFile.read(refx.file) { h =>
            val columns = h.split(delimiter)
            refx.columnm = columns.zipWithIndex.toMap
            refx.iids = columns.filter(_.startsWith("NA")).map(_.replace(suffixa, "").replace(suffixb, "")).distinct
            refx.iids.foreach(iid => samfile.println(s"$iid$t${refx.pop}$t${refx.grp}${t}1"))
            val rsidi = refx.columnm(refx.rsid)
            val posi = refx.columnm(refx.pcn)
            val iidis = refx.iids.flatMap(iid => Seq(iid + suffixa, iid + suffixb)).map(refx.columnm(_))
            line => {
              val row = line.split(delimiter)
              row(posi).toInt -> (row(rsidi), iidis.map(row(_)(0)))
            }
          }
        }
      } finally samfile.close()

      try {
        mapfile.println(mheader)
        legfile.println(Seq("id", "position", "a0", "a1").mkString(t))
        for ((rows, map) <- Iterators.unionZip(Iterators.zip(refs.map(_.iter)))(gmap)(null)(null)(_.head._1 - _._1)) {
          if (rows != null && map != null) {
            val head = rows.head
            if (rows.exists(row => row._1 != head._1)) throw new Exception("refs not compatible!")
            val alleles = rows.flatMap(_._2._2)
            val x = Generic.countElement(alleles)
            if (x.size > 1) {
              val y = (x.head._1, x.tail.head._1)
              legfile.println(s"${head._2._1}$t${head._1}$t${y._1}$t${y._2}")
              hapfile.println(alleles.map(h => if (h == y._1) 0 else 1).mkString(t))
              mapfile.println(map._2)
            }
          }
        }
      } finally {
        hapfile.close()
        legfile.close()
        mapfile.close()
      }
    }
  }
}
