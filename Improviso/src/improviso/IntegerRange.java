package improviso;

import java.util.Random;

/**
 * Randomly generates an integer value within a set interval. This interval
 * can vary linearly between an initial and a final interval, and the generated
 * value will depend on the position of the section currently being executed.
 * @author Fernie Canto
 */
public class IntegerRange {
    final private int value;
    final private int valueMax;
    final private int valueEnd;
    final private int valueEndMax;
    final private Integer resolution;
    
    public IntegerRange(int val, int valMax, int valEnd, int valEndMax) {
        this.value = val;
        this.valueMax = valMax;

        this.valueEnd = valEnd;
        this.valueEndMax = valEndMax;
        
        this.resolution = null;
    }
    
    public IntegerRange(int val, int valMax, int valEnd, int valEndMax, Integer res) {
        this.value = val;
        this.valueMax = valMax;

        this.valueEnd = valEnd;
        this.valueEndMax = valEndMax;
        
        this.resolution = res;
    }
    
    public int getValue(Random rand) {
        return getValue(rand, 0.0);
    }
    
    public int getValue(Random rand, double position) {
        int var = valueMax - value;
        int varEnd = valueEndMax - valueEnd;
        int returnVal;
        
        returnVal  = value + (int)( (valueEnd - value) * position );
        returnVal += rand.nextInt(var + (int)( (varEnd - var) * position) + 1);
        
        if(resolution != null) {
            int delta = (returnVal - value) % resolution;
            
            if(delta < (int)(resolution / 2)) {
                returnVal -= delta;
            } else {
                returnVal += resolution - delta;
            }
        }
        
        return returnVal;
    }
}
