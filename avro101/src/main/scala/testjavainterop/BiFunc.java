package testjavainterop;

public interface BiFunc<A, B, C> {
  C make(A a, B b);
}
