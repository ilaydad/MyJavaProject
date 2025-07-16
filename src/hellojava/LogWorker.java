package hellojava;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

public class LogWorker implements Runnable {
    private static final Logger logger = LogManager.getLogger(LogWorker.class);
    private final int workerId;

    public LogWorker(int id) {
        this.workerId = id;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int sleepTime = rand.nextInt(15) + 1; //between 1 and 15 seconds

        try {
          logger.info("Worker " + workerId + " started, sleeping for " + sleepTime + " seconds.");
            System.out.println("Worker " + workerId + " started, sleeping for " + sleepTime + " seconds.");

            Thread.sleep(sleepTime * 1000L);

            logger.info("Worker " + workerId + " finished.");
            System.out.println("Worker " + workerId + " finished.");
        } catch (InterruptedException e) {
            logger.error("Worker " + workerId + " was interrupted.");
            System.err.println("Worker " + workerId + " was interrupted.");
        }
    }
}
