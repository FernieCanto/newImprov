/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class TrackTest {
    PatternMock pattern1;
    
    @Before
    public void setUp() {
        PatternMock.PatternMockBuilder builder = new PatternMock.PatternMockBuilder();
        builder.setDuration(new IntegerRangeMock(100));
        this.pattern1 = builder.build();
    }
    
    @Test
    public void testCreateTrack() {
        RandomMock random = new RandomMock();
        GroupMessage message = new GroupMessage("test");
        GroupMock group;
        GroupMock.GroupMockBuilder groupBuilder = new GroupMock.GroupMockBuilder();
        groupBuilder.setFinishedSignal(new GroupSignalMock())
                .setInterruptSignal(new GroupSignalMock());
        group = groupBuilder.build();
        group.execute(random);
        
        Track track;
        Track.TrackBuilder trackBuilder = new Track.TrackBuilder()
                .setId("trackTest").setRootGroup(group);
        track = trackBuilder.build();
        
        assertNotNull(track);
        assertEquals("trackTest", track.getId());
        
        track.initialize(300);
        assertEquals(300, track.getCurrentPosition());
        assertEquals(0, group.getExecutions());
        
        group.setNextPattern(this.pattern1);
        group.setNextMessage(message);
        
        this.pattern1.setNextDuration(150);
        track.selectNextPattern(random);
        assertEquals(this.pattern1, track.getCurrentPattern());
        assertEquals(message, track.getMessage());
        assertEquals(this.pattern1.getLength(), 150);
        assertEquals(450, track.getEnd());
        
        track.execute();
        assertEquals(450, track.getCurrentPosition());
    }
}
