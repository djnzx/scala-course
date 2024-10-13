package lesson56s6.warmup;

public class Application {

  public static void main(String[] args) {
    Rectangles rectangles = new Rectangles();
    System.out.printf("List of generated rectangles is: %s\n", rectangles.list());
    System.out.printf("The square of all the rectangles is: %d\n", rectangles.area());
  }

}
