package dev.ag6.mclauncher.task

import dev.ag6.mclauncher.task.Task.State
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class TaskExecutor(maxConcurrent: Int = 4) : AutoCloseable {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val semaphore = Semaphore(maxConcurrent)

    fun <T> submit(task: Task<T>): Deferred<T> {
        return scope.async {
            semaphore.withPermit {
                task.stateProperty.set(State.RUNNING)

                try {
                    task.execute().also {
                        task.stateProperty.set(State.COMPLETED)
                    }
                } catch (e: Exception) {
                    task.stateProperty.set(State.FAILED)
                    throw e
                }
            }
        }
    }

    override fun close() {
        scope.cancel()
    }
}