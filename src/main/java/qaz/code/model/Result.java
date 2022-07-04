package qaz.code.model;

import java.util.List;

public abstract class Result {
    public final long duration;
    public final Execution execution;
    
    public Result(long duration, Execution execution) {
        this.duration = duration;
        this.execution = execution;
    }
    
    public static class Succes extends Result {
        public final List<List<Character>> output;
        public Succes(List<List<Character>> output, long duration, Execution execution) {
            super(duration, execution);
            this.output = output;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (List<Character> characters : output) {
                for (Character character : characters) {
                    sb.append(character);
                }
            }
            return sb.toString();
        }
    }
    
    public static class Failure extends Result {
        public final String error;
        public Failure(String error, long duration, Execution execution) {
            super(duration, execution);
            this.error = error;
        }

        @Override
        public String toString() {
            return error;
        }
    }
}
