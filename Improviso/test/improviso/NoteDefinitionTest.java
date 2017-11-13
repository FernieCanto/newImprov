/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.DoubleRangeMock;
import improviso.mocks.IntegerRangeMock;
import improviso.mocks.RandomMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class NoteDefinitionTest {
    
    public NoteDefinitionTest() {
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
    
    @Test
    public void testInterpretNoteNames() throws ImprovisoException {
        ElementLibrary library = new ElementLibrary();
        library.addNoteAlias("alias", 15);
        
        long note1 = NoteDefinition.interpretNoteName(library, "D#2");
        assertEquals(51, note1);
        
        long note2 = NoteDefinition.interpretNoteName(library, "Gb1");
        assertEquals(42, note2);
        
        long note3 = NoteDefinition.interpretNoteName(library, "C-2");
        assertEquals(0, note3);
        
        long note4 = NoteDefinition.interpretNoteName(library, "50");
        assertEquals(50, note4);
        
        long note5 = NoteDefinition.interpretNoteName(library, "alias");
        assertEquals(15, note5);
    }
    
    @Test(expected = ImprovisoException.class)
    public void testInterpretNoteError() throws ImprovisoException {
        long noteErro = NoteDefinition.interpretNoteName(new ElementLibrary(), "erro");
    }
    
    /**
     * Test a note that's not generated due to the probability.
     */
    @Test
    public void testNotGenerateNote() {
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
                .setMIDITrack(2)
                .setPitch(30)
                .setProbability(0.5)
                .build();
        RandomMock rand = new RandomMock();
        rand.addDouble(0.9);
        MIDINote resultNoNote = noteDef.generateNote(rand, 50, 500, 0.5, Integer.MAX_VALUE);
        assertNull(resultNoNote);
    }

    /**
     * Test a note with fixed start and length.
     */
    @Test
    public void testGenerateFixedNote() {
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
                .setMIDITrack(2)
                .setPitch(30)
                .setStart(new IntegerRangeMock(20))
                .setLength(new IntegerRangeMock(50))
                .setVelocity(new IntegerRangeMock(10))
                .build();
        
        RandomMock rand = new RandomMock();
        rand.addDouble(0.1);
        MIDINote result = noteDef.generateNote(rand, 50, 55, 0.5, 55);
        assertNotNull(result);
        assertEquals(30, result.getPitch());
        assertEquals(10, result.getVelocity());
        assertEquals(70, result.getStart());
        assertEquals(35, result.getLength());
        assertEquals(2, result.getMIDITrack());
    }
    
    /**
     * Test a note with relative start and length
     */
    @Test
    public void testGenerateRelativeNote() {
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
                .setPitch(30)
                .setRelativeStart(new DoubleRangeMock(.4))
                .setRelativeLength(new DoubleRangeMock(.25))
                .setVelocity(new IntegerRangeMock(10))
                .build();
        
        RandomMock rand = new RandomMock();
        rand.addDouble(0.1);
        MIDINote result = noteDef.generateNote(rand, 0, 200, 0.5, Integer.MAX_VALUE);
        assertNotNull(result);
        assertEquals(30, result.getPitch());
        assertEquals(10, result.getVelocity());
        assertEquals(80, result.getStart());
        assertEquals(50, result.getLength());
    }
    
    @Test
    public void testGenerateTransposedNote() {
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
                .setPitch(30)
                .setTransposition(new IntegerRangeMock(2))
                .setStart(new IntegerRangeMock(0))
                .setLength(new IntegerRangeMock(10))
                .setVelocity(new IntegerRangeMock(10))
                .build();
        MIDINote result = noteDef.generateNote(new RandomMock(), 0, 200, 0.5, Integer.MAX_VALUE);
        assertNotNull(result);
        assertEquals(32, result.getPitch());
    }
}
