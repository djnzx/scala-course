package count_java.count;

public final class CountNonBlank implements Count {
  @Override
  public int count(String s) {
    return s.trim().isEmpty() ? 0 : 1;
  }
}
