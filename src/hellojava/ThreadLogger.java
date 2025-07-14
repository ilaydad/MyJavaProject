package hellojava;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadLogger implements Runnable  {
	private static final Logger logger = LogManager.getLogger(ThreadLogger.class);
	 public static void main(String[] args) {
	        Thread thread = new Thread(new ThreadLogger());
	        thread.start();
	    }

	    @Override
	    public void run() {
	        logger.info("Thread is working — log entry written by a separate thread.");
	        System.out.println("Thread is running and writing to log file.");
	    }
	}

	


