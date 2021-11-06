package interview.join;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class JoinCollections001 {

    final static int SIZE1 = 10;
    final static int SIZE2 = 15;

    private static int rand() {
        return rand(0, 100);
    }

    private static int rand(int from, int to) {
        return (int)(Math.random()*(to-from)+from);
    }

    private static boolean check_sorted(List<Integer> list) {
        if (list.size()<2) return true;
        int el1 = list.get(0);
        int el2;
        for (int i = 1; i < list.size(); i++) {
            el2 = list.get(i);
            System.out.printf("%d : %d\n", el1, el2);
            if (!(el1 <= el2)) return false;
            el1 = el2;
        }
        return true;
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> c1 = new PriorityQueue<>(SIZE1);
        PriorityQueue<Integer> c2 = new PriorityQueue<>(SIZE2);
        while (c1.size()<SIZE1) {
            c1.add(rand());
        }
        while (c2.size()<SIZE2) {
            c2.add(rand(100, 200));
        }
        LinkedList<Integer> l1 = new LinkedList<>();
        while (c1.size()>0) {
            l1.add(c1.poll());
        }
        LinkedList<Integer> l2 = new LinkedList<>();
        while (c2.size()>0) {
            l2.add(c2.poll());
        }
        System.out.printf("List 1: %s\n", l1);
        System.out.printf("List 2: %s\n", l2);
        LinkedList<Integer> target = new LinkedList<>();
        while (l1.size()>0 && l2.size()>0) {
            target.add(l1.peek() < l2.peek() ? l1.remove() : l2.remove());
        }
        target.addAll(l1);
        target.addAll(l2);
        System.out.printf("Merged List: %s\n", target);
        //System.out.println(check_sorted(target));
    }
}