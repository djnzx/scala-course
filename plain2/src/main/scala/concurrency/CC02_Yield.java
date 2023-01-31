package concurrency;

public class CC02_Yield {

  private static boolean ready = false;
  private static int x;

  /** maybe never terminates */
  private static class ReaderThread extends Thread {

    @Override
    public void run() {
      while (!ready) Thread.yield();
      System.out.println(x);
    }

    public static void main(String[] args) {
      new ReaderThread().start();
      x = 42;
      ready = true;
    }

  }

}
