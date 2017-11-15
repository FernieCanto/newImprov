/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso.mocks;

import improviso.*;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class TrackMock extends Track {
    private int numExecutions = 0;
    private Pattern pattern;
    final private ArrayList<GroupMessage> groupMessages = new ArrayList<>();
    
    public static class TrackMockBuilder extends Track.TrackBuilder {
        @Override
        public TrackMock build() {
            return new TrackMock(this);
        }
    }
    
    protected TrackMock(TrackMockBuilder builder) {
        super(builder);
    }
    
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
    
    public void addMessage(boolean finished, boolean interrupt) {
        GroupMessage message = new GroupMessage("Test");
        if (finished) {
            message.signal();
        }
        if (interrupt) {
            message.finish();
        }
        this.groupMessages.add(message);
    }
    
    @Override
    public GroupMessage getMessage() {
        this.numExecutions++;
        GroupMessage message = this.groupMessages.get(0);
        return message;
    }
    
    @Override
    public Pattern execute() {
        this.groupMessages.remove(0);
        return super.execute();
    }
    
    @Override
    public int getExecutions() {
        return this.numExecutions;
    }
}
