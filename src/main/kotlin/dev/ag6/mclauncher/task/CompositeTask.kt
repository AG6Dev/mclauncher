package dev.ag6.mclauncher.task

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CompositeTask(override val name: String, private val parallel: Boolean, private val tasks: List<Task<*>>) :
    Task<List<Any?>> {
    override suspend fun execute(): List<Any?> {
        return if (parallel) {
            coroutineScope {
                tasks.map { task ->
                    async {
                        task.execute()
                    }
                }.map { it.await() }
            }
        } else {
            val results = mutableListOf<Any?>()
            tasks.forEachIndexed { index, task ->
                progressProperty.set(index.toFloat() / tasks.size)
                results.add(task.execute())
            }
            results
        }
    }
}