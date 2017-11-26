/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso.mocks;

import java.util.Random;

/**
 *
 * @author User
 */
public class IntegerRangeMock extends improviso.IntegerRange {
    private final int fixedVal;
    
    public IntegerRangeMock(int fixedVal) {
        super(fixedVal, fixedVal, fixedVal, fixedVal);
        this.fixedVal = fixedVal;
    }
    
    private int getValueMin() {
        return this.fixedVal;
    }
    
    @Override
    public int getValue(Random rand) {
        return this.getValueMin();
    }
    
    @Override
    public int getValue(Random rand, double position) {
        return this.getValueMin();
    }
}
