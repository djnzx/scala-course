package count_java.util;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class Functions {

  public static String indent(int level, int LEVEL_SIZE) {
    byte[] bs = new byte[level * LEVEL_SIZE];
    Arrays.fill(bs, (byte) ' ');
    return new String(bs);
  }

  public static String lastChunk(File f) {
    String[] chunks = f.toPath().toString().split("/");
    return chunks[chunks.length - 1];
  }

  public static <A> Stream<A> safeStream(A[] origin) {
    return origin != null ? Arrays.stream(origin) : Stream.empty();
  }

  public static File fileFromRes(String fname) {
    return new File(Functions.class.getClassLoader().getResource(fname).getFile());
  }

}
