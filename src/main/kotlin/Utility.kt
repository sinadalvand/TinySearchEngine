import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.analysis.core.WhitespaceAnalyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search.similarities.LMDirichletSimilarity
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity
import org.apache.lucene.search.similarities.Similarity
import java.util.*

/**
 * print operation time on lambda
 * @param msg String
 * @param lambda Function0<Unit>
 */
fun timmy(msg: String = "Mission done in", lambda: () -> Unit) {
    val start = Date()
    lambda()
    val end = Date()
    val OpTime = end.time - start.time
    println("$msg $OpTime milliseconds")
}


fun getSimilarity(similarity: SIMILARITY): Similarity? {
    return when (similarity) {
        SIMILARITY.DEFAULT -> null
        SIMILARITY.S_JM -> LMJelinekMercerSimilarity(0.5f)
        SIMILARITY.S_DR -> LMDirichletSimilarity()
        SIMILARITY.TFIDF -> TFIDFSimi()
        SIMILARITY.CUSTOM -> TFIDF2()

    }
}

fun getAnalyzer(analyzer: ANALYZER): Analyzer {
    return when (analyzer) {
        ANALYZER.STOP -> StandardAnalyzer()
        ANALYZER.NON_STOP -> SimpleAnalyzer()
        ANALYZER.STEMMING -> WhitespaceAnalyzer()
        ANALYZER.NON_STEMMING -> EnglishAnalyzer()
    }
}


