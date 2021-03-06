import org.apache.lucene.search.similarities.TFIDFSimilarity
import org.apache.lucene.util.BytesRef
import kotlin.math.ln

class TFIDFSimi : TFIDFSimilarity() {

    override fun tf(freq: Float): Float = ln(( freq).toDouble()).toFloat()+1.toFloat()

    override fun idf(docFreq: Long, docCount: Long): Float
    {
        var a=ln((docFreq / (docCount + 1)).toDouble()).toFloat()
        if (a>0) return a else return 0.toFloat()
    }

    override fun lengthNorm(length: Int): Float = 1f

    override fun sloppyFreq(distance: Int): Float = 1f

    override fun scorePayload(doc: Int, start: Int, end: Int, payload: BytesRef?): Float = 1f
}