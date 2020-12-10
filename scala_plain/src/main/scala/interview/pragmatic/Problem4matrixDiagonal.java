package interview.pragmatic;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Problem4matrixDiagonal {

  /**
   * 1. naive implementation
   * with while and variables in the stack
   */
  static String traverse1(int R, int C, int[][] m) {
    StringJoiner sj = new StringJoiner(" ");
    int r = 0, c = 0, next = 0;
    do {
      sj.add(String.valueOf(m[r][c]));
      switch (next) {
        case 0: c++; next=1; break;
        case 1:
               if (r == R-1) {      c++; next=2; } // last row
          else if (c == 0  ) { r++;      next=2; } // first column
          else               { r++; c--;         }
          break;
        case 2:
               if (c == C-1) { r++;      next=1; } // last column
          else if (r == 0  ) {      c++; next=1; } // first row
          else               { r--; c++;         }
      }
    } while (r<R && c<C);
    return sj.toString();
  }

  /**
   * 2. implementation
   * with MUTABLE state
   * encapsulated in the class Track
   */
  static String traverse2(int R, int C, int[][] m) {

    class Track {
      int r = 0;
      int c = 0;
      int next = 0;
    }

    Track t = new Track();
    return IntStream.range(0, R * C).map(n -> {
      int val = m[t.r][t.c];
      switch (t.next) {
        case 0: t.c++; t.next=1; break;
        case 1:
          if      (t.r == R-1) {        t.c++; t.next=2; } // last row
          else if (t.c == 0  ) { t.r++;        t.next=2; } // first column
          else                 { t.r++; t.c--;           }
          break;
        case 2:
          if      (t.c == C-1) { t.r++;        t.next=1; } // last column
          else if (t.r == 0  ) {        t.c++; t.next=1; } // first row
          else                 { t.r--; t.c++;           }
      }
      return val;
    })
        .mapToObj(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  /**
   * 3. pure math implementation
   * based on found relations
   */
  static String traverse3(int R, int C, int[][] m) {
    StringJoiner sj=new StringJoiner(" ");

    int row = 0;
    int sumOfIndexes = 0;
    int numberOfCombinations = 1;
    int counter = 0;

    for (int i = 0; i < R*C ; i++) {
      if (row<R && sumOfIndexes-row<C)
        sj.add(String.valueOf(m[row][sumOfIndexes-row]));

      if (row>=R || sumOfIndexes-row>=C)
        i--;
      counter++;
      row = (sumOfIndexes&1)==0 ? row-1 : row+1;
      if(counter == numberOfCombinations){
        sumOfIndexes++;
        numberOfCombinations = sumOfIndexes + 1;
        counter = 0;
        row = (sumOfIndexes&1)==0 ? sumOfIndexes : 0 ;
      }
    }
    return sj.toString();
  }

  /**
   * foldLeft(xs: Seq[A], acc: B, f: (B, A) => B): B
   */
  static <A, B> B foldLeft(Collection<A> data, B empty, BiFunction<B, A, B> f) {
    Iterator<A> it = data.iterator();
    B acc = empty;
    while (it.hasNext()) {
      acc = f.apply(acc, it.next());
    }
    return acc;
  }

  /**
   * 4. foldLeft based implementation
   * with IMMUTABLE state
   * encapsulated in class Track
   */
  static String traverse4(int R, int C, int[][] m) {

    class Track {
      final int r;
      final int c;
      final int next;
      final List<Integer> l;

      public Track() {
        this(0,0,0, new LinkedList<>());
      }

      public Track(int r, int c, int next, List<Integer> l) {
        this.r = r;
        this.c = c;
        this.next = next;
        this.l = l;
      }

      Track c(int delta) {
        return new Track(r,c+delta, next, l);
      }

      Track r(int delta) {
        return new Track(r+delta, c, next, l);
      }

      Track nxt(int value) {
        return new Track(r, c, value, l);
      }

      Track add(int value) {
        l.add(value);
        return this;
      }
    }

    List<Integer> indexes = IntStream.range(0, R * C).boxed().collect(Collectors.toList());
    return foldLeft(indexes, new Track(), (t, v) -> {
      t.add(m[t.r][t.c]);
      switch (t.next) {
        case 1:
          if (t.r == R - 1)  return t.c(1).nxt(2);
          else if (t.c == 0) return t.r(1).nxt(2);
          else               return t.r(1).c(-1);
        case 2:
          if (t.c == C - 1)  return t.r(1).nxt(1);
          else if (t.r == 0) return t.c(1).nxt(1);
          else               return t.r(-1).c(1);
        default: return t.c(1).nxt(1);
      }
    }).l.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  /**
   * 5. foldLeft based implementation
   * with IMMUTABLE state
   * encapsulated in class Track
   * and logic partially moved to the class Track
   */
  static String traverse5(int R, int C, int[][] m) {

    class Track {
      final int r;
      final int c;
      final int next;
      final List<Integer> l;
      final int R;
      final int C;

      public Track(int R, int C) {
        this(0,0,0, new LinkedList<>(), R, C);
      }

      public Track(int r, int c, int next, List<Integer> l, int R, int C) {
        this.r = r;
        this.c = c;
        this.next = next;
        this.l = l;
        this.R = R;
        this.C = C;
      }

      Track c(int delta) {
        return new Track(r,c+delta, next, l, R, C);
      }

      Track r(int delta) {
        return new Track(r+delta, c, next, l, R, C);
      }

      Track nxt(int value) {
        return new Track(r, c, value, l, R, C);
      }

      Track add(int value) {
        l.add(value);
        return this;
      }

      boolean isRowFirst() {
        return r == 0;
      }

      boolean isRowLast() {
        return r == R - 1;
      }

      boolean isColFirst() {
        return c == 0;
      }

      boolean isColLast() {
        return c == C - 1;
      }

    }

    List<Integer> indexes = IntStream.range(0, R * C).boxed().collect(Collectors.toList());
    return foldLeft(indexes, new Track(R, C), (t, v) -> {
      t.add(m[t.r][t.c]);
      return t.next==1 ? t.isRowLast() ? t.c(1).nxt(2) : t.isColFirst() ? t.r(1).nxt(2) : t.r(1).c(-1)
           : t.next==2 ? t.isColLast() ? t.r(1).nxt(1) : t.isRowFirst() ? t.c(1).nxt(1) : t.r(-1).c(1)
           : t.c(1).nxt(1);
    }).l.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  /**
   * 6. foldLeft based implementation
   * with IMMUTABLE state
   * encapsulated in class Track
   * and logic FULLY moved to the class Track
   */
  static String traverse6(int R, int C, int[][] m) {

    class Track {
      final int r;
      final int c;
      final int next;
      final List<Integer> l;

      public Track() {
        this(0,0,0, new LinkedList<>());
      }

      public Track(int r, int c, int next, List<Integer> l) {
        this.r = r;
        this.c = c;
        this.next = next;
        this.l = l;
      }

      Track c(int delta) {
        return new Track(r,c+delta, next, l);
      }

      Track r(int delta) {
        return new Track(r+delta, c, next, l);
      }

      Track nxt(int value) {
        return new Track(r, c, value, l);
      }

      Track add(int value) {
        l.add(value);
        return this;
      }

      boolean isRowFirst() {
        return r == 0;
      }

      boolean isRowLast() {
        return r == R - 1;
      }

      boolean isColFirst() {
        return c == 0;
      }

      boolean isColLast() {
        return c == C - 1;
      }

      Track step() {
        add(m[r][c]);
        return next==1 ? isRowLast() ? c(1).nxt(2) : isColFirst() ? r(1).nxt(2) : r(1).c(-1)
            : next==2 ? isColLast() ? r(1).nxt(1) : isRowFirst() ? c(1).nxt(1) : r(-1).c(1)
            : c(1).nxt(1);
      }

    }

    List<Integer> indexes = IntStream.range(0, R * C).boxed().collect(Collectors.toList());
    return foldLeft(indexes, new Track(), (t, v) -> t.step())
        .l.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(" "));
  }

  /**
   * 6. implementation without foldLeft
   * with IMMUTABLE state
   * encapsulated in class Track
   * and logic FULLY moved to the class Track
   */
  interface Steppable<A> {
    A step();
  }

  static class IterateN<A extends Steppable<A>> {
    A step(int cnt, A t) {
      if (cnt == 0) return t;
      return step(cnt -1, t.step());
    }
  }

  static String traverse7(int R, int C, int[][] m) {

    class Track implements Steppable<Track> {
      final int r;
      final int c;
      final int next;
      final List<Integer> l;

      public Track() {
        this(0,0,0, new LinkedList<>());
      }

      public Track(int r, int c, int next, List<Integer> l) {
        this.r = r;
        this.c = c;
        this.next = next;
        this.l = l;
      }

      Track c(int delta) {
        return new Track(r,c+delta, next, l);
      }

      Track r(int delta) {
        return new Track(r+delta, c, next, l);
      }

      Track nxt(int value) {
        return new Track(r, c, value, l);
      }

      Track add(int value) {
        l.add(value);
        return this;
      }

      boolean isRowFirst() {
        return r == 0;
      }

      boolean isRowLast() {
        return r == R - 1;
      }

      boolean isColFirst() {
        return c == 0;
      }

      boolean isColLast() {
        return c == C - 1;
      }

      @Override
      public Track step() {
        add(m[r][c]);
        return next==1 ? isRowLast() ? c(1).nxt(2) : isColFirst() ? r(1).nxt(2) : r(1).c(-1)
             : next==2 ? isColLast() ? r(1).nxt(1) : isRowFirst() ? c(1).nxt(1) : r(-1).c(1)
             : c(1).nxt(1);
      }

    }

    return
        new IterateN<Track>().step(R * C, new Track()) // iterate over the matrix
        .l.stream()                                        // Stream<Integer>
        .map(String::valueOf)                              // Stream<String>
        .collect(Collectors.joining(" "));         // String
  }

  static String traverse8(int R, int C, int[][] m) {

    final List<Integer> result = new LinkedList<>();

    class Track {
      final int r;
      final int c;
      final int next;

      public Track() {
        this(0,0,0);
      }

      public Track(int r, int c, int next) {
        this.r = r;
        this.c = c;
        this.next = next;
      }

      Track c(int delta) {
        return new Track(r,c+delta, next);
      }

      Track r(int delta) {
        return new Track(r+delta, c, next);
      }

      Track nxt(int value) {
        return new Track(r, c, value);
      }

      Track add(int value) {
        result.add(value);
        return this;
      }

      boolean isRowFirst() {
        return r == 0;
      }

      boolean isRowLast() {
        return r == R - 1;
      }

      boolean isColFirst() {
        return c == 0;
      }

      boolean isColLast() {
        return c == C - 1;
      }

      public Track step() {
        add(m[r][c]);
        return next==1 ? isRowLast() ? c(1).nxt(2) : isColFirst() ? r(1).nxt(2) : r(1).c(-1)
             : next==2 ? isColLast() ? r(1).nxt(1) : isRowFirst() ? c(1).nxt(1) : r(-1).c(1)
             : c(1).nxt(1);
      }

    }

    class Iterate {
      Track step(int n, Track t) {
        if (n==0) return t;
        return step(n-1, t.step());
      }
    }

    new Iterate().step(R * C, new Track());

    return result.stream()                         // Stream<Integer>
        .map(String::valueOf)                      // Stream<String>
        .collect(Collectors.joining(" ")); // String
  }

  public static void main(String[] args) {
//        {1,  2, 3},
//        {5,  6, 7},
//        {9, 10,11},
//        {13,14,15},
//        {17,18,19},
//        {21,22,23},
//        {25,26,27},
    int[][]a = {
        {  1,  2,  3,  4, },
        {  5,  6,  7,  8, },
        {  9, 10, 11, 12, },
        { 13, 14, 15, 16, },
        { 17, 18, 19, 20, },
        { 21, 22, 23, 24, },
        { 25, 26, 27, 28, },
    };
//        {1,  2,},
//        {5,  6,},
//        {9, 10,},
//        {13,14,},
//        {17,18,},
//        {21,22,},
//        {25,26,},
//        {1,  2, 3, 4,  100},
//        {5,  6, 7, 8,  101},
//        {9, 10,11, 12, 102},
//        {13,14,15, 16, 103},
//        {17,18,19, 20, 104},
//        {21,22,23, 24, 105},
//        {25,26,27, 28, 106},
//    };
    System.out.println("1 2 5 9 6 3 4 7 10 13 17 14 11 8 12 15 18 21 25 22 19 16 20 23 26 27 24 28");
//    System.out.println(traverse1(a.length, a[0].length, a));
//    System.out.println(traverse2(a.length, a[0].length, a));
//    System.out.println(traverse3(a.length, a[0].length, a));
//    System.out.println(traverse4(a.length, a[0].length, a));
//    System.out.println(traverse5(a.length, a[0].length, a));
//    System.out.println(traverse6(a.length, a[0].length, a));
//    System.out.println(traverse7(a.length, a[0].length, a));
    System.out.println(traverse8(a.length, a[0].length, a));
  }
}
