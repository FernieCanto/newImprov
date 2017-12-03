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
        
        Pattern.PatternExecution execution = pattern.initialize(new RandomMock());
        assertEquals(100, execution.getLength());
        
        MIDINoteList notes = execution.execute(new RandomMock(), 1, null);
        assertTrue(notes.isEmpty());
    }
    
    @Test
    public void testExecutePattern() {
        NoteMock def1 = new NoteMock.NoteMockBuilder().build();
        def1.setNote(new MIDINote(10, 0, 50, 100, 1));
        NoteMock def2 = new NoteMock.NoteMockBuilder().build();
        def2.setNote(new MIDINote(20, 50, 50, 100, 1));
        Pattern pattern = new Pattern.PatternBuilder()
                .setId("pattern1")
                .setDuration(new improviso.mocks.IntegerRangeMock(100))
                .addNote(def1)
                .addNote(def2)
                .build();
        
        assertNotNull(pattern);
        Pattern.PatternExecution execution = pattern.initialize(new RandomMock());
        assertEquals(100, execution.getLength());
        
        MIDINoteList notes = execution.execute(new RandomMock(), 0, 99);
        assertEquals(2, notes.size());
        
        assertEquals(10, notes.get(0).getPitch());
        assertEquals(0, notes.get(0).getStart());
        assertEquals(50, notes.get(0).getLength());
        assertEquals(100, notes.get(0).getVelocity());
        assertEquals(1, notes.get(0).getMIDITrack());
        
        assertEquals(20, notes.get(1).getPitch());
        assertEquals(50, notes.get(1).getStart());
        assertEquals(49, notes.get(1).getLength());
        assertEquals(100, notes.get(1).getVelocity());
        assertEquals(1, notes.get(1).getMIDITrack());
    }
    
    @Test
    public void testExecutePatternInterrupt() {
        NoteMock def1 = new NoteMock.NoteMockBuilder().build();
        def1.setNote(new MIDINote(10, 0, 50, 100, 1));
        NoteMock def2 = new NoteMock.NoteMockBuilder().build();
        def2.setNote(new MIDINote(20, 50, 50, 100, 1));
        Pattern pattern = new Pattern.PatternBuilder()
                .setId("pattern1")
                .setDuration(new improviso.mocks.IntegerRangeMock(100))
                .addNote(def1)
                .addNote(def2)
                .build();
        
        Pattern.PatternExecution execution = pattern.initialize(new RandomMock());
        
        MIDINoteList notes = execution.execute(new RandomMock(), 0, 49);
        assertEquals(1, notes.size());
        
        assertEquals(10, notes.get(0).getPitch());
        assertEquals(0, notes.get(0).getStart());
        assertEquals(49, notes.get(0).getLength());
        assertEquals(100, notes.get(0).getVelocity());
        assertEquals(1, notes.get(0).getMIDITrack());
    }
}
