package files;

import java.io.File;

public class FileListV1 {

  static void processFolder(String name, int level) {
    if (level>1) return;
    File file = new File(name);
    // if smth went wrong - exit
    if (!file.exists() || !file.isDirectory()) return;
    // get contents
    File[] files = file.listFiles();
    for (File f: files) {
      for (int i = 0; i < level; i++) System.out.print("\t");
      if (f.isFile()) {
        System.out.println(f.getName());
      } else if (f.isDirectory()) {
        System.out.printf("[%s]\n", f.getName());
        processFolder(f.getAbsolutePath(), level + 1);
      }
    }
  }

  public static void main(String[] args) {
//        String root = "/Volumes/Photos";
    String root = "/Volumes/WD4TB/media";
    processFolder(root, 0);
  }
}
