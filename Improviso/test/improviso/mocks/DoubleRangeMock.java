/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso.mocks;

import improviso.DoubleRange;
import java.util.Random;

/**
 * @author Fernie Canto
 */
public class DoubleRangeMock extends DoubleRange {
    private final double fixedVal;
    public DoubleRangeMock(double fixedVal) {
        super(fixedVal, fixedVal, fixedVal, fixedVal);
        this.fixedVal = fixedVal;
    }
    
    @Override
    public double getValue() {
        return this.fixedVal;
    }
    
    @Override
    public double getValue(double position) {
        return this.getValue();
    }
    
    @Override
    public double getValue(Random rand) {
        return this.getValue();
    }
    
    @Override
    public double getValue(double position, Random rand) {
        return this.getValue();
    }
}
