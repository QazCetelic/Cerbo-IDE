package qaz.code.model

import java.util.regex.Pattern

object Analyzer {
    @JvmStatic
    fun isBalanced(str: String): Boolean {
        var count = 0
        for (i in str.indices) {
            if (str[i] == Operations.LOOP_START.char) {
                count++
            } else if (str[i] == Operations.LOOP_END.char) {
                count--
            }
            if (count < 0) {
                return false
            }
        }
        return count == 0
    }


    private val EMPTY_LOOP_PATTERN = Pattern.compile("\\[[^]\\[.,><+-]*]")

    /**
     * Finds any loop that doesn't contain any operators.
     * @param code The code that potentially contains empty loops.
     * @return All the starting indexes of the empty loops.
     */
    @JvmStatic
    fun findEmptyLoops(code: String): List<Int> {
        val matcher = EMPTY_LOOP_PATTERN.matcher(code)
        val indexes: MutableList<Int> = ArrayList()
        while (matcher.find()) {
            indexes.add(matcher.start())
        }
        return indexes
    }
}