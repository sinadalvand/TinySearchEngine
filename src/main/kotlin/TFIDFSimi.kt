import org.apache.lucene.search.similarities.TFIDFSimilarity
import org.apache.lucene.util.BytesRef
import kotlin.math.ln

class TFIDFSimi : TFIDFSimilarity() {

    override fun tf(freq: Float): Float = ln((1 + freq).toDouble()).toFloat()

    override fun idf(docFreq: Long, docCount: Long): Float = ln((docFreq / docCount).toDouble()).toFloat()

    override fun lengthNorm(length: Int): Float = 1f

    override fun sloppyFreq(distance: Int): Float = 1f

    override fun scorePayload(doc: Int, start: Int, end: Int, payload: BytesRef?): Float = 1f
}