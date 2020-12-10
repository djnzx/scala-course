package interview.intetics;

class Entry {
    private final long one;
    private final long two;
    private final long mult;

    Entry(long one, long two, long mult) {
        this.one = one;
        this.two = two;
        this.mult = mult;
    }

    @Override
    public String toString() {
        return String.format("1:%d, 2:%d, mult:%d", one, two, mult);
    }

    boolean biggerThan(Entry e) {
        return this.mult>e.mult;
    }
}
