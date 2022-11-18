package qaz.code.model

import qaz.code.model.Analyzer.findEmptyLoops
import qaz.code.model.Analyzer.isBalanced
import qaz.code.model.Result.Succes
import java.util.stream.Collectors

class Execution(profile: Profile) {
    private var pointer = 0
    private val size: Int
    private val maximumOperations: Int

    var operationsMoveLeft = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsMoveRight = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsIncrease = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsDecrease = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsLeftLoop = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsRightLoop = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsInput = 0
        private set(value) {
            field = value
            checkOperations()
        }
    var operationsOutput = 0
        private set(value) {
            field = value
            checkOperations()
        }
    val operations: Int
        get() = operationsMoveLeft + operationsMoveRight + operationsIncrease + operationsDecrease + operationsLeftLoop + operationsRightLoop + operationsInput + operationsOutput
    private val loopOperations: Int
        get() = operationsLeftLoop + operationsRightLoop + operationsMoveLeft + operationsMoveRight
    private val memory: ByteArray

    /**
     * @return A clone of the memory array
     */
    fun getMemory(): ByteArray {
        return memory.clone()
    }

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

    init {
        size = profile.size
        memory = ByteArray(size)
        maximumOperations = profile.maximumOperations
    }

    @Throws(ExecutionException::class)
    private fun checkOperations() {
        if (loopOperations >= maximumOperations) {
            throw ExecutionException("Infinite loop? Maximum of $maximumOperations looping operations exceeded")
        }
    }

    fun interpret(s: String, input: List<Char>): Result {
        val start = System.currentTimeMillis()
        return try {
            if (!isBalanced(s)) throw ExecutionException("Brackets are not balanced")
            val emptyLoops = findEmptyLoops(s)
            if (emptyLoops.isNotEmpty()) {
                throw ExecutionException("Empty loops found at character ${emptyLoops.joinToString(separator = ", ")}")
            }
            var c = 0
            val output = mutableListOf<List<Char>>()
            var lineOutput = mutableListOf<Char>()
            var i = 0
            while (i < s.length) {

                // > moves the pointer to the right
                if (s[i] == '>') {
                    operationsMoveRight++
                    if (pointer == size - 1) {
                        pointer = 0
                    }
                    else {
                        pointer++
                    }
                }
                else if (s[i] == '<') {
                    operationsMoveLeft++
                    if (pointer == 0) {
                        // Wraps to right
                        pointer = size - 1
                    }
                    else {
                        pointer--
                    }
                }
                else if (s[i] == '+') {
                    operationsIncrease++
                    memory[pointer]++
                }
                else if (s[i] == '-') {
                    operationsDecrease++
                    memory[pointer]--
                }
                else if (s[i] == '.') {
                    operationsOutput++
                    val character = Char(memory[pointer].toUShort())
                    lineOutput.add(character)
                }
                else if (s[i] == '\n') {
                    output.add(lineOutput)
                    lineOutput = ArrayList()
                }
                else if (s[i] == ',') {
                    operationsInput++
                    if (input.size > 0) {
                        memory[pointer] = input.first().toChar().code.toByte()
                        input.drop(1)
                    }
                    else {
                        throw ExecutionException("No input available")
                    }
                }
                else if (s[i] == '[') {
                    operationsLeftLoop++
                    if (memory[pointer].toInt() == 0) {
                        operationsLeftLoop++
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
                    operationsRightLoop++
                    if (memory[pointer].toInt() != 0) {
                        operationsRightLoop++
                        i--
                        while (c > 0 || s[i] != '[') {
                            if (s[i] == ']') {
                                operationsRightLoop++
                                c++
                            }
                            else if (s[i] == '[') {
                                operationsLeftLoop++
                                c--
                            }
                            else {
                                operationsLeftLoop++ // TODO remove
                            }
                            i--
                        }
                        i--
                    }
                }
                i++
            }
            if (lineOutput.isNotEmpty()) {
                output.add(lineOutput)
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

    // TODO create a profile using settings pane and use it for the execution
    class Profile(val size: Int, val maximumOperations: Int) {
        companion object {
            val DEFAULT = Profile(30000, 512000)
        }
    }

}