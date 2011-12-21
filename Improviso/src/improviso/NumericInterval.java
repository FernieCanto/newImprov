package improviso;

import java.util.Random;

/**
 *
 * @author fernando
 */
public class NumericInterval {
    public int value, valueVar, valueEnd, valueEndVar;
    Random rand = null;
    
    NumericInterval(int val, int valMax, int valFim, int valMaxFim) {
        this.value = val;
        this.valueVar = (valMax - val);

        this.valueEnd = valFim;
        this.valueEndVar = (valMaxFim - valFim);
    }
    
    public void setSeed(long seed) {
        if(rand == null)
            rand = new Random(seed);
        else
            rand.setSeed(seed);
    }
    
    protected Random getRandom() {
        if(rand == null)
            rand = new Random();
        return rand;
    }
    
    int getValue() {
        return getValue(0.0, getRandom());
    }
    
    int getValue(float position) {
        return getValue(position, getRandom());
    }
    
    int getValue(Random rand) {
        return getValue(0.0, rand);
    }
    
    int getValue(double position, Random rand) {
        int returnVal;
        
        returnVal  = value + (int)( (valueEnd - value) * position );
        returnVal += rand.nextInt(valueVar + (int)( (valueEndVar - valueVar) * position) + 1);
        
        return returnVal;
    }
}