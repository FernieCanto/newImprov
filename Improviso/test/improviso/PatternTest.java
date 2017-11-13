/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.IntegerRangeMock;
import improviso.mocks.NoteDefinitionMock;
import improviso.mocks.RandomMock;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author fernando
 */
public class PatternTest {
    
    public PatternTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Pattern.
     */
    @Test
    public void testExecuteEmptyPattern() {
        Pattern pattern = new Pattern.PatternBuilder()
                .setId("empty")
                .setDuration(new improviso.mocks.IntegerRangeMock(100))
                .build();
        
        assertNotNull(pattern);
        assertEquals("empty", pattern.getId());
        
        pattern.initialize(new RandomMock());
        assertEquals(100, pattern.getLength());
        
        ArrayList<MIDINote> notes = pattern.execute(new RandomMock(), 0, 0, 1, null);
        assertTrue(notes.isEmpty());
    }
    
    @Test
    public void testExecutePattern() {
        NoteDefinitionMock def1 = new NoteDefinitionMock.NoteDefinitionMockBuilder().build();
        def1.setNote(new MIDINote(10, 0, 50, 100, 1));
        NoteDefinitionMock def2 = new NoteDefinitionMock.NoteDefinitionMockBuilder().build();
        def2.setNote(new MIDINote(20, 50, 50, 100, 1));
        Pattern pattern = new Pattern.PatternBuilder()
                .setId("pattern1")
                .setDuration(new improviso.mocks.IntegerRangeMock(100))
                .addNoteDefinition(def1)
                .addNoteDefinition(def2)
                .build();
        
        assertNotNull(pattern);
        pattern.initialize(new RandomMock());
        assertEquals(100, pattern.getLength());
        
        ArrayList<MIDINote> notes = pattern.execute(new RandomMock(), 15, 0, 0, 99);
        assertEquals(2, notes.size());
        
        assertEquals(10, notes.get(0).getPitch());
        assertEquals(15, notes.get(0).getStart());
        assertEquals(50, notes.get(0).getLength());
        assertEquals(100, notes.get(0).getVelocity());
        assertEquals(1, notes.get(0).getMIDITrack());
        
        assertEquals(20, notes.get(1).getPitch());
        assertEquals(65, notes.get(1).getStart());
        assertEquals(49, notes.get(1).getLength());
        assertEquals(100, notes.get(1).getVelocity());
        assertEquals(1, notes.get(1).getMIDITrack());
    }
}
