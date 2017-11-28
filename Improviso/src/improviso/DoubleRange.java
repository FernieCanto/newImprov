package improviso;

import java.util.Random;

/**
 *
 * @author Usuário
 */
public class DoubleRange extends NumberRange<Double> {
    public DoubleRange(double val) {
        super(val, val);
    }
    
    public DoubleRange(double valMin, double valMax) {
        super(valMin, valMax);
    }
    
    public DoubleRange(double valMin, double valMax, double valEndMin, double valEndMax) {
        super(valMin, valMax, valEndMin, valEndMax);
    }
    
    @Override
    public Double getValue(Random random, double position) {
        return getVariation(random, position) + getValueMin();
    }
    
    private double getVariation(Random random, double position) {
        return getBaseValue(position) + getRandomVariation(random, position);
    }
    
    private double getBaseValue(double position) {
        return (double)( (getValueEndMin() - getValueMin()) * position );
    }
    
    private double getRandomVariation(Random random, double position) {
        return random.nextDouble() * (this.getIntervalStart() + getBaseInterval(position));
    }
    
    private double getIntervalStart() {
        return getValueMax() - getValueMin();
    }
    
    private double getIntervalEnd() {
        return getValueEndMax() - getValueEndMin();
    }
    
    private double getBaseInterval(double position) {
        return (this.getIntervalEnd() - this.getIntervalStart()) * position;
    }
}
