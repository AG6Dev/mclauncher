package dev.ag6.mclauncher.instance

class LoadInstanceResult(val instance: GameInstance?, val error: String?) {
    fun isSuccess(): Boolean {
        return instance != null && error.isNullOrEmpty()
    }

    companion object {
        fun success(instance: GameInstance): LoadInstanceResult {
            return LoadInstanceResult(instance, null)
        }

        fun failure(error: String): LoadInstanceResult {
            return LoadInstanceResult(null, error)
        }
    }
}