package interview.semipalindrom;

import java.util.stream.LongStream;

public class Semi2 {
    public static void main(String[] args) {
        long[] ints = LongStream.rangeClosed(10000, 99999)
                .filter(i -> LongStream.rangeClosed(2, (long) Math.sqrt(i))
                        .allMatch(j -> i % j != 0))
                .toArray();
        System.out.println(ints.length);
        //System.out.println(Arrays.toString(ints));

        long maxPalindrome = 0;
        long multiplierI = 0;
        long multiplierJ = 0;
        long currentI, currentJ;



        for (int i = ints.length-1; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                long product = ((long) ints[i]) * ints[j];
                currentJ = ints[j];
                currentI = ints[i];
                if (product < maxPalindrome){
                    break;
                }

                String longStr = String.valueOf(product);

                if(longStr.equals(new StringBuilder(longStr).reverse().toString()) && product > maxPalindrome) {
                    maxPalindrome = product;
                    multiplierI = currentI;
                    multiplierJ = currentJ;
                }
            }
        }
        System.out.println(maxPalindrome + " " + multiplierI + " " + multiplierJ);
    }}
