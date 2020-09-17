package glovolive;

import java.util.*;

public class TaskBJava2 {
  
  public static boolean isValid(String s) {
    if (s == null || s.length() %2 !=0) return false;
    Set<Character> open = new HashSet<>(Arrays.asList('(', '[', '{'));
    Map<Character, Character> pairs = new HashMap<Character, Character>() {{
      put('}', '{');
      put(']', '[');
      put(')', '(');
    }};
    Stack<Character> stack = new Stack<>();

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      
      if (open.contains(c)) stack.push(c);
      else {
        if (!pairs.containsKey(c)) return false; // syntax
        if (stack.isEmpty()) return false; // empty stack on closing one
        if (!pairs.get(c).equals(stack.peek())) return false; // wrong pair
        stack.pop();
      }
    }

    return stack.isEmpty();
  }
  
  
}
