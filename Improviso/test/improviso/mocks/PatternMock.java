package improviso.mocks;

import improviso.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author User
 */
public class PatternMock extends Pattern {
    private ArrayList<NoteMock> notes = new ArrayList<>();
    private Integer currentDuration;
    private Integer nextDuration;
    private int executions = 0;
    
    public static class PatternMockBuilder extends PatternBuilder {
        public PatternMockBuilder() {
            super();
        }
        
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
    
    public void setNotes(ArrayList<NoteMock> list) {
        this.notes = list;
    }
    
    public ArrayList<NoteMock> getNotes() {
        return this.notes;
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
        MIDINoteList list = new MIDINoteList();
        this.executions++;
        this.notes.forEach((note) -> {
            list.addAll(note.execute(rand, this.currentDuration, finalPosition, length));
        });
        
        return list;
    }
    
    public int getExecutions() {
        return this.executions;
    }
}
