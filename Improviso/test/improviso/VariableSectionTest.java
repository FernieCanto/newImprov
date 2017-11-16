/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class VariableSectionTest {
    private static final int PATTERN1_LENGTH = 200;
    private static final int PATTERN2_LENGTH = 300;
    private static final int PATTERN3_LENGTH = 700;
    private RandomMock random;
    private PatternMock pattern1;
    private PatternMock pattern2;
    private PatternMock pattern3;
    private GroupMock group1;
    private GroupMock group2;
    private GroupMock group3;
    
    @Before
    public void setUp() {
        this.random = new RandomMock();
        PatternMock.PatternMockBuilder patternBuilder1 = new PatternMock.PatternMockBuilder();
        MIDINoteList notes1 = new MIDINoteList();
        notes1.add(new MIDINote(10, 10, 10, 10, 1));
        this.pattern1 = patternBuilder1.build();
        this.pattern1.setNextDuration(VariableSectionTest.PATTERN1_LENGTH);
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
        this.pattern2.setNextDuration(VariableSectionTest.PATTERN2_LENGTH);
        this.pattern2.setMIDINotes(notes2);
        
        GroupMock.GroupMockBuilder groupBuilder2 = new GroupMock.GroupMockBuilder();
        this.group2 = groupBuilder2.build();
        this.group2.setNextMessage(new GroupMessage("test2"));
        this.group2.setNextPattern(this.pattern2);
        
        PatternMock.PatternMockBuilder patternBuilder3 = new PatternMock.PatternMockBuilder();
        MIDINoteList notes3 = new MIDINoteList();
        notes3.add(new MIDINote(10, 10, 10, 10, 1));
        notes3.add(new MIDINote(20, 20, 20, 20, 1));
        notes3.add(new MIDINote(30, 30, 30, 30, 1));
        this.pattern3 = patternBuilder3.build();
        this.pattern3.setNextDuration(VariableSectionTest.PATTERN3_LENGTH);
        this.pattern3.setMIDINotes(notes3);
        
        GroupMock.GroupMockBuilder groupBuilder3 = new GroupMock.GroupMockBuilder();
        this.group3 = groupBuilder3.build();
        this.group3.setNextMessage(new GroupMessage("test3"));
        this.group3.setNextPattern(this.pattern3);
    }
    
    @Test
    public void testCreateVariableSection() {
        VariableSection section;
        VariableSection.VariableSectionBuilder sectionBuilder = new VariableSection.VariableSectionBuilder();
        sectionBuilder.setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(new TrackMock.TrackMockBuilder().setRootGroup(this.group1).build());
        section = sectionBuilder.build();
        
        assertNotNull(section);
        
        section.initialize(this.random, 100);
        assertFalse(section.getEnd().endIsKnown());
        assertEquals(100, section.getActualEnd());
    }
    
    @Test
    public void testExecuteVariableSectionOneTrackFinished() {
        TrackMock track1 = (TrackMock) new TrackMock.TrackMockBuilder().setRootGroup(this.group1).build();
        VariableSection section;
        VariableSection.VariableSectionBuilder sectionBuilder = new VariableSection.VariableSectionBuilder();
        sectionBuilder.setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(track1);
        section = sectionBuilder.build();
        track1.addMessage(false, false);
        track1.addMessage(false, false);
        track1.addMessage(true, false);
        
        section.initialize(this.random, 100);
        
        this.pattern1.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(3, this.pattern1.getExecutions()); // 300 - 500 - 700
        assertEquals(700, section.getActualEnd());
        assertEquals(3, notes.size());
    }
    
    @Test
    public void testExecuteVariableSectionTwoTracksFinished() {
        TrackMock track1 = (TrackMock) new TrackMock.TrackMockBuilder().setId("track1").setRootGroup(this.group1).build();
        TrackMock track2 = (TrackMock) new TrackMock.TrackMockBuilder().setId("track2").setRootGroup(this.group2).build();
        VariableSection section;
        VariableSection.VariableSectionBuilder sectionBuilder = new VariableSection.VariableSectionBuilder();
        sectionBuilder.setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(track1);
        sectionBuilder.addTrack(track2);
        section = sectionBuilder.build();
        
        track1.addMessage(false, false);
        track1.addMessage(false, false);
        track1.addMessage(true, false);
        
        track2.addMessage(false, false);
        track2.addMessage(true, false);
        
        section.initialize(this.random, 100);
        
        this.pattern1.resetExecutions();
        this.pattern2.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(3, this.pattern1.getExecutions()); // 300 - 500 - 700!
        assertEquals(2, this.pattern2.getExecutions()); // 400 - 700!
        assertEquals(700, section.getActualEnd());
        assertEquals(7, notes.size());
    }
    
    @Test
    public void testExecuteVariableSectionThreeTracksFinished() {
        TrackMock track1 = (TrackMock) new TrackMock.TrackMockBuilder().setId("track1").setRootGroup(this.group1).build();
        TrackMock track2 = (TrackMock) new TrackMock.TrackMockBuilder().setId("track2").setRootGroup(this.group2).build();
        TrackMock track3 = (TrackMock) new TrackMock.TrackMockBuilder().setId("track3").setRootGroup(this.group3).build();
        VariableSection section;
        VariableSection.VariableSectionBuilder sectionBuilder = new VariableSection.VariableSectionBuilder();
        sectionBuilder.setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(track1);
        sectionBuilder.addTrack(track2);
        sectionBuilder.addTrack(track3);
        section = sectionBuilder.build();
        
        track1.addMessage(false, false);
        track1.addMessage(true, false);
        
        track2.addMessage(false, false);
        track2.addMessage(true, false);
        
        track3.addMessage(true, false);
        
        section.initialize(this.random, 0);
        
        this.pattern1.resetExecutions();
        this.pattern2.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(4, this.pattern1.getExecutions()); // 200 - 400! - 600 - 800
        assertEquals(3, this.pattern2.getExecutions()); // 300 - 600! - 900
        assertEquals(1, this.pattern3.getExecutions()); // 700!
        assertEquals(900, section.getActualEnd());
        assertEquals(13, notes.size());
    }
    
    @Test
    public void testExecuteVariableSectionOneTrackInterrupt() {
        TrackMock track1 = (TrackMock) new TrackMock.TrackMockBuilder().setRootGroup(this.group1).setId("interruptTrack").build();
        VariableSection section;
        VariableSection.VariableSectionBuilder sectionBuilder = new VariableSection.VariableSectionBuilder();
        sectionBuilder.setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(track1);
        section = sectionBuilder.build();
        track1.addMessage(false, false);
        track1.addMessage(false, false);
        track1.addMessage(false, true);
        
        section.initialize(this.random, 100);
        
        this.pattern1.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(3, this.pattern1.getExecutions()); // 200 - 400 - 600!!
        assertEquals(700, section.getActualEnd());
        assertEquals(3, notes.size());
    }
    
    @Test
    public void testExecuteVariableSectionTwoTracksInterrupt() {
        TrackMock track1 = (TrackMock) new TrackMock.TrackMockBuilder().setRootGroup(this.group1).build();
        TrackMock track2 = (TrackMock) new TrackMock.TrackMockBuilder().setRootGroup(this.group2).build();
        VariableSection section;
        VariableSection.VariableSectionBuilder sectionBuilder = new VariableSection.VariableSectionBuilder();
        sectionBuilder.setId("sectionTest").setTempo(100);
        sectionBuilder.addTrack(track1);
        sectionBuilder.addTrack(track2);
        section = sectionBuilder.build();
        track1.addMessage(false, false);
        track1.addMessage(false, false);
        track1.addMessage(false, false);
        track1.addMessage(false, true);
        
        track2.addMessage(false, false);
        track2.addMessage(false, false);
        track2.addMessage(false, false);
        
        section.initialize(this.random, 100);
        
        this.pattern1.resetExecutions();
        this.pattern2.resetExecutions();
        MIDINoteList notes = section.execute(this.random);
        
        assertEquals(4, this.pattern1.getExecutions()); // 300 - 500 - 700 - 900!!
        assertEquals(3, this.pattern2.getExecutions()); // 400 - 700 - 1000
        assertEquals(1000, section.getActualEnd());
        assertEquals(10, notes.size());
    }
}
