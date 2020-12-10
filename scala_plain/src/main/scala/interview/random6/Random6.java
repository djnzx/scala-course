package interview.random6;

import java.util.Random;
import java.util.TreeSet;

public class Random6 {
    public static void main(String[] args) {
        Random r = new Random();
        TreeSet<Integer> t = new TreeSet<>();
        while (t.size()<6) {
            t.add((int)(Math.random()*48+1));
        }
        System.out.println(t);
    }
}
