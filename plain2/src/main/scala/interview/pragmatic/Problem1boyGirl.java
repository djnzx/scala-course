package interview.pragmatic;

public class Problem1boyGirl {

  public static void main(String[] args) {
    int X = 5;
    int Y = 1;
    int max = Math.max(X, Y);
    int min = Math.min(X, Y);
    int intervals = min+1;
    int r = (int) Math.ceil((double) max/intervals);
    System.out.println(r);
  }

}
