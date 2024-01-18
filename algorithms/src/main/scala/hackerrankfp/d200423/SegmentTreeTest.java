package hackerrankfp.d200423;

public final class SegmentTreeTest {

  private final static int SIZE = 100;
  private int[] values = new int[SIZE];
  private SegmentTree st;

  private void assertEquals(int a, int b) {
    if (a!=b) throw new RuntimeException("a!=b");
  }

  public void initArray() {
    for (int i = 0; i < SIZE; i++) {
      values[i] = i;
    }
    st = new SegmentTree(values);
  }

  public void verifySearchInRange1(){
    assertEquals(0, st.findMin(0, SIZE -1));
  }

  public void verifySearchInRange2(){
    assertEquals(5, st.findMin(5,11));
  }

  public void verifySearchInRange3(){
    assertEquals(12, st.findMin(12,56));
  }

  public void verifySearchInRange4(){
    assertEquals(4, st.findMin(4,12));
  }

  public void verifySearchInRange5(){
    assertEquals(33, st.findMin(33,77));
  }
}
