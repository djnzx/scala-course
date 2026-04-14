package az.be.betweentwosets;

import java.io.*;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// https://www.hackerrank.com/challenges/between-two-sets/problem
class Result {

    public static int getTotalX(List<Integer> a, List<Integer> b) {
        int lcmA = lcmList(a); // [2, 4] -> 4
        int gcdB = gcdList(b); // [16, 32, 96] -> 16

        int count = 0;

        // 4,8,12,16
        // 16 % 12 != 0
        for (int x = lcmA; x <= gcdB; x += lcmA) {
            if (gcdB % x == 0) count++;
        }

        return count; // 3
    }

    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    static int gcdList(List<Integer> xs) {
        return xs.stream()
                .reduce(Result::gcd)
                .orElseThrow(IllegalStateException::new);
    }

    static int lcmList(List<Integer> xs) {
        return xs.stream()
                .reduce(Result::lcm)
                .orElseThrow(IllegalStateException::new);
    }

}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(System.out));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int m = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> arr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        List<Integer> brr = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                .map(Integer::parseInt)
                .collect(toList());

        int total = Result.getTotalX(arr, brr);

        bufferedWriter.write(String.valueOf(total));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
