package concurrency;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CC03_SyncMap {

  public static void main(String[] args) {
    HashMap<Integer, String> unsafe = new HashMap<>();
    Map<Integer, String> safe = Collections.synchronizedMap(unsafe);
  }
}
