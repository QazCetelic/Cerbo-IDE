package qaz.code.model

class ExecutionManager {
    companion object {
        val INSTANCE = ExecutionManager()
        /**
         * Filters out comments, so they don't have to be processed.
         */
        val FILTER_COMMENTS = Regex("[^-+,.\\[\\]><\n]")
    }

    private var lastExecutionCodeHash = 0
    private var lastExecutionInputHash = 0
    private var executionThread: Thread? = null
    private var killThread: Thread? = null

    fun process(sheet: Sheet) {
        val codeWithComments = sheet.codeProperty.get()
        // Remove everything that isn't an instruction or newline
        val cleanedCode = codeWithComments
            .replace(FILTER_COMMENTS, "")
            .trim()

        val input = sheet.inputProperty.get().toList();

        // Only executes if the code or input has changed to prevent unnecessary executions
        if (cleanedCode.hashCode() != lastExecutionCodeHash || input.hashCode() != lastExecutionInputHash) {
            lastExecutionCodeHash = cleanedCode.hashCode()
            lastExecutionInputHash = input.hashCode()

            // Stopping old threads
            if (executionThread?.isAlive == true) executionThread!!.interrupt()
            if (killThread?.isAlive == true) killThread!!.interrupt()
            // Create new thread
            executionThread = Thread {
                // Delay 100ms
                runCatching { Thread.sleep(100) }
                // Create execution machine
                val execution = Execution(Execution.Profile.DEFAULT)
                // Execute code
                val result = execution.interpret(cleanedCode, input)
                sheet.lastResultProperty.set(result)
            }
            // And start it
            executionThread!!.start()

            // Creates parallel thread that kills execution thread if it takes too long
            killThread = Thread {
                runCatching { Thread.sleep(5000) }

                if (executionThread!!.isAlive) {
                    System.err.println("Execution is taking too long, aborting...")
                    executionThread!!.interrupt()
                }
            }
            killThread!!.start()
        }
    }
}