import org.apache.commons.io.FileUtils
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.store.FSDirectory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * class responsible to do indexing and generate index output
 */
class Indexer {

    private val indexPath = Const.indexPath
    private val docPath = Const.documentPath

    /**
     * index all file in directory
     * @param indexPath String
     */
    fun index(analyzer: ANALYZER, similarity: SIMILARITY) = timmy("Indexing Done in") {
        try {
            val iwc = IndexWriterConfig(getAnalyzer(analyzer)).apply {
                openMode = OpenMode.CREATE
                getSimilarity(similarity)?.let {
                    this.similarity = it
                }
            }

            // delete old data in index Dir
            FileUtils.cleanDirectory(Paths.get(indexPath).toFile());

            IndexWriter(FSDirectory.open(Paths.get(indexPath)), iwc).apply {
                indexDocument(docPath, this)
                forceMerge(1)
                close()
            }
        } catch (e: Exception) {
            println("caught a ${e.javaClass} with message: ${e.message}")
        }
    }

    // indexing content of input file
    private fun indexDocument(filePath: String, writer: IndexWriter) {
        Files.newInputStream(Path.of(filePath)).use { stream ->
            val buffer = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8))
            var id = ""
            var title = ""
            var author = ""
            var bib = ""
            var w = ""
            var state: String? = ""
            var first = true
            var line: String? = buffer.readLine()
            while (line != null) {
                when (line.substring(0, 2)) {
                    ".I" -> {
                        if (!first) {
                            val d: Document = makeDocument(id, title, author, bib, w)
                            writer.addDocument(d)
                        } else {
                            first = false
                        }
                        title = ""
                        author = ""
                        bib = ""
                        w = ""
                        id = line.substring(3, line.length)
                    }
                    ".T", ".A", ".B", ".W" -> state = line
                    else -> when (state) {
                        ".T" -> title += "$line "
                        ".A" -> author += "$line "
                        ".B" -> bib += "$line "
                        ".W" -> w += "$line "
                    }
                }
                line = buffer.readLine()
            }
            val d: Document = makeDocument(id, title, author, bib, w)
            writer.addDocument(d)
        }
    }

    // save indexes into document
    private fun makeDocument(id: String, title: String, author: String, bib: String, w: String): Document =
        Document().apply {
            this.add(StringField("id", id, Field.Store.YES))
            this.add(StringField("path", id, Field.Store.YES))
            this.add(TextField("title", title, Field.Store.YES))
            this.add(TextField("author", author, Field.Store.YES))
            this.add(TextField("bib", bib, Field.Store.YES))
            this.add(TextField("w", w, Field.Store.YES))
        }
}