package warmup.charcoord;

import java.util.*;

public class CharacterCoordinatesJava_V0 {
    public static String queueToString(Queue<Integer> q) {
        StringBuilder sb = new StringBuilder("[");
        while (q.size()>0) {
            sb.append(q.poll());
            if (q.size()>0) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public static boolean inRange(char c) {
        return c>='A' && c<='Z' || c>='a' && c<='z';
    }

    public static void main(String[] args) {
        String input = "This is test string";
        Scanner in = new Scanner(
                input
                //System.in
                );
        String s = in.nextLine();
        // grab data
        HashMap<Character, Queue<Integer>> outcome = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Character symbol = s.charAt(i);
            if (inRange(symbol)) {
                Queue<Integer> list = outcome.getOrDefault(symbol, new PriorityQueue<>());
                list.add(i);
                outcome.put(symbol,list);
            }
        }
        // sort data
        PriorityQueue<Map.Entry<Character, Queue<Integer>>> sorted = new PriorityQueue<>((o1, o2) -> o1.getKey() - o2.getKey());
        outcome.forEach((sym, list) -> sorted.add(new AbstractMap.SimpleEntry<>(sym, list)));
        // print data
        while (sorted.size()>0) {
            Map.Entry<Character, Queue<Integer>> e = sorted.poll();
            System.out.printf("%s=%s%s",e.getKey(), queueToString(e.getValue()), sorted.size()>0 ? ", " : "");
        }
    }
}
