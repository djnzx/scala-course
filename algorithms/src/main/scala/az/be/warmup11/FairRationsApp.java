package lesson55s5.warmup;

import java.io.*;
import java.util.*;

public class FairRationsApp {

  // Complete the fairRations function below.
  static Optional<Integer> fairRations(int[] B) {
    // happy
    int result=5;
//    return Optional.of(result);

    return Optional.empty();
  }

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

    int N = scanner.nextInt();
    scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

    int[] B = new int[N];

    String[] BItems = scanner.nextLine().split(" ");
    scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

    for (int i = 0; i < N; i++) {
      int BItem = Integer.parseInt(BItems[i]);
      B[i] = BItem;
    }

    String result = fairRations(B).map(String::valueOf).orElse("NO");
    bufferedWriter.write(result);

    bufferedWriter.newLine();

    bufferedWriter.close();

    scanner.close();
  }
}
