package micit.tools

/**
  * Created by sovar on 11/6/16.
  */
object Help {
  def columnSplit() = println(
    """NAME
     |        ColumnSplit
     |
     |SYNOPSIS
     |        java micit.tools.ColumnSplit [options]
     |
     |DESCRIPTION
     |        Split a big TableFile to some TableFile in according to Column
     |
     |OPTIONS
     |        --help
     |            show this help info
     |        -r read_delimiter
     |            read_delimiter: input file column delimiter, default value is TAB
     |        -w write_delimiter
     |            write_delimiter: output file column delimiter, default value is TAB
     |        -i infile
     |            infile: input Table File
     |        -o outfile1,columnM[,columnN][,...][:outfile2,columnP[,columnQ][,...]][:...]
     |            outfileX: output Table File
     |            columnY: column name in the input Table File
     |
     |EXAMPLE
     |        java micit.tools.ColumnSplit --help
     |        java micit.tools.ColumnSplit -i infile -o outfile1,column1,column4:outfile2,column2,column4""".stripMargin)

  def insertN() = println(
    """NAME
     |        InertN
     |
     |SYNOPSIS
     |        java micit.tools.InsertN [options]
     |        or insertn
     |
     |DESCRIPTION
     |        Insert N at the missing position
     |
     |OPTIONS
     |        --help
     |            display this help information
     |        -delimiter delimiter
     |            global column delimiter, default is "\s" means a space or a tab
     |        -rsid rsid
     |            rsid: rsid column name, default is "rsID"
     |        -pcn pcn
     |            global position column name, default is "phys_position", you can specify for each ref file and input file
     |        -l legendfile[,delimiter=delimiter][,rsid=rsid][,pcn=pcn]
     |            legendfile: legend file name
     |            delimiter: column delimiter default is global delimiter
     |        -i filename[,delimiter=delimiter][,rsid=rsid][,pcn=pcn]
     |            filename: input file name
     |            delimiter: column delimiter default is global delimiter
     |            rsid: rsid column name, default is global rsid
     |            pcn: position column name, default is global pcn
     |        -o filename[,delimiter=delimiter][,rsid=rsid][,pcn=pcn]
     |            filename: output file name, default is suffix a "_WithN" at the input file name.
     |            delimiter: column delimiter default is global delimiter
     |            rsid: rsid column name, default is global rsid
     |            pcn: position column name, default is global pcn
     |        -insert N
     |            N default is "N"
     |
     |EXAMPLE
     |        insertn --help
     |        insertn -pcn position_b36 -r refHaplo.txt -i genotype.txt -insert NN""".stripMargin)

  def geno2Ped() = println(
    """NAME
     |        Geno2Ped
     |
     |SYNOPSIS
     |        scala micit.tools.Geno2Ped [options]
     |        geno2ped [options]
     |
     |DESCRIPTION
     |        Transform the genotype file to Plink(Ped/Map) file
     |
     |OPTIONS
     |        --help
     |            show this help info
     |        -chr n
     |            Chromesome number
     |        -delimiter delimiter
     |            Column delimter, default is "\s"
     |        -rsid rsid
     |            rsid column name, default is "rsID"
     |        -pcn pcn
     |            position column name, default is "phys_position"
     |        -g filename
     |            input genotype file name
     |        -m genetic_map
     |            genetic map file name
     |        -o prefix
     |            output ped/map file name, default is replace the genotype file extend name with "ped" and "map"
     |
     |EXAMPLE
     |        geno2ped --help
     |        geno2ped -chr 1 -pcn position_b36 -g genotype.txt""".stripMargin)

  def ref2HapLS() = println(
    """NAME
     |        Reference2HapLS
     |
     |SYNOPSIS
     |        scala micit.tools.Ref2HapLS [options]
     |
     |DESCRIPTION
     |        Transform reference haplo file to Hap/Legend/Sample file
     |
     |OPTIONS
     |        --help
     |            show this help info
     |        -delimiter delimiter
     |            delimiter: global column delimiter, default is "\s" means a space or a tab
     |        -rsid rsid
     |            rsid: global rsid column name, default is "rsID"
     |        -pcn pcn
     |            pcn: global position column name, default is "phys_position", you can specify for each ref file and input file
     |        -suffix-a strand_A
     |            strand_A: 列名后缀, 默认值"_A"
     |        -suffix-b strand_B
     |            strand_B: 列名后缀, 默认值"_B"
     |        -m genetic_map[,delimiter=delimiter]
     |            genetic_map: genetic map file
     |            delimiter: 列分隔符, 未指定时使用全局delimiter
     |        -r pop=population,grp=group,file=filename[,delimiter=delimiter][,rsid=rsid][,pcn=pcn][:...]
     |            Refer, 参考库信息, 可设置多个参考库, 用":"分隔, 每个参考库提供下列信息, 其中id和file是必需的
     |            population: pupulation
     |            group: group
     |            filename: filename
     |            delimiter: 列分隔符, 未指定时使用全局delimiter
     |            rsid: rsid列名, default is global rsid
     |            pcn: position列名, 未指定时使用全局pcn
     |        -o filename[,delimiter=delimiter]
     |            filename: output Hap/Legend/Sample file name, the extend name will be "haplotypes", "legend" and "sample"
     |            delimiter: output file delimiter, default is space
     |
     |EXAMPLE
     |        scala micit.tools.Ref2HapLS -pcn position_b36 -r pop=JPT,grp=ASN,file=chr1_jpt_chb.phased:pop=YRI,grp=AFR,file=chr1_yri.phased -o unmatched
     |
    """.stripMargin)

  def table2Vcf() = println(
    """NAME
     |        Table2Vcf
     |
     |SYNOPSIS
     |        scala micit.tools.Table2Vcf [options]
     |
     |DESCRIPTION
     |        Transform the table format genotype haplotype and reference to VCF 4.3 format
     |
     |OPTIONS
     |        -read-delimiter grl
     |            grl: 全局读文件列分隔符, 默认为"\s", 表示单个空格或制表符
     |        -write-delimiter
     |            gwd: 全局写文件列分隔符, 默认为"\t"(制表符)
     |        -pcn pcn
     |            pcn: 全局position列名, 若未指定则需要为每个文件单独指定, 默认值position
     |        -r id=id,file=file[,delimiter=delimiter][,rsid=rsid][,pcn=pcn][:...]
     |            Refer, 参考库信息, 可设置多个参考库, 用":"分隔, 每个参考库提供下列信息, 其中id和file是必需的
     |            id: 库标识, 不可使用保留字"all", all表示使用全部库
     |            file: 文件名
     |            delimiter: 列分隔符, 未指定时使用全局readdelimiter
     |            rsid: rsid列名, 为且只为多个refer中的一个指定rsid列名
     |            pcn: position列名, 未指定时使用全局pcn
     |        -g filename[,delimiter=delimiter][,pcn=pcn][:...]
     |            Genotype文件, 可配置多个Genotype文件, 用":"分隔
     |            filename: 文件名
     |            delimiter: 列分隔符, 未指定时使用全局readdelimiter
     |            pcn: position列名, 未指定时使用全局pcn
     |        -h filename[,delimiter=delimiter][,pcn=pcn][:...]
     |            Haplotype文件, 可配置多个Haplotype文件, 用":"分隔
     |            filename: 文件名
     |            *delimiter: 列分隔符, 未指定时使用全局readdelimiter
     |            *pcn: position列名, 未指定时使用全局pcn
     |
     |EXAMPLE
     |
    """.stripMargin)

  def SamSplit() = println(
    """NAME
     |        SamSplit
     |
     |SYNOPSIS
     |        java micit.tools.SamSplit [options][ dir]
     |
     |DESCRIPTION
     |        According Chromesome Name Split a Sam File
     |
     |OPTIONS
     |        --help
     |            show this help info
     |        dir
     |            input and output sam file directory, default is current direcory
     |
     |EXAMPLE
     |        java micit.tools.SamSplit
     |        java micit.tools.SamSplit --help
     |        java micit.tools.SamSplit sam""".stripMargin)

  def VcfSplit() = println(
    """NAME
     |        VcfSplit
     |
     |SYNOPSIS
     |        java micit.tools.VcfSplit [options]
     |
     |DESCRIPTION
     |        split a multi sample vcf file to two or more vcf files each of which contains one or more samples.
     |
     |OPTIONS
     |        --help
     |            show this help info
     |        -i infile
     |            infile: input vcf file
     |        -o outfile1,sample1[,sampleN][,...][:outfile2,sampleP[,sampleQ][,...]][:...]
     |            outfileX: output vcf file
     |            sampleY: column name in the input Table File
     |
     |EXAMPLE
     |        java micit.tools.VcfSplit -i data -o hg00096.vcf,HG00096:Ref.vcf,HG00097,HG00098""".stripMargin)
}
