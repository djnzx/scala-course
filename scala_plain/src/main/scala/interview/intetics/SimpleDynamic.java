package interview.intetics;

class SimpleDynamic {
    private final int origin;
    private final int[] simples;
    private final int length;

    SimpleDynamic(int num, int[] simp, int len) {
        origin = num;
        simples = simp;
        length =len;
    }

    private boolean restIsZero(int orig, int idx) {
        return (orig % simples[idx]) == 0;
    }

    private boolean toBeAnalyzed(int idx, int max) {
        return simples[idx]<=max;
    }

    boolean is() {
        if (origin==1) return false;
        if (origin==2) return true;
        int maxToAnalyze = (int) Math.sqrt(origin);
        for (int idx=0; idx<length; idx++) {
            if (toBeAnalyzed(idx, maxToAnalyze)
                && restIsZero(origin, idx)) {
                return false;
            }
        }
        return true;
    }
}
