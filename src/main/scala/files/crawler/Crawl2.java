package files.crawler;

import java.io.File;
import java.util.*;

import static files.crawler.Fn.lastpart;

public class Crawl2 {

  private static int counter = 0;
  private static String woprefix(String origin, String prefix) {
    return origin.substring(prefix.length());
  }

  private static class FileEntry {
    public final String name;
    public final Long size;

    private FileEntry(String name, Long size) {
      this.name = name;
      this.size = size;
    }
  }

  private final static List<FileEntry> src_data = new ArrayList<>(75000);

  public static TreeElement processTree(String path, int level, String prefix) {
    counter++;
    if (counter % 10 == 0) System.out.print(counter+" ");
    if (counter % 100 == 0) System.out.println();
    File file = new File(path);
    // file
    if (file.isFile()) return new TEFile(woprefix(file.getPath(), prefix), level, file.length());
    if (file.isDirectory()) {
      ArrayList<TreeElement> contents = new ArrayList<>();
      for (File f: file.listFiles()) {
        contents.add(processTree(f.getPath(), level + 1, prefix));
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

  static void processNode(TreeElement from) {
    if (from instanceof TEFile) {
      TEFile tefile = (TEFile) from;
      src_data.add(new FileEntry(tefile.path, tefile.size));
    } else if (from instanceof TEFolder) {
      TEFolder tefolder = (TEFolder) from;
      tefolder.items.forEach(Crawl2::processNode);
    }
  }

  private static void compare(FileEntry e, String dst) {
    String name = e.name;
    Long size = e.size;
    File dstfile = new File(dst, name);
    if (name.endsWith("ZbThumbnail.info")
        || (name.endsWith("Thumbs.db"))
        || (name.endsWith(".DS_Store"))
    ) {
      dstfile.delete();
    } else if (!dstfile.exists()) {
      System.out.printf("%s - DOESN'T EXIST\n", name);
    } else if (dstfile.length() != size) {
      System.out.printf("%s - HAS DIFFERENT SIZE\n", name);
    }
  }

  public static void main(String[] args) {
    String src = "/Volumes/Photos/";      // OLD location
    String dst = "/Volumes/WD4TB/media/"; // NEW location
    TreeElement data = processTree(src, 0, src);
    processNode(data);
    src_data.forEach(e -> compare(e, dst));
  }

}
