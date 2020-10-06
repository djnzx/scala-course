package warmups.nth;

public class NthJava {
  public int calculate(final int m) {
    int countdown = m; // counter to count down
    int mth_number = 0;
    int sequence = 1;
    while (countdown > 0) {
      if ((sequence % 3 == 0) || (sequence % 4 == 0))
        if (sequence % 2 ==0) {
          countdown--;
          mth_number = sequence;
        }
      sequence++;
    }
    return mth_number;
  }
}
