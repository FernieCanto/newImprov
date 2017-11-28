package improviso;

import improviso.mocks.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author FernieCanto
 */
public class DoubleRangeTest {
    @Test
    public void testFixedValueRange() {
        RandomMock random = new RandomMock();
        random.addDouble(0.3333);
        DoubleRange fixedValue = new DoubleRange(.5);
        assertEquals(new Double(.5), fixedValue.getValue(random));
        assertEquals(new Double(.5), fixedValue.getValue(random, 0.0));
        assertEquals(new Double(.5), fixedValue.getValue(random, 0.5));
        assertEquals(new Double(.5), fixedValue.getValue(random, 1.0));
    }
    
    @Test
    public void testMinMaxValueRange() {
        RandomMock random = new RandomMock();
        random.addDouble(0.0);
        random.addDouble(0.1);
        random.addDouble(0.9);
        random.addDouble(1.0);
        DoubleRange minMaxValue = new DoubleRange(.5, 1.0);
        assertEquals(new Double(.5), minMaxValue.getValue(random));
        assertEquals(new Double(.55), minMaxValue.getValue(random));
        assertEquals(new Double(.95), minMaxValue.getValue(random));
        assertEquals(new Double(1.0), minMaxValue.getValue(random));
    }
    
    @Test
    public void testVariableValueRange() {
        RandomMock random = new RandomMock();
        DoubleRange variableValue = new DoubleRange(.5, .5, 1.0, 1.0);
        assertEquals(new Double(.5), variableValue.getValue(random));
        assertEquals(new Double(.5), variableValue.getValue(random, 0.0));
        assertEquals(new Double(.75), variableValue.getValue(random, 0.5));
        assertEquals(new Double(1.0), variableValue.getValue(random, 1.0));
    }
    
    @Test
    public void testVariableMinMaxValueRange() {
        RandomMock random = new RandomMock();
        DoubleRange variableMinMaxValue = new DoubleRange(.0, .2, .6, 1.0);
        
        random.addDouble(.0);
        assertEquals(.0, variableMinMaxValue.getValue(random, 0.0), 0.000001);
        random.addDouble(.25);
        assertEquals(.05, variableMinMaxValue.getValue(random, 0.0), 0.000001);
        random.addDouble(1.0);
        assertEquals(.2, variableMinMaxValue.getValue(random, 0.0), 0.000001);
        
        random.addDouble(.0);
        assertEquals(.3, variableMinMaxValue.getValue(random, 0.5), 0.000001);
        random.addDouble(0.5);
        assertEquals(.45, variableMinMaxValue.getValue(random, 0.5), 0.000001);
        random.addDouble(1.0);
        assertEquals(.6, variableMinMaxValue.getValue(random, 0.5), 0.000001);
        
        random.addDouble(.0);
        assertEquals(.6, variableMinMaxValue.getValue(random, 1.0), 0.000001);
        random.addDouble(.5);
        assertEquals(.8, variableMinMaxValue.getValue(random, 1.0), 0.000001);
        random.addDouble(1.0);
        assertEquals(1.0, variableMinMaxValue.getValue(random, 1.0), 0.000001);
    }
}
