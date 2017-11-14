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
    Pattern pattern1;
    
    @Before
    public void setUp() {
        PatternMock.PatternMockBuilder builder = new PatternMock.PatternMockBuilder();
        builder.setDuration(new IntegerRangeMock(100));
        this.pattern1 = builder.build();
    }
    
    @Test
    public void testCreateTrack() {
        RandomMock random = new RandomMock();
        GroupMock group;
        GroupMock.GroupMockBuilder groupBuilder = new GroupMock.GroupMockBuilder();
        group = groupBuilder.build();
        
        Track track;
        Track.TrackBuilder trackBuilder = new Track.TrackBuilder()
                .setId("trackTest").setRootGroup(group);
        track = trackBuilder.build();
        
        assertNotNull(track);
        
        track.initialize(0);
        assertEquals(0, track.getCurrentPosition());
        
        group.setNextPattern(this.pattern1);
        group.setNextMessage(new GroupMessage("test"));
        
        track.selectNextPattern(random);
        assertEquals(100, track.getEnd());
    }
}
