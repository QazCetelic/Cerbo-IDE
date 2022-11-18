package qaz.code.model

abstract class Result(val duration: Long, val execution: Execution) {
    class Succes(val output: List<List<Char>>, duration: Long, execution: Execution) : Result(duration, execution) {
        override fun toString(): String {
            val sb = StringBuilder()
            for (characters in output) {
                for (character in characters) {
                    sb.append(character)
                }
            }
            return sb.toString()
        }
    }

    class Failure(val error: String, duration: Long, execution: Execution) : Result(duration, execution) {
        override fun toString(): String {
            return error
        }
    }
}