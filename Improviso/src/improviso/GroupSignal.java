package improviso;

import java.util.Random;

/**
 *
 * @author FernieCanto
 */
public class GroupSignal {
    private final Integer minExecutionsSignal;
    private final Double probabilitySignal;
    private final Integer maxExecutionsSignal;
    
    public GroupSignal(Integer min, Integer max, Double probability) {
        this.minExecutionsSignal = min;
        this.maxExecutionsSignal = max;
        this.probabilitySignal = probability;
    }
    
    public boolean signal(int executions, Random random) {
        if (executions > this.maxExecutionsSignal) {
            return true;
        }
        if (executions > this.minExecutionsSignal && random.nextDouble() <= this.probabilitySignal) {
            return true;
        }
        return false;
    }
}
