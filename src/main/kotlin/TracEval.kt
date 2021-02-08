import uk.ac.gla.terrier.jtreceval.trec_eval
import java.io.*


class TracEval {

    fun trecEval(results: String) = timmy("Trec Eval Done in") {
        val te = trec_eval()
        val output = te.runAndGetOutput(arrayOf("-m", "map", "-m", "ndcg", "-m", "P.5,10", Const.newqrelPath, results))
        saveEvalResult(output, generateEvalOutputName(results))
    }

    private fun saveEvalResult(result: Array<Array<String>>, outputName: String) {
        val output = Const.outputPath + outputName
        val ostream = FileOutputStream(output)
        val writer = PrintWriter(ostream)

        writer.println("measure | query | value")
        writer.println("=======================")
        writer.flush()

        for (line in result) {
            writer.println("${line[0]} | ${line[1]} | ${line[2]}")
            writer.flush()
        }

        ostream.close()
        writer.close()
    }

    private fun generateEvalOutputName(fileName: String): String {
        return fileName.substringAfterLast(File.separator).substringBeforeLast(".").plus("-eval.txt")
    }

    companion object {
        fun qrelCorrector(): String {
            val fstream = FileInputStream(Const.qrelPath)
            val ostream = FileOutputStream(Const.newqrelPath)
            val writer = PrintWriter(ostream)

            val br = BufferedReader(InputStreamReader(fstream))
            var strLine: String?
            while (br.readLine().also { strLine = it } != null) {
                val first = strLine?.split(" ")?.get(0)

                if (first.isNullOrBlank()) {
                    throw IllegalStateException("Malform")
                }

                val result: String = strLine?.replaceFirst(first, "$first 0") ?: ""
                writer.println(result)
                writer.flush()
            }
            ostream.flush()
            ostream.close()
            fstream.close()
            return Const.newqrelPath
        }
    }
}