package improviso;

import java.util.Random;

/**
 *
 * @author Usuário
 */
public class DoubleRange {
    final private Double value;
    final private Double valueVar;
    final private Double valueEnd;
    final private Double valueEndVar;
    
    public DoubleRange(double val, double valMax, double valFim, double valMaxFim) {
        this.value = val;
        this.valueVar = (valMax - val);

        this.valueEnd = valFim;
        this.valueEndVar = (valMaxFim - valFim);
    }
    
    public double getValue() {
        return getValue(0.0, new Random());
    }
    
    public double getValue(double position) {
        return getValue(position, new Random());
    }
    
    public double getValue(Random rand) {
        return getValue(0.0, rand);
    }
    
    public double getValue(double position, Random rand) {
        double returnVal;
        
        returnVal  = value + ( (valueEnd - value) * position );
        returnVal += rand.nextDouble() * (valueVar + ( (valueEndVar - valueVar) * position));
        
        return returnVal;
    }
}
