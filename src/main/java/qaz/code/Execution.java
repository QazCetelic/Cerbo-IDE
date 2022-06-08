package qaz.code;

import java.sql.Time;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class Execution {
    private int pointer;
    private final int size;
    private final byte[] memory;

    public byte[] getMemory() {
        return memory.clone();
    }

    public int getLastFilledIndex() {
        int lastIndex = memory.length - 1;
        while (lastIndex > 0 && memory[lastIndex] == 0) {
            lastIndex--;
        }
        return lastIndex;
    }

    public int getAmountNotEmpty() {
        int counter = 0;
        for (byte b : memory) {
            if (b != 0) {
                counter++;
            }
        }
        return counter;
    }

    public Execution(Profile profile) {
        this.size = profile.size;
        memory = new byte[this.size];
    }

    public Result interpret(String s, ArrayList<Character> input) {
        // Remove everything that isn't an instruction
        s = s.replaceAll("[^><\\[\\],.\n+-]", "");

        long start = System.currentTimeMillis();
        try {
            int c = 0;
            ArrayList<List<Character>> output = new ArrayList<>();
            ArrayList<Character> lineOutput = new ArrayList<>();

            for (int i = 0; i < s.length(); i++) {
                // > moves the pointer to the right
                if (s.charAt(i) == '>') {
                    if (pointer == size - 1) {
                        pointer = 0;
                    }
                    else {
                        pointer++;
                    }
                }
                // < moves the pointer to the left
                else if (s.charAt(i) == '<') {
                    if (pointer == 0) {
                        // Wraps to right
                        pointer = size - 1;
                    }
                    else {
                        pointer--;
                    }
                }
                // + increments the value of the memory cell under the pointer
                else if (s.charAt(i) == '+') {
                    memory[pointer]++;
                }
                // - decrements the value of the memory cell under the pointer
                else if (s.charAt(i) == '-') {
                    memory[pointer]--;
                }
                // . outputs the character signified by the cell at the pointer
                else if (s.charAt(i) == '.') {
                    char character = (char) (memory[pointer]);
                    if (character == '\n') {
                        output.add(lineOutput);
                        lineOutput = new ArrayList<>();
                    }
                    else {
                        lineOutput.add(character);
                    }
                }
                // , inputs a character and store it in the cell at the pointer
                else if (s.charAt(i) == ',') {
                    if (input.size() > 0) {
                        memory[pointer] = (byte) (input.remove(0).charValue());
                    }
                    else {
                        throw new IllegalStateException("No input available");
                    }
                }
                // [ jumps past the matching ] if the cell under the pointer is 0
                else if (s.charAt(i) == '[') {
                    if (memory[pointer] == 0) {
                        i++;
                        while (c > 0 || s.charAt(i) != ']') {
                            if (s.charAt(i) == '[') {
                                c++;
                            }
                            else if (s.charAt(i) == ']') {
                                c--;
                            }
                            i++;
                        }
                    }
                }
                // ] jumps back to the matching [ if the cell under the pointer is nonzero
                else if (s.charAt(i) == ']') {
                    if (memory[pointer] != 0) {
                        i--;
                        while (c > 0 || s.charAt(i) != '[') {
                            if (s.charAt(i) == ']') {
                                c++;
                            }
                            else if (s.charAt(i) == '[') {
                                c--;
                            }
                            i--;
                        }
                        i--;
                    }
                }
            }
            if (!lineOutput.isEmpty()) {
                output.add(lineOutput);
            }
            long end = System.currentTimeMillis();
            return new Succes(output, (end - start));
        }
        catch (Exception e) {
            long end = System.currentTimeMillis();
            return new Failure(e.getMessage(), (end - start));
        }
    }

    public static class Profile {
        final int size;
        public Profile(int size) {
            // TODO create a profile using settings pane and use it for the execution
            this.size = size;
        }

        public static final Profile DEFAULT = new Profile(30_000);
    }

    public abstract static class Result {
        public final long duration;
        public Result(long duration) {
            this.duration = duration;
        }
    }

    public static class Succes extends Result {
        public final List<List<Character>> output;
        public Succes(List<List<Character>> output, long duration) {
            super(duration);
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
        public Failure(String error, long duration) {
            super(duration);
            this.error = error;
        }

        @Override
        public String toString() {
            return error;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}