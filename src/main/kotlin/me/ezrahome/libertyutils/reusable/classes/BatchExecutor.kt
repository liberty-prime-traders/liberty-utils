package me.ezrahome.libertyutils.reusable.classes

import java.util.function.Function

object BatchExecutor{

    fun <K,T> findAllBySomeKeyIn(keys: Collection<K>, fetcher: Function<Collection<K>, List<T>>): List<T> {
        return keys.chunked(1000).flatMap { fetcher.apply(it) }
    }
}
