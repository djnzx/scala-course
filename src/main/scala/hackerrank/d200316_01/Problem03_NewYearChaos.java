package hackerrank.d200316_01;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Problem03_NewYearChaos {

  static class Pair<A, B> {
    public final A a;
    public final B b;

    Pair(A index, B valid) {
      this.a = index;
      this.b = valid;
    }
  }

  static boolean isOrdered(int[] data) {
    return IntStream.range(0, data.length-1)
        .allMatch(idx -> data[idx] < data[idx + 1]);
  }

  static int findLeftUnordered(int[] data) {
    for (int i = 0; i < data.length-1; i++) {
      if (data[i] != i+1) return i;
    }
    return data.length;
  }

  static int findRightUnordered(int[] data) {
    int prev = data.length;
    for (int i = data.length-1; i > 0; i--) {
      if (data[i] != i+1) return prev;
      prev = i;
    }
    return -1;
  }

  static int[] pairIndexesFrom(int[] data) {
    return IntStream.range(0, data.length - 1)
        .mapToObj(idx -> new Pair<>(idx, data[idx] > data[idx+1])) // a - index, b - valid to perm
        .filter(p -> p.b)
        .mapToInt(p -> p.a)
        .toArray();
  }

  static int pickIdxFrom(int[] indexes, int[] data) {
    // min to left
    int minIndexPairToLeft = Arrays.stream(indexes)
        .mapToObj(idx -> new Pair<>(idx, data[idx + 1])) // a - index, b - value
        .min((o1, o2) -> o1.b - o2.b)
        .orElseThrow(RuntimeException::new)
        .a; // index
    //  max to right
    int maxIndexPairToRight = Arrays.stream(indexes)
        .mapToObj(idx -> new Pair<>(idx, data[idx])) // a - index, b - value
        .max((o1, o2) -> o1.b - o2.b)
        .orElseThrow(RuntimeException::new)
        .a; // index

//    System.out.printf("minIndexPairToLeft: %d\n", minIndexPairToLeft);
//    System.out.printf("maxIndexPairToRight: %d\n", maxIndexPairToRight);

//    System.out.printf("findLeftUnordered: %d\n", findLeftUnordered(data));
    int deltaL = Math.abs(minIndexPairToLeft - (findLeftUnordered(data)+1));
//    System.out.printf("findRightUnordered: %d\n", findRightUnordered(data));
    int deltaR = Math.abs(findRightUnordered(data) - (maxIndexPairToRight+1));

//    System.out.printf("Left:  idx:%d, num:%d, dL:%d\n", minIndexPairToLeft, data[minIndexPairToLeft + 1], deltaL);
//    System.out.printf("Right: idx:%d, num:%d, dR:%d\n", maxIndexPairToRight, data[maxIndexPairToRight], deltaR);
    if (deltaL<deltaR) return minIndexPairToLeft;
    else return maxIndexPairToRight;
  }

  static int pairIndexToPermutationFrom(int[] data) {
    int[] idxs = pairIndexesFrom(data);
//    System.out.printf("Eligible to perm: %s\n", Arrays.toString(idxs));
    return pickIdxFrom(idxs, data);
  }

  static void swap(int[] data, int index) {
    int t = data[index];
    data[index] = data[index+1];
    data[index+1] = t;
  }

  static void swapAndRegister(int[] data, int index, int[] reg) {
    swap(data, index);
    reg[data[index+1]]++;
  }

  static boolean isMore2(int[] reg) {
    for (int cnt: reg) {
      if (cnt > 2) return true;
    }
    return false;
  }

  static void minimumBribes(int[] a) {
    int number = 0;
    int[] counter = new int[a.length+1]; // we won't use index zero
    int[] process = a.clone();

//    System.out.print("indexes: "); IntStream.range(0, 8).forEach(i -> System.out.printf("%d: ", i));System.out.println();
//    System.out.printf("Source: %s\n", Arrays.toString(process));
    boolean too = false;

    while (!isOrdered(process)) {
      int idx = pairIndexToPermutationFrom(process);
//      System.out.printf("index: %d\n", idx);
//      System.out.print("indexes:         "); IntStream.range(0, 8).forEach(i -> System.out.printf("%d: ", i));System.out.println();
//      System.out.printf("Process Before: %s\n", Arrays.toString(process));
      swapAndRegister(process, idx, counter);
//      System.out.printf("Process After : %s\n", Arrays.toString(process));
//      System.out.print("Numbers:  "); IntStream.rangeClosed(0, 8).forEach(i -> System.out.printf("%d: ", i));
//      System.out.println();
//      System.out.printf("Weights: %s\n", Arrays.toString(counter));
      if (isMore2(counter)) {
        too = true;
        break;
      }
      number++;
//      System.out.println("--------");
    }
    System.out.println(too ? "Too chaotic" : number);
  }

  public static void main(String[] args) {
    int[] src = {1,2,5,3,7,8,6,4};
    minimumBribes(src);
  }
}
