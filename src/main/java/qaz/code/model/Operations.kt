package qaz.code.model

enum class Operations(val char: Char) {
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
    }
}