package a;

public class App2 {
  public static void main(String[] args) {
    String name = a.b.c.ImplementationXofInterfaceY.class.getCanonicalName();
    System.out.println(name);
  }
}
