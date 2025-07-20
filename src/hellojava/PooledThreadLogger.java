package hellojava;

import java.util.concurrent.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PooledThreadLogger {
	private static final Logger logger = LogManager.getLogger(PooledThreadLogger.class);
	private static final int MAX_THREADS = 5;
	private static final int INTERVAL_SECONDS = 5;
	
	private static int workerCounter = 1;
	public static void main (String[] args) { 
		
		//thread pool
		ExecutorService threadPool = new ThreadPoolExecutor (
				MAX_THREADS,
				MAX_THREADS,
				0L,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>()
		        );

		//timer triggered every 5 seconds
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate (() -> {
			if (((ThreadPoolExecutor) threadPool).getActiveCount() < MAX_THREADS) {
                LogWorker worker = new LogWorker(workerCounter++);
                threadPool.submit(worker);
            } else {
            	logger.warn("the pool is full, the new thread will be put on hold.");
            	 System.out.println("pool is full, could not add new thread. Try again in 5 seconds.");
            }
        }, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

}
				
		
		
		
	