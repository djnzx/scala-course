package hackerrankfp.d200602;

abstract class AbstractClass {
  public void method() {
    System.out.println("AC");
  }
}

interface Interface {
  default void method() {
    System.out.println("INT");
  }
}

class TestClass extends AbstractClass implements Interface {
//  @Override
//  public void method() {
//    System.out.println("OVER");
//  }

  public static void main(String[] args) {
    TestClass object = new TestClass();
    object.method();
  }
}