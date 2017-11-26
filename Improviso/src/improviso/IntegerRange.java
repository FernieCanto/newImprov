package improviso;

import java.util.Random;

/**
 * Randomly generates an integer valueMin within a set interval. This interval
 can vary linearly between an initial and a final interval, and the generated
 valueMin will depend on the position of the section currently being executed.
 * @author Fernie Canto
 */
public class IntegerRange {
    final private int valueMin;
    final private int valueMax;
    final private int valueEndMin;
    final private int valueEndMax;
    final private Integer resolution;
    
    public IntegerRange(int val, int valMax, int valEnd, int valEndMax) {
        this.valueMin = val;
        this.valueMax = valMax;

        this.valueEndMin = valEnd;
        this.valueEndMax = valEndMax;
        
        this.resolution = null;
    }
    
    public IntegerRange(int val, int valMax, int valEnd, int valEndMax, Integer res) {
        this.valueMin = val;
        this.valueMax = valMax;

        this.valueEndMin = valEnd;
        this.valueEndMax = valEndMax;
        
        this.resolution = res;
    }
    
    public int getValue(Random rand) {
        return getValue(rand, 0.0);
    }
    
    public int getValue(Random rand, double position) {
        int var = this.valueMax - this.valueMin;
        int varEnd = this.valueEndMax - this.valueEndMin;
        int returnVal;
        
        returnVal  = this.valueMin + (int)( (this.valueEndMin - this.valueMin) * position );
        returnVal += rand.nextInt(var + (int)( (varEnd - var) * position) + 1);
        
        if(this.resolution != null) {
            int delta = (returnVal - this.valueMin) % getResolution();
            
            if(delta < (int)(this.resolution / 2)) {
                returnVal -= delta;
            } else {
                returnVal += this.resolution - delta;
            }
        }
        
        return returnVal;
    }

    public int getValueMin() {
        return valueMin;
    }

    public int getValueMax() {
        return valueMax;
    }

    public int getValueEndMin() {
        return valueEndMin;
    }

    public int getValueEndMax() {
        return valueEndMax;
    }

    public Integer getResolution() {
        return resolution;
    }
}
