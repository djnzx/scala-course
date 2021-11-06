package interview.intetics;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Found {
    private final static int MIN=10000;
    private final static int MAX=99999;
    private final static int NS_TO_MS=1_000_000;

    private static boolean test(int i) {
        return new Simple(i).is();
    }

    public static void main1(String[] args) {
        final long timeStart = System.nanoTime();
        // blind search MIN..MAX
        int[] simples = IntStream.rangeClosed(MIN, MAX).filter(Found::test).toArray();
        final int length=simples.length;
        final int i0 = simples[0];
        Entry max = new Entry(i0, i0, i0 * i0);
        for (int idx1=0; idx1<length; idx1++) {
            for (int idx2=idx1; idx2<length; idx2++) {
                Palindrome p = new Palindrome(simples[idx1], simples[idx2]);
                if (p.is() && p.entry().biggerThan(max)) {
                    max=p.entry();
                }
            }
        }
        System.out.printf("Maximum found: %s, time used:%dms\n",max,(System.nanoTime()-timeStart)/NS_TO_MS);
    }

    public static void main(String[] args) {
        final long timeStart = System.nanoTime();
        // dynamic search 1..MAX optimized to arrays instead of ArrayList
        int[] simplesMinMaxOptimized = new SimpleBuilded(MIN, MAX).listOptimized();
        final long timeSimple = System.nanoTime();
        final int length=simplesMinMaxOptimized.length;
        final int i0 = simplesMinMaxOptimized[0];
        Entry max = new Entry(i0, i0, i0 * i0);
        for (int idx1=0; idx1<length; idx1++) {
            for (int idx2=idx1; idx2<length; idx2++) {
                Palindrome p = new Palindrome(simplesMinMaxOptimized[idx1], simplesMinMaxOptimized[idx2]);
                if (p.is() && p.entry().biggerThan(max)) {
                    max=p.entry();
                }
            }
        }
        System.out.printf("Time to build Simples list: %dms\n",(timeSimple-timeStart)/NS_TO_MS);
        System.out.printf("Maximum found: %s, time used:%dms\n",max,(System.nanoTime()-timeStart-timeSimple)/NS_TO_MS);
    }

    public static void main9(String[] args) {
/*
        final long timeStart = System.nanoTime();
        // blind search 1..MAX
        int[] simplesBlind = IntStream.rangeClosed(1, MAX).filter(Found::test).toArray();
        final long timeBlind = System.nanoTime()-timeStart;
        // dynamic search 1..MAX
        List<Integer> simples1max = new SimpleBuilded(MAX).list();
        final long timeDynamic = System.nanoTime()-timeBlind-timeStart;
        // dynamic search 1..MAX optimized to arrays instead of ArrayList
        int[] simplesMinMaxOptimized = new SimpleBuilded(MIN, MAX).listOptimized();
        final long timeDynamicOptimized = System.nanoTime()-timeStart-timeBlind-timeDynamic;

        System.out.printf("Simple list size:%d\nTime to find simples (blind method) 1..99999: %sms\n-------------------\n",simplesBlind.length,timeBlind/NS_TO_MS);
        System.out.printf("Simple list size:%d\nTime to find simples (dynmic method) 1..99999: %sms\n-------------------\n",simples1max.size(),timeDynamic/NS_TO_MS);
        System.out.printf("Simple list size:%d\nTime to find simples (dynmic method) 10000..99999: %sms\n-------------------\n",simplesMinMaxOptimized.length,timeDynamicOptimized/NS_TO_MS);
*/
        final int count=5;
        final int[] time = new int[count];
        int[][] test = new int[count][];
        for (int i=0; i<count; i++) {
            long begin=System.nanoTime();
            test[i]=
                // mega optimized version
                new SimpleBuilded(MIN, MAX).listOptimized();
                // blind version
                //IntStream.rangeClosed(MIN, MAX).filter(Found::test).toArray();

            long end=System.nanoTime();
            time[i]= (int) ((end-begin)/NS_TO_MS);
        }
        for (int t:time) {
            System.out.printf("time:%dms\n",t);
        }
        for (int[] a : test) {
            System.out.printf("%s\n",Arrays.toString(a));
        }



/*
        final int length=simplesMinMaxOptimized.length;

        Integer i0 = simplesMinMaxOptimized[0];
        Entry max = new Entry(i0, i0, i0 * i0);
        for (int idx1=0; idx1<length; idx1++) {
            for (int idx2=idx1; idx2<length; idx2++) {
                Palindrome p = new Palindrome(simplesMinMaxOptimized[idx1], simplesMinMaxOptimized[idx2]);
                if (p.is() && p.entry().biggerThan(max)) {
                    max=p.entry();
                }
            }
        }
        final long timeToFind = (System.nanoTime()-timeBlind-timeStart-timeDynamic-timeDynamicOptimized)/NS_TO_MS;
        System.out.printf("Maximum found: %s, time used:%dms\n",max,timeToFind);
*/
    }
}
