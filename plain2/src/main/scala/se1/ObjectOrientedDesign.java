package se1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Point2D {
  int x;
  int y;

  public Point2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  void move(int dx, int dy) {
    x += dx;
    y += dy;
  }

  void show() {
    //...
  }

  void hide() {
    //...
  }

}

class Person {
  private final String name;
  private final List<String> knowledge;

  Person(String name, List<String> knowledge) {
    this.name = name;
    this.knowledge = knowledge;
  }

  public String getName() {
    return name;
  }

  public List<String> getKnowledge() {
    return knowledge;
  }
}

public class ObjectOrientedDesign {

  public static void main1(String[] args) {
    Point2D p1 = new Point2D(10, 20);
    p1.show();
    p1.hide();
    p1.move(5, 6); // 15, 26
    p1.x -= 10;
    p1.show();
  }

  public static void main(String[] args) {
    Person person = new Person("Jim", new ArrayList<>(Arrays.asList("Java", "JavaScript")));
    String name = person.getName();
    List<String> knowledge = person.getKnowledge();
    knowledge.add("PHP");
    System.out.println(person.getKnowledge());
  }

}
