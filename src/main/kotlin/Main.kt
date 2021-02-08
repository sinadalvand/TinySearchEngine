import java.util.*

val indexer = Indexer()
val searcher = Searcher()
val tracEval = TracEval()

fun main(args: Array<String>) {

    // try to fix cranqrel file (add 0 to second column)
    fixCranQrelFile()

    val scanner = Scanner(System.`in`)
    while (true) {
        // get analyzer mode
        val analyzer = analyzerMenu(scanner)

        // get similarity mode
        val similarity = similarityMenu(scanner)

        // apply inputs and make result
        doJob(analyzer, similarity)
    }
}


fun doJob(analyzer: ANALYZER, similarity: SIMILARITY) = timmy("Total Done in") {

    // indexing operations
    indexer.index(analyzer, similarity)

    // search and rank operations
    searcher.search(analyzer, similarity).apply {
        tracEval.trecEval(this)
    }
}


fun analyzerMenu(scanner: Scanner): ANALYZER {
    println("\n\n< ================ Analyzer Menu ================ >\n1.StopWords\n2.non-StopWords\n3.Stemming\n4.non-Stemming \n \nwhich one?")
    return when (scanner.nextInt()) {
        1 -> ANALYZER.STOP
        2 -> ANALYZER.NON_STOP
        3 -> ANALYZER.STEMMING
        4 -> ANALYZER.NON_STEMMING
        else -> ANALYZER.STOP
    }

}


fun similarityMenu(scanner: Scanner): SIMILARITY {
    println("\n< ================ Similarity Menu ================ > \n1.Default\n2.Smoothing (JM)\n3.Smoothing (Drishlet)\n4.TFIDF\n5.Custom-TFIDF  \n \nwhich one?")
    return when (scanner.nextInt()) {
        1 -> SIMILARITY.DEFAULT
        2 -> SIMILARITY.S_JM
        3 -> SIMILARITY.S_DR
        4 -> SIMILARITY.TFIDF
        5 ->SIMILARITY.CUSTOM
        else -> SIMILARITY.DEFAULT
    }
}

fun fixCranQrelFile() {
    TracEval.qrelCorrector()
}


enum class ANALYZER(val type: String) {
    STOP("StopWord"), NON_STOP("NoneStopWord"), STEMMING("Stemming"), NON_STEMMING("NoneStemming")
}


enum class SIMILARITY(val type: String) {
    DEFAULT("Default"), S_JM("Smoothing(JM)"), S_DR("Smoothing(Drishlet)"), TFIDF("TFIDF"),CUSTOM("New TFIDF ")
}
