package improviso;

import java.util.Random;

/**
 *
 * @author Usuário
 */
public class DoubleInterval {
    public double value, valueVar, valueEnd, valueEndVar;
    Random rand = null;
    
    DoubleInterval(double val, double valMax, double valFim, double valMaxFim) {
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
    
    double getValue() {
        return getValue(0.0, getRandom());
    }
    
    double getValue(double position) {
        return getValue(position, getRandom());
    }
    
    double getValue(Random rand) {
        return getValue(0.0, rand);
    }
    
    double getValue(double position, Random rand) {
        double returnVal;
        
        returnVal  = value + ( (valueEnd - value) * position );
        returnVal += rand.nextDouble() * (valueVar + ( (valueEndVar - valueVar) * position));
        
        return returnVal;
    }
}
