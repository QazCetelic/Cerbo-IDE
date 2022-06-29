package qaz.code.model;

import qaz.code.Sheet;

import java.time.Instant;
import java.util.ArrayList;

public class ExecutionManager {
    private Thread executionThread;
    private Thread killThread;

    public void execute(String code, ArrayList<Character> input, Sheet sheet) {
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
            System.out.println("A " + Instant.now().getEpochSecond());
            Execution.Result result = execution.interpret(code, input);
            System.out.println("B " + Instant.now().getEpochSecond());
            sheet.setResult(result, execution);
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
