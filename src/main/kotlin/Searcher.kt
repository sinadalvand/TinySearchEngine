import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.store.FSDirectory
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class Searcher {

    private val indexesPath = Const.indexPath
    private val output = Const.outputPath
    private val queriesPath = Const.queriesPath


    fun search(analyzer: ANALYZER, similarity: SIMILARITY): String {
        val outputName = outputNameGenerator(analyzer, similarity)
        timmy("Search done in") {
            val indexes: IndexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexesPath)))
            val outputter = PrintWriter(outputName, "UTF-8")
            val searcher = IndexSearcher(indexes).apply { getSimilarity(similarity)?.let { this.setSimilarity(it) } }
            val buffer = Files.newBufferedReader(Paths.get(queriesPath), StandardCharsets.UTF_8)
            val parser =
                MultiFieldQueryParser(arrayOf("title", "author", "bibliography", "content"), getAnalyzer(analyzer))


            var queryString = ""
            var queryNumber = 1
            var first = true
            var line: String? = buffer.readLine()

            while (line != null) {
                if (line.substring(0, 2) == ".I") {
                    if (!first) {
                        val query = parser.parse(QueryParser.escape(queryString))
                        makeQueryResult(searcher, outputter, queryNumber, query)
                        queryNumber++
                    } else {
                        first = false
                    }
                    queryString = ""
                } else {
                    queryString += " $line"
                }
                line = buffer.readLine()
            }

            val query = parser.parse(QueryParser.escape(queryString))
            makeQueryResult(searcher, outputter, queryNumber, query)

            outputter.close()
            indexes.close()
        }
        return outputName
    }

    private fun makeQueryResult(searcher: IndexSearcher, writer: PrintWriter, queryNumber: Int, query: Query?) {
        val results = searcher.search(query, 10)
        val hits = results.scoreDocs
        for (i in hits.indices) {
            val doc = searcher.doc(hits[i].doc)
            writer.println(queryNumber.toString() + " 0 " + doc["id"] + " " + i + " " + hits[i].score + " Dalvand-Roomiani")
        }
    }

    private fun outputNameGenerator(analyzer: ANALYZER, similarity: SIMILARITY): String {
        return output + analyzer.type + "-" + similarity.type + ".out"
    }


}