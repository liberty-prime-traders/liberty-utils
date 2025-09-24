package me.ezrahome.libertyutils.reusable

object BatchExecutor {
    private const val DEFAULT_BATCH_SIZE = 1000

    fun <K, ENTITY> findAllByKeyIn(keys: Set<K>, fetcher: (Set<K>) -> List<ENTITY>): List<ENTITY> {
        if (keys.isEmpty()) return emptyList()
        val list = ArrayList(keys)
        val result = ArrayList<ENTITY>()
        var i = 0
        while (i < list.size) {
            val end = minOf(i + DEFAULT_BATCH_SIZE, list.size)
            val batch = list.subList(i, end).toSet()
            result.addAll(fetcher(batch))
            i = end
        }
        return result
    }

    fun <K> applyToAllByIdIn(ids: Set<K>, consumer: (Set<K>) -> Unit) {
        if (ids.isEmpty()) return
        val list = ArrayList(ids)
        var i = 0
        while (i < list.size) {
            val end = minOf(i + DEFAULT_BATCH_SIZE, list.size)
            val batch = list.subList(i, end).toSet()
            consumer(batch)
            i = end
        }
    }
}
