package files.crawler;

import files.FileListV2;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static files.crawler.Fn.lastpart;

public class Crawl {
  public static TreeElement processTree(String path, int level) {
    File file = new File(path);
    // file
    if (file.isFile()) return new TEFile(file.getAbsolutePath(), level, file.length());
    if (file.isDirectory()) {
      ArrayList<TreeElement> contents = new ArrayList<>();
      for (File f: file.listFiles()) {
        contents.add(processTree(f.getPath(), level + 1));
      }
      // folder
      return new TEFolder(lastpart(file.getPath()), level, contents);
    }
    // non existent file
    return new TEEmpty(path, level);
  }
  static void print(TreeElement from) {
    System.out.println(from);
  }

  public static void main(String[] args) {
    String root = "d0";
    TreeElement data = processTree(root, 0);
    print(data);
  }

}
