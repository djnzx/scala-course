package interview.intetics;

class Palindrome {
    private final long one;
    private final long two;
    private long cached_mul =-1;

    Palindrome(long one, long two) {
        this.one = one;
        this.two = two;
    }

    private long mul() {
        if (cached_mul ==-1) {
            cached_mul = one * two;
        }
        return cached_mul;
    }

    boolean is() {
        boolean ret = true;
        String sNumber = Long.toString(mul());
        int len = sNumber.length();
        for (int i=0; i<len/2; i++) {
            if (sNumber.charAt(i) != sNumber.charAt(len-i-1)) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    Entry entry() {
        return new Entry(one, two, mul());
    }
}
