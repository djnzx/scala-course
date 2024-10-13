package lesson39w06.warmup;

public class JumpingOnTheClouds {

  static int calc(int[] clouds) {
    final int SAFE = 0;
    int len = clouds.length;
    int counter = 0;
    int curr = 0;
    while (curr < len - 1) {
      if (curr+2<len && clouds[curr+2] == SAFE) {
        counter++;
        curr+=2;
        continue;
      }
      if (curr+1<len && clouds[curr+1] == SAFE) {
        counter++;
        curr+=1;
        continue;
      }
      throw new RuntimeException("no way");
    }
    return counter;
  }

}
