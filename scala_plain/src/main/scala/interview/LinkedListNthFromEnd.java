package interview;

import java.util.Iterator;
import java.util.LinkedList;

public class LinkedListNthFromEnd {

    public static Integer getNthEnd(LinkedList<Integer> origin, int pos) {
        if (origin.size() < pos) {
            throw new IllegalArgumentException("List is too small");
        }
        if (origin.size() == pos) {
            return origin.get(0);
        }
        Iterator<Integer> it = origin.descendingIterator();
        for (int i = 0; i < pos-1; i++) {
            it.next();
        }
        return it.next();
    }

    public static void main(String[] args) {
        LinkedList<Integer> ll = new LinkedList<>();
        ll.add(1);
        ll.add(2);
        ll.add(3);
        System.out.println(getNthEnd(ll, 3));



    }
}
