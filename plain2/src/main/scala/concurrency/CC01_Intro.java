package concurrency;

public class CC01_Intro {

  static void tn(String prefix) {
    System.out.println(prefix + " " + Thread.currentThread().getName());
  }

  static class Box {
    private int i = 0;

    private Object monitor = new Object();

    public synchronized void inc() {
      i++;
    }

    public void dec() {

      synchronized (this) {
        i--;
      }

    }

    public int get() {

      synchronized (monitor) {
        return i;
      }

    }
  }

  public static void main(String[] args) throws InterruptedException {
    tn("main");
    Box box = new Box();

    Runnable r1 = () -> {
      tn("r1");
      for (int i = 1; i <= 100_000; i++) box.inc();
    };

    Runnable r2 = () -> {
      tn("r2");
      for (int i = 1; i <= 100_000; i++) box.dec();
    };

    Thread t1 = new Thread(r1);
    Thread t2 = new Thread(r2);

    t1.start();
    t2.start();

    Thread.sleep(100);
    System.out.println(box.i);

  }
}
