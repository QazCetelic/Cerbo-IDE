package qaz.code.model

import qaz.code.model.Analyzer.emptyLoops
import qaz.code.model.Analyzer.findEmptyLoops
import qaz.code.model.Analyzer.isBalanced
import kotlin.properties.Delegates

class Execution(private val profile: Profile) {
    // Normalized properties
    private var pointer = 0
    private val memory: ByteArray = ByteArray(profile.size)
    val operationsCounter: MutableMap<Operator, Int> by Delegates.observable(mutableMapOf()) { _, _, new ->
        if (loopingOperations >= profile.maximumOperations) {
            throw ExecutionException("Infinite loop? Maximum of ${profile.maximumOperations} looping operations exceeded")
        }
    }

    // Derived properties
    val operations: Int
        get() = operationsCounter.values.sum()
    private val loopingOperations: Int
        get() = operationsCounter
            .entries
            .filter { entry -> entry.key in Operator.LOOP_OPERATORS || entry.key in Operator.MOVE_OPERATORS }
            .sumOf { entry -> entry.value }
    /**
     * @return A clone of the memory array
     */
    val memoryCloned: ByteArray
        get() = memory.clone()
    /**
     * @return The last index of the memory array that is not 0
     */
    val lastFilledIndex: Int
        get() {
            // TODO Keep track of this using a variable to prevent having to loop through the whole array every time
            var lastIndex = memory.lastIndex
            while (lastIndex > 0 && memory[lastIndex] == 0.toByte()) {
                lastIndex--
            }
            return lastIndex
        }
    /**
     * @return The amount of bytes in memory that aren't 0
     */
    val amountNotEmpty: Int
        get() = memory.count { it != 0.toByte() }

    // Methods
    fun interpret(s: String, input: MutableList<Char>): Result {
        // TODO split into multiple functions
        val start = System.currentTimeMillis()
        try {
            if (!isBalanced(s)) throw ExecutionException("Brackets are not balanced")
            if (emptyLoops(s).any()) {
                throw ExecutionException("Empty loops found at character ${findEmptyLoops(s).joinToString(separator = ", ")}")
            }

            val output = mutableListOf<Line>()
            var lineOutput = mutableListOf<Char>()
            fun nextLine() {
                output.add(Line(lineOutput, pointer))
                lineOutput = mutableListOf()
            }

            var c = 0
            var i = 0
            while (i < s.length) {

                // > moves the pointer to the right
                if (s[i] == '>') {
                    incrementOperationCounter(Operator.MOVE_RIGHT)
                    if (pointer == memory.lastIndex) {
                        pointer = 0
                    }
                    else {
                        pointer++
                    }
                }
                else if (s[i] == '<') {
                    incrementOperationCounter(Operator.MOVE_LEFT)
                    if (pointer == 0) {
                        // Wraps to right
                        pointer = memory.lastIndex
                    }
                    else {
                        pointer--
                    }
                }
                else if (s[i] == '+') {
                    incrementOperationCounter(Operator.INCREMENT)
                    memory[pointer]++
                }
                else if (s[i] == '-') {
                    incrementOperationCounter(Operator.DECREMENT)
                    memory[pointer]--
                }
                else if (s[i] == '.') {
                    incrementOperationCounter(Operator.OUTPUT)
                    val character = Char(memory[pointer].toUShort())
                    lineOutput.add(character)
                }
                else if (s[i] == '\n') {
                    nextLine()
                }
                else if (s[i] == ',') {
                    incrementOperationCounter(Operator.INPUT)
                    if (input.isNotEmpty()) {
                        memory[pointer] = input.removeAt(0).code.toByte()
                    }
                    else {
                        throw ExecutionException("No input available")
                    }
                }
                else if (s[i] == '[') {
                    incrementOperationCounter(Operator.LOOP_START)
                    if (memory[pointer] == 0.toByte()) {
                        incrementOperationCounter(Operator.LOOP_START)
                        i++
                        while (c > 0 || s[i] != ']') {
                            if (s[i] == '[') {
                                c++
                            }
                            else if (s[i] == ']') {
                                c--
                            }
                            i++
                        }
                    }
                }
                else if (s[i] == ']') {
                    incrementOperationCounter(Operator.LOOP_END)
                    if (memory[pointer] != 0.toByte()) {
                        incrementOperationCounter(Operator.LOOP_END)
                        i--
                        while (c > 0 || s[i] != '[') {
                            if (s[i] == ']') {
                                incrementOperationCounter(Operator.LOOP_END)
                                c++
                            }
                            else if (s[i] == '[') {
                                incrementOperationCounter(Operator.LOOP_START)
                                c--
                            }
                            else {
                                incrementOperationCounter(Operator.LOOP_START)
                                // TODO remove
                            }
                            i--
                        }
                        i--
                    }
                }
                i++
            }
            if (lineOutput.isNotEmpty()) {
                nextLine()
            }
            val end = System.currentTimeMillis()

            return Result.Succes(output, end - start, this)
        }
        catch (e: ExecutionException) {
            System.err.println(e.message)
            val end = System.currentTimeMillis()
            return Result.Failure(e.message!!, end - start, this)
        }
    }

    private fun incrementOperationCounter(operator: Operator) {
        operationsCounter[operator] = (operationsCounter[operator] ?: 0) + 1
    }

    // Data classes
    data class Profile(val size: Int, val maximumOperations: Int) {
        companion object {
            // TODO create a profile using settings pane and use it for the execution
            val DEFAULT = Profile(32_000, 512_000)
        }
    }
    data class Line(val output: List<Char>, val pointer: Int)
}