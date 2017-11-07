/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.util.Random;

/**
 *
 * @author User
 */
public class IntegerRangeMock extends IntegerRange {
    private final int fixedVal;
    
    IntegerRangeMock(int fixedVal) {
        super(fixedVal, fixedVal, fixedVal, fixedVal);
        this.fixedVal = fixedVal;
    }
    
    @Override
    int getValue() {
        return this.fixedVal;
    }
    
    @Override
    int getValue(float position) {
        return this.fixedVal;
    }
    
    @Override
    int getValue(Random rand) {
        return this.fixedVal;
    }
    
    @Override
    int getValue(double position, Random rand) {
        return this.fixedVal;
    }
}
