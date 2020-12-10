package interview.pragmatic;

public class Warmup2 {

  public static void main(String[] args) {
    final String VOWELS = "aeoui";
    String s = "Alex";
    long count = s.codePoints().map(n -> n | 0x20)
        .filter(c -> VOWELS.contains(String.valueOf((char)c)))
        .count();
    System.out.println(count);
  }

}
