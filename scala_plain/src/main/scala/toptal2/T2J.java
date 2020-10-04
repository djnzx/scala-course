package toptal2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class T2J {

  private static void inc(Map<Integer, Integer> m, Integer k) {
    m.merge(k, 1, Integer::sum);
  }
  
  static int minRange(int[] a) {
    final int n = a.length;
    final int k = (int) Arrays.stream(a).distinct().count();

    int l = 0;
    int r = n;
    int j = -1;

    Map<Integer, Integer> map = new HashMap<>();

    for(int i = 0; i < n; i++) {
      while (j < n) {
        j++;
        if (j < n && map.size() < k) inc(map, a[j]);
        if (map.size() == k && (r - l >= j - i)) {
          l = i;
          r = j;
          break;
        }
        
      }
      
      if (map.size() < k) break;
      while (map.size() == k) {
        if (map.getOrDefault(a[i], 0) == 1) map.remove(a[i]);
        else map.put(a[i], map.getOrDefault(a[i], 0) - 1);
        i++;
        if (map.size() == k && r - l >= j - i) {
          l = i;
          r = j;
        }
      }
      if (map.getOrDefault(a[i], 0) == 1) map.remove(a[i]);
      else map.put(a[i], map.getOrDefault(a[i], 0) - 1);
    }
    return r-l+1;
  }

}
