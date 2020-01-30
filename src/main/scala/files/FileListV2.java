package files;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileListV2 {

  static String tab(int level) {
    return String.join("", Collections.nCopies(level*4, " "));
  }

  static String lastpart(String path) {
    String[] split = path.split("/");
    return split[split.length-1];
  }

  abstract static class TreeElement {
    public final String path;
    public final int level;

    protected TreeElement(String path, int level) {
      this.path = path;
      this.level = level;
    }
  }

  static class TEEmpty extends TreeElement {
    protected TEEmpty(String path, int level) {
      super(path, level);
    }

    @Override
    public String toString() {
      return String.format("%sE: %s", tab(level), path);
    }
  }

  static class TEFile extends TreeElement {
    public final Long size;

    public TEFile(String path, int level, Long size) {
      super(path, level);
      this.size = size;
    }

    @Override
    public String toString() {
      return String.format("%s%s, s:%s", tab(level), path, size);
    }
  }

  static class TEFolder extends TreeElement {
    public final List<TreeElement> items;

    public TEFolder(String path, int level) {
      this(path, level, new ArrayList<>());
    }

    public TEFolder(String path, int level, List<TreeElement> items) {
      super(path, level);
      this.items = items;
    }

    @Override
    public String toString() {
      String h = String.format("%s[%s]", tab(level), path);
      String h2 = items.stream().map(Object::toString).collect(Collectors.joining("\n"));
      return h2.equals("") ? h : String.format("%s\n%s", h, h2);
    }
  }

  static TreeElement process(String path, int level) {
    File file = new File(path);
    if (file.isFile()) return new TEFile(file.getAbsolutePath(), level, file.length());
    if (file.isDirectory()) {
      ArrayList<TreeElement> contents = new ArrayList<>();
      for (File f: file.listFiles()) {
        contents.add(process(f.getPath(), level + 1));
      }
      return new TEFolder(lastpart(file.getPath()), level, contents);
    }
    return new TEEmpty(path, level);
  }

  static void print(TreeElement from) {
    System.out.println(from);
  }

  public static void main(String[] args) {
//        String root = "/Volumes/Photos";
//    String root = "/Volumes/WD4TB/media";
    String root = "d0";
    TreeElement data = process(root, 0);
    print(data);
  }
}
