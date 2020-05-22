package warmup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArrayToStreamApp {

  static class ListenerDetails {
    public final Object listener;
    public final Set<Class> interestedIn;

    ListenerDetails(Object listener, Set<Class> interestedIn) {
      this.listener = listener;
      this.interestedIn = interestedIn;
    }

    static ListenerDetails of(Object listener, Class[] interestedIn) {
      return new ListenerDetails(
          listener,
          Collections.unmodifiableSet(Arrays.stream(interestedIn).collect(Collectors.toSet()))
      );
    }
  }

  public static void main(String[] args) {
    Class[] a = { ArrayToStreamApp.class };
  }
}
