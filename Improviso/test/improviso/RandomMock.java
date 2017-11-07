package improviso;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Fernie Canto
 */
public class RandomMock extends Random {
    private final ArrayList<Double> doubles = new ArrayList<>();
    private final ArrayList<Integer> ints = new ArrayList<>();
    
    public void addDouble(Double nextDouble) {
        doubles.add(nextDouble);
    }
    
    @Override
    public double nextDouble() {
        if (doubles.isEmpty()) {
            return super.nextDouble();
        } else {
            Double nextDouble = doubles.get(0);
            doubles.remove(0);
            return nextDouble;
        }
    }
    
    public void addInteger(Integer nextInt) {
        ints.add(nextInt);
    }
    
    @Override
    public int nextInt() {
        if (ints.isEmpty()) {
            return super.nextInt();
        } else {
            Integer nextInt = ints.get(0);
            ints.remove(0);
            return nextInt;
        }
    }
    
    @Override
    public int nextInt(int bound) {
        if (ints.isEmpty() || ints.get(0) >= bound) {
            return super.nextInt(bound);
        } else {
            Integer nextInt = ints.get(0);
            ints.remove(0);
            return nextInt;
        }
    }
}
