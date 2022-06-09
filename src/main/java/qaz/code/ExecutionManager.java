package qaz.code;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ExecutionManager {
    private Thread executionThread;
    private Thread killThread;

    public void execute(String code, ArrayList<Character> input, Sheet sheet) {
        // Stopping old threads
        if (executionThread != null && executionThread.isAlive()) executionThread.interrupt();
        if (killThread != null && killThread.isAlive()) killThread.interrupt();
        // Create new thread
        executionThread = new Thread(() -> {
            // Delay 500ms
            try {
                TimeUnit.MILLISECONDS.sleep(100);
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
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e) {}
            if (executionThread.isAlive()) {
                System.err.println("Execution is taking too long, aborting...");
                executionThread.stop();
                executionThread.interrupt();
            }
        });
        killThread.start();
    }
}
