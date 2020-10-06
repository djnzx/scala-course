package warmups;

import java.util.Scanner;

public class HammingDistanceInteger {

    static int countBits(int val) {
        int total=0;
        for (int i = 0; i < 16; i++) {
            if (((val >> i) & 1) == 1) total++;
        }
        return total;
    }

    static int hammingDistance(int one, int two) {
        int similarBits = one & two;
        int mask = ~similarBits;
        int diff = (one & mask) | (two & mask);
        return countBits(diff);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int len=in.nextInt();
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i]=in.nextInt();
        }

        int total=0;
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                total+=hammingDistance(a[i], a[j]);
            }
        }
        System.out.println(total);
    }
}
