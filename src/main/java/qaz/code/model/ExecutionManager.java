package qaz.code.model;

import java.util.ArrayList;
import java.util.List;

public class ExecutionManager {
    public static final ExecutionManager INSTANCE = new ExecutionManager();
    private int lastExecutionCodeHash = 0;
    private int lastExecutionInputHash = 0;
    private Thread executionThread;
    private Thread killThread;

    public void process(Sheet sheet) {
        final String codeWithComments = sheet.codeProperty().get();
        final String cleanedCode = codeWithComments
                // Remove everything that isn't an instruction or newline
                .replaceAll("[^-+,.\\[\\]><\n]", "")
                // Trim start
                .replaceAll("^\s+", "")
                // Trim end
                .replaceAll("\s+$", "");
        final List<Character> input = new ArrayList<>() {{
            for (char c : sheet.inputProperty().get().toCharArray()) add(c);
        }};
        
        if (cleanedCode.hashCode() != lastExecutionCodeHash || input.hashCode() != lastExecutionInputHash) {
            lastExecutionCodeHash = cleanedCode.hashCode();
            lastExecutionInputHash = input.hashCode();
            // Stopping old threads
            if (executionThread != null && executionThread.isAlive()) executionThread.interrupt();
            if (killThread != null && killThread.isAlive()) killThread.interrupt();
            // Create new thread
            executionThread = new Thread(() -> {
                // Delay 100ms
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException ignored) {}
                // Create execution machine
                Execution execution = new Execution(Execution.Profile.DEFAULT);
                // Execute code
                Result result = execution.interpret(cleanedCode, input);
                sheet.lastResultProperty().set(result);
            });
            // And start it
            executionThread.start();
    
            // Creates parallel thread that kills execution thread if it takes too long
            killThread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException ignored) {}
                if (executionThread.isAlive()) {
                    System.err.println("Execution is taking too long, aborting...");
                    executionThread.interrupt();
                }
            });
            killThread.start();
        }
    }
}
