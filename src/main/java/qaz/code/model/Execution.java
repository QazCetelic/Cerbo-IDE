package qaz.code.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Execution {
    private int pointer;
    private final int size;
    private final int maximumOperations;
    
    private int operationsMoveLeft;
    private int operationsMoveRight;
    private int operationsIncrease;
    private int operationsDecrease;
    private int operationsLeftLoop;
    private int operationsRightLoop;
    private int operationsInput;
    private int operationsOutput;
    
    public int getOperations() {
        return operationsMoveLeft + operationsMoveRight + operationsIncrease + operationsDecrease + operationsLeftLoop + operationsRightLoop + operationsInput + operationsOutput;
    }
    
    public int getOperationsLeftLoop() {
        return operationsLeftLoop;
    }
    
    public int getOperationsRightLoop() {
        return operationsRightLoop;
    }
    
    public int getOperationsInput() {
        return operationsInput;
    }
    
    public int getOperationsOutput() {
        return operationsOutput;
    }
    
    public int getOperationsMoveLeft() {
        return operationsMoveLeft;
    }
    
    public int getOperationsMoveRight() {
        return operationsMoveRight;
    }
    
    public int getOperationsIncrease() {
        return operationsIncrease;
    }
    
    public int getOperationsDecrease() {
        return operationsDecrease;
    }
    
    private int getLoopOperations() {
        return operationsLeftLoop + operationsRightLoop + operationsMoveLeft + operationsMoveRight;
    }
    
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
        this.maximumOperations = profile.maximumOperations;
    }

    private void checkOperations() throws ExecutionException {
        if (getLoopOperations() >= maximumOperations) {
            throw new ExecutionException("Infinite loop? Maximum of " + maximumOperations + " looping operations exceeded");
        }
    }

    public Result interpret(String s, List<Character> input) {
        long start = System.currentTimeMillis();
        try {
            if (!Analyzer.isBalanced(s)) throw new ExecutionException("Brackets are not balanced");
            List<Integer> emptyLoops = Analyzer.findEmptyLoops(s);
            if (!emptyLoops.isEmpty()) throw new ExecutionException("Empty loops found at character " + emptyLoops.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            
            int c = 0;
            ArrayList<List<Character>> output = new ArrayList<>();
            ArrayList<Character> lineOutput = new ArrayList<>();

            for (int i = 0; i < s.length(); i++) {
                // > moves the pointer to the right
                if (s.charAt(i) == '>') {
                    operationsMoveRight++;
                    if (pointer == (size - 1)) {
                        pointer = 0;
                        checkOperations();
                    }
                    else {
                        pointer++;
                    }
                }
                // < moves the pointer to the left
                else if (s.charAt(i) == '<') {
                    operationsMoveLeft++;
                    if (pointer == 0) {
                        // Wraps to right
                        pointer = size - 1;
                        checkOperations();
                    }
                    else {
                        pointer--;
                    }
                }
                // + increments the value of the memory cell under the pointer
                else if (s.charAt(i) == '+') {
                    operationsIncrease++;
                    memory[pointer]++;
                }
                // - decrements the value of the memory cell under the pointer
                else if (s.charAt(i) == '-') {
                    operationsDecrease++;
                    memory[pointer]--;
                }
                // . outputs the character signified by the cell at the pointer
                else if (s.charAt(i) == '.') {
                    operationsOutput++;
                    char character = (char) (memory[pointer]);
                    lineOutput.add(character);
                }
                else if (s.charAt(i) == '\n') {
                    output.add(lineOutput);
                    lineOutput = new ArrayList<>();
                }
                // , inputs a character and store it in the cell at the pointer
                else if (s.charAt(i) == ',') {
                    operationsInput++;
                    if (input.size() > 0) {
                        memory[pointer] = (byte) (input.remove(0).charValue());
                    }
                    else {
                        throw new ExecutionException("No input available");
                    }
                }
                // [ jumps past the matching ] if the cell under the pointer is 0
                else if (s.charAt(i) == '[') {
                    operationsLeftLoop++;
                    checkOperations();
                    if (memory[pointer] == 0) {
                        operationsLeftLoop++;
                        checkOperations();
                        i++;
                        while (c > 0 || s.charAt(i) != ']') {
                            checkOperations();
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
                    operationsRightLoop++;
                    checkOperations();
                    if (memory[pointer] != 0) {
                        operationsRightLoop++;
                        checkOperations();
                        i--;
                        while (c > 0 || s.charAt(i) != '[') {
                            if (s.charAt(i) == ']') {
                                operationsRightLoop++;
                                c++;
                            }
                            else if (s.charAt(i) == '[') {
                                operationsLeftLoop++;
                                c--;
                            }
                            else {
                                operationsLeftLoop++; // TODO remove
                            }
                            checkOperations();
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
            return new Result.Succes(output, (end - start), this);
        }
        catch (ExecutionException e) {
            System.err.println(e.getMessage());
            long end = System.currentTimeMillis();
            return new Result.Failure(e.getMessage(), (end - start), this);
        }
    }

    public static class Profile {
        final int size;
        final int maximumOperations;
        public Profile(int size, int maximumOperations) {
            // TODO create a profile using settings pane and use it for the execution
            this.size = size;
            this.maximumOperations = maximumOperations;
        }

        public static final Profile DEFAULT = new Profile(30_000, 512_000);
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
