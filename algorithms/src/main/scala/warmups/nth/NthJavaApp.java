package warmups.nth;

import java.util.Scanner;

public class NthJavaApp {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter the number:");
    int m = in.nextInt();
    NthJava app = new NthJava();
    int r = app.calculate(m);
    System.out.printf("%d-th number is:%d\n", m, r);
  }
}
