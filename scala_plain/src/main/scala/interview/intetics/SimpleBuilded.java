package interview.intetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleBuilded {
    private final List<Integer> simples = new ArrayList<>(0);
    // plain version dynamic implementation
    private final int max;
    private final int min;
    private int sim[];
    private int sim_idx=0;

    SimpleBuilded(int max) {
        this(1, max);
    }

    SimpleBuilded(int mn, int mx) {
        max = mx;
        min = mn;
        sim = new int[this.max/10];
    }

    List<Integer> list() {
        for (int num=1;num<=max;num++) {
            if (new Simple(num,simples).is()) {
                simples.add(num);
            }
        }
        return simples;
    }

/*
    private void action(int value) {
        if (new SimpleDynamic(value, sim, sim_idx).is()) {
            sim[sim_idx++]=value;
        }
    }
*/

    int[] listOptimized() {
        for (int idx=1;idx<=max;idx++) {
            if (new SimpleDynamic(idx, sim, sim_idx).is()) {
                sim[sim_idx++] = idx;
            }
        }
/*
        // this be interested if will be reused.
        IntStream.rangeClosed(1,max).forEach(this::action);
*/
        // cut to MIN
        if (min>1) {
            int min_index=0;
            for (int i=0;i<sim_idx; i++) {
                if (sim[i]>=min) {
                    min_index=i;
                    break;
                }
            }
            return Arrays.copyOfRange(sim,min_index,sim_idx);
        }
        else {
            return sim;
        }
    }

}
