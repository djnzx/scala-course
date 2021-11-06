package interview.intetics;

import java.util.ArrayList;
import java.util.List;

class Simple {
    private static final int MIN_SIMPLE = 2;
    private final int origin;
    private final List<Integer> simpleItemsBefore;

    /**
     * Simple - just seek 1..sqrt(N)
     * @param origin
     */
    @Deprecated
    Simple(int origin) {
        this(origin, new ArrayList<>());
    }

    /**
     * Simple
     * Smart Version
     * just seek 1..sqrt(N) only within previous simple list built
     * @param origin
     */
    Simple(int origin, List<Integer> from) {
        this.origin = origin;
        this.simpleItemsBefore = from;
    }

    private boolean restIsZero(int orig, int div) {
        return (orig % div) == 0;
    }

    boolean is() {
        if (origin==1) return false;
        if (origin==2) return true;
        int maxToAnalyze = (int) Math.sqrt(origin);

        if (simpleItemsBefore.size()==0) {
            // plain search
            for (int item=MIN_SIMPLE;item<=maxToAnalyze;item++) {
                if (restIsZero(origin, item)) {
                    return false;
                }
            }
        }
        else {
            // dynamic search
            for (int item : simpleItemsBefore) {
                if (item<=maxToAnalyze && restIsZero(origin, item)) {
                    return false;
                }
            }
        }
        return true;
    }
}
