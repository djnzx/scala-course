package local_return;

import java.util.Arrays;

public class LocalReturnJ {
    public static void something() {
        Arrays.asList(1,2,3).forEach(x -> System.out.println(x));
    }

    public static void main(String[] args) {
        something();
    }
}
