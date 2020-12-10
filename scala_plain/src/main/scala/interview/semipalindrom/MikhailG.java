package interview.semipalindrom;

import java.util.*;

public class MikhailG {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        ArrayList<Integer> list = new ArrayList<>(primeNumbers(99999, 9999));
        palindrome(list);

        long timeSpent = System.currentTimeMillis() - startTime;
        System.out.println(timeSpent);
    }

    private static void palindrome(ArrayList<Integer> primeNumbers) {
        long iterations = 0;
        long palindrome = 0;
        long firstMultiplier = 0;
        long secondMultiplier = 0;

        for (int i = 0; i < primeNumbers.size(); i++) {
            for (int j = i + 1; j < primeNumbers.size(); j++) {
                iterations++;
                long mult = (long) primeNumbers.get(i) * primeNumbers.get(j);
                if ((mult > palindrome)&&palindromeCheck(mult)) {
                    palindrome = mult;
                    firstMultiplier = primeNumbers.get(i);
                    secondMultiplier = primeNumbers.get(j);
                }
                if (j < primeNumbers.size() - 1 && (long)primeNumbers.get(i) * primeNumbers.get(j + 1) < palindrome ) {
                    break;
                }
            }
        }

        System.out.println("Number of iterations: " + iterations / 1000+"_"+iterations % 1000);
        System.out.println("Palindrome: " + palindrome);
        System.out.println("First multiplier: " + firstMultiplier);
        System.out.println("Second multiplier: " + secondMultiplier);
    }

    private static boolean palindromeCheck(long i) {
        char[] palindrome = String.valueOf(i).toCharArray();
        int begin = 0;
        int end = palindrome.length - 1;

        while (begin < end) {
            if (palindrome[begin] == palindrome[end]) {
                begin++;
                end--;
            } else {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<Integer> primeNumbers(int max, int min) {
        boolean[] array = new boolean[max];
        int count=0;

        for (int i = 2; Math.pow(i, 2) <= max; i++) {
            if (!array[i]) {
                for (int j = (int) Math.pow(i, 2); j < max; j += i) {
                    array[j] = true;
                }
            }
        }

        ArrayList<Integer> primeNumbers = new ArrayList<>();
        for (int i = max - 1; i >= min; i--) {
            if (!array[i]) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }
}