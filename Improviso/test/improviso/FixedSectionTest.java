/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.*;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class FixedSectionTest {
    private static final int PATTERN1_LENGTH = 200;
    private static final int PATTERN2_LENGTH = 300;
    private RandomMock random;
    private PatternMock pattern1;
    private PatternMock pattern2;
    private GroupMock group1;
    private GroupMock group2;
            
    @Before
    public void setUp() {
        this.random = new RandomMock();
        PatternMock.PatternMockBuilder patternBuilder1 = new PatternMock.PatternMockBuilder();
        MIDINoteList notes1 = new MIDINoteList();
        notes1.add(new MIDINote(10, 10, 10, 10, 1));
        this.pattern1 = patternBuilder1.build();
        this.pattern1.setNextDuration(FixedSectionTest.PATTERN1_LENGTH);
        this.pattern1.setMIDINotes(notes1);
        
        GroupMock.GroupMockBuilder groupBuilder1 = new GroupMock.GroupMockBuilder();
        this.group1 = groupBuilder1.build();
        this.group1.setNextMessage(new GroupMessage("test1"));
        this.group1.setNextPattern(this.pattern1);
        
        PatternMock.PatternMockBuilder patternBuilder2 = new PatternMock.PatternMockBuilder();
        MIDINoteList notes2 = new MIDINoteList();
        notes2.add(new MIDINote(10, 10, 10, 10, 1));
        notes2.add(new MIDINote(20, 20, 20, 20, 1));
        this.pattern2 = patternBuilder2.build();
        this.pattern2.setNextDuration(FixedSectionTest.PATTERN2_LENGTH);
        this.pattern2.setMIDINotes(notes2);
        
        GroupMock.GroupMockBuilder groupBuilder2 = new GroupMock.GroupMockBuilder();
        this.group2 = groupBuilder2.build();
        this.group2.setNextMessage(new GroupMessage("test2"));
        this.group2.setNextPattern(this.pattern2);
    }
    
    @Test
    public void testBuildFixedSection() {
        FixedSection section;
        FixedSection.FixedSectionBuilder builder = new FixedSection.FixedSectionBuilder();
        builder.setLength(new IntegerRangeMock(500)).setId("sectionTest").setTempo(100);
        section = builder.build();
        
        assertNotNull(section);
        
        section.initialize(this.random, 100);
        assertEquals(new Integer(600), section.getEnd());
        assertEquals(100, section.getCurrentPosition());
    }
    
    @Test
    public void testExecuteFixedSectionOneTrack() {
        FixedSection section;
        FixedSection.FixedSectionBuilder sectionBuilder = new FixedSection.FixedSectionBuilder();
        sectionBuilder.setLength(new IntegerRangeMock(500)).setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(new Track.TrackBuilder().setRootGroup(this.group1).build());
        section = sectionBuilder.build();
        section.initialize(this.random, 100);
        
        this.pattern1.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(3, this.pattern1.getExecutions());
        assertEquals(700, section.getCurrentPosition());
        assertEquals(3, notes.size());
    }
    
    @Test
    public void testExecuteFixedSectionTwoTracks() {
        FixedSection section;
        FixedSection.FixedSectionBuilder sectionBuilder = new FixedSection.FixedSectionBuilder();
        sectionBuilder.setLength(new IntegerRangeMock(500)).setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(new Track.TrackBuilder().setRootGroup(this.group1).build());
        sectionBuilder.addTrack(new Track.TrackBuilder().setRootGroup(this.group2).build());
        section = sectionBuilder.build();
        section.initialize(this.random, 100);
        
        this.pattern1.resetExecutions();
        this.pattern2.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(3, this.pattern1.getExecutions());
        assertEquals(2, this.pattern2.getExecutions());
        assertEquals(700, section.getCurrentPosition());
        assertEquals(7, notes.size());
    }
}
