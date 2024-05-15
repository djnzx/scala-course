package kse;

public class JavaPlayground {

    // min element
    static int min(int[] xs) {
        int min = xs[0];
        for (int x: xs) {
            min = Math.min(min, x);
        }
        return min;
    }

    // max element
    static int max(int[] xs) {
        int max = xs[0];
        for (int x: xs) {
            max = Math.max(max, x);
        }
        return max;
    }

    // sum
    static int sum(int[] xs) {
        int sum = 0;
        for (int x: xs) {
            sum = sum + x;
        }
        return sum;
    }

    // contains

    public static void main(String[] args) {

        int[] data = {1,2,3,4,5};

        int mn = min(data); // 1
        int mx = max(data); // 5
        int sm = sum(data); // 15

        System.out.println(mn);
        System.out.println(mx);
        System.out.println(sm);

    }

}
