package qaz.code.model

import java.util.regex.MatchResult
import java.util.regex.Pattern
import kotlin.streams.asSequence

object Analyzer {
    @JvmStatic
    fun isBalanced(str: String): Boolean {
        var count = 0
        for (i in str.indices) {
            if (str[i] == Operator.LOOP_START.char) {
                count++
            }
            else if (str[i] == Operator.LOOP_END.char) {
                count--
            }
            if (count < 0) {
                return false
            }
        }
        return count == 0
    }

    @JvmStatic
    /**
     * Will return an amount when the loops are unbalanced
     * The fact that it can be executed regardless of validity is a choice
     * @return The amount of loops, will be inaccurate if the code is not balanced
     */
    fun countApproximateAmountOfLoops(str: String): Int {
        var count = 0
        var depth = 0
        for (i in str.indices) {
            if (str[i] == Operator.LOOP_START.char) {
                depth++
            }
            else if (str[i] == Operator.LOOP_END.char) {
                if (depth > 0) {
                    depth--
                    count++
                }
            }
        }
        return count
    }


    private val EMPTY_LOOP_PATTERN = Pattern.compile("\\[[^]\\[.,><+-]*]")

    /**
     * Finds any loop that doesn't contain any operators.
     * @param code The code that potentially contains empty loops.
     * @return All the starting indexes of the empty loops.
     */
    @JvmStatic
    fun findEmptyLoops(code: String): List<Int> = emptyLoops(code).toList()

    /**
     * Finds any loop that doesn't contain any operators incrementally.
     * @param code The code that potentially contains empty loops.
     * @return An iterator that returns all the starting indexes of the empty loops.
     */
    @JvmStatic
    fun emptyLoops(code: String): Sequence<Int> = EMPTY_LOOP_PATTERN
        .matcher(code)
        .results()
        .map(MatchResult::start)
        .asSequence()
}