package warmup;

import java.util.Scanner;

public class Olha6 {
  public static void main(String[] args) {
    Scanner reader = new Scanner(System.in);
    System.out.print("Enter m: ");
    final int m = reader.nextInt();
    System.out.print("Enter n: ");
    final int n = reader.nextInt();
    // we imply, that m < n
    int i = n;
    while (i >= m && i % 7 > 0 && i % 9 > 0) i--;
    System.out.println(i);
  }
}
