package hellojava;

public class ThreadLogger {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            Thread thread = new Thread(new LogWorker(i));
            thread.start();
        }
    }
}

	


