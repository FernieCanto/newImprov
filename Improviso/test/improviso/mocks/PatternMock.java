package improviso.mocks;

import improviso.*;
import java.util.Random;

/**
 *
 * @author User
 */
public class PatternMock extends Pattern {
    private MIDINoteList MIDINotes = new MIDINoteList();
    private Integer currentDuration;
    private Integer nextDuration;
    private int executions = 0;
    
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
    
    public void resetExecutions() {
        this.executions = 0;
    }
    
    public void setMIDINotes(MIDINoteList list) {
        this.MIDINotes = list;
    }
    
    @Override
    public int getLength() {
        return this.currentDuration;
    }
    
    @Override
    public void initialize(Random random) {
        this.currentDuration = this.nextDuration;
    }
    
    @Override
    public MIDINoteList execute(Random rand, double finalPosition, Integer length) {
        this.executions++;
        return this.MIDINotes;
    }
    
    public int getExecutions() {
        return this.executions;
    }
}
