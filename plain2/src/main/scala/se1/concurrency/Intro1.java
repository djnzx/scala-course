package se1.concurrency;

public class Intro1 {

  int sum(int[] data) {
    int total = 0;

    for (int a: data) {
      total += a;
    }

    return total;
  }

  public static void main(String[] args) {

    System.out.println("Hello");

  }

}
