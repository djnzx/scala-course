package glovolive;

import java.util.*;

public class TaskBJava1 {
  
  public static boolean isValid(String s) {
    if (s == null || s.length() %2 !=0) return false;
    Stack<Character> stack = new Stack<>();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '[':
        case '(': 
        case '{':
          stack.push(c);
          break;
        case ']':
          if (stack.isEmpty()) return false;
          if (stack.peek() != '[') return false;
          stack.pop();
          break;
        case ')':
          if (stack.isEmpty()) return false;
          if (stack.peek() != '(') return false;
          stack.pop();
          break;
        case '}':
          if (stack.isEmpty()) return false;
          if (stack.peek() != '{') return false;
          stack.pop();
          break;
        default: return false;  
      }
    }

    return stack.isEmpty();
  }
  
  
}
