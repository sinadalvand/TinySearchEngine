import java.io.File
import java.nio.file.Paths

object Const {

    private val basePath = Paths.get("").toAbsolutePath().toString() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator

    val documentPath = basePath + "sources" + File.separator + "cran.all.1400"
    val queriesPath = basePath + "sources" + File.separator + "cran.qry"
    val qrelPath = basePath + "sources" + File.separator + "cranqrel"
    val newqrelPath = basePath + "output" + File.separator + "cranqrel"


    val indexPath = basePath + "indexes"+ File.separator
    val outputPath = basePath + "output" + File.separator

}