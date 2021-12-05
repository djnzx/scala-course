package se1;

public class DataProcessing {

  <A> A obtain() {
    throw new IllegalArgumentException("not implemented");
  }

  <B> void store(B b) {
    throw new IllegalArgumentException("not implemented");
  }

  <A, B> B process(A a) {
    throw new IllegalArgumentException("not implemented");
  }

  <A, B> void wholeCombination() {
    A a = obtain();
    B b = process(a);
    store(b);
  }

}
