package improviso.mocks;

import improviso.Pattern;
import java.util.Random;

/**
 *
 * @author User
 */
public class PatternMock extends Pattern {
    private Integer currentDuration;
    private Integer nextDuration;
    
    public static class PatternMockBuilder extends PatternBuilder {
        @Override
        public PatternMock build() {
            return new PatternMock(this);
        }
    }
    
    private PatternMock(PatternMockBuilder builder) {
        super(builder);
    }
    
    public void setNextDuration(Integer duration) {
        this.nextDuration = duration;
    }
    
    @Override
    public int getLength() {
        return this.currentDuration;
    }
    
    @Override
    public void initialize(Random random) {
        this.currentDuration = this.nextDuration;
    }
}
