package qaz.code.model

enum class Operator(val char: Char) {
    INCREMENT('+'),
    DECREMENT('-'),
    MOVE_RIGHT('<'),
    MOVE_LEFT('>'),
    OUTPUT('.'),
    INPUT(','),
    LOOP_START('['),
    LOOP_END(']');

    companion object {
        val OPERATORS = values().map { it.char }.toSet()
        val LOOP_OPERATORS = setOf(LOOP_START, LOOP_END)
        val MOVE_OPERATORS = setOf(MOVE_LEFT, MOVE_RIGHT)
    }
}