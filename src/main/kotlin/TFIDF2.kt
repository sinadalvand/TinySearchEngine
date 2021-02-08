import org.apache.lucene.search.similarities.TFIDFSimilarity
import org.apache.lucene.util.BytesRef
import kotlin.math.ln


class TFIDF2 : TFIDFSimilarity() {
    override fun tf(freq: Float): Float = ln(( freq).toDouble()).toFloat()+1.toFloat()

    override fun idf(docFreq: Long, docCount: Long): Float
    {
        var a=ln(1.toFloat()+(docCount-docFreq+0.5)/(docFreq+0.5))
        if (a>0) return a.toFloat() else return 0.toFloat()
    }

    override fun lengthNorm(length: Int): Float = 1f

    override fun sloppyFreq(distance: Int): Float = 1f/distance.toFloat()

    override fun scorePayload(doc: Int, start: Int, end: Int, payload: BytesRef?): Float = 1f
}