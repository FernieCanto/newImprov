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
    
    private int getFixedVal() {
        return this.fixedVal;
    }
    
    @Override
    public Integer getValue(Random rand) {
        return this.getFixedVal();
    }
    
    @Override
    public Integer getValue(Random rand, double position) {
        return this.getFixedVal();
    }
}
