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
public class NoteTest {
    @Test
    public void testInterpretNoteNames() throws ImprovisoException {
        ElementLibrary library = new ElementLibrary();
        library.addNoteAlias("alias", 15);
        
        long note1 = Note.interpretNoteName(library, "D#2");
        assertEquals(51, note1);
        
        long note2 = Note.interpretNoteName(library, "Gb1");
        assertEquals(42, note2);
        
        long note3 = Note.interpretNoteName(library, "C-2");
        assertEquals(0, note3);
        
        long note4 = Note.interpretNoteName(library, "50");
        assertEquals(50, note4);
        
        long note5 = Note.interpretNoteName(library, "alias");
        assertEquals(15, note5);
    }
    
    @Test(expected = ImprovisoException.class)
    public void testInterpretNoteError() throws ImprovisoException {
        Note.interpretNoteName(new ElementLibrary(), "erro");
    }
    
    /**
     * Test a note that's not generated due to the probability.
     */
    @Test
    public void testProbabilityBelowNote() {
        Note noteDef = new Note.NoteBuilder()
                .setMIDITrack(2)
                .setPitch(30)
                .setProbability(0.5)
                .build();
        RandomMock random = new RandomMock();
        random.addDouble(0.9);
        MIDINoteList resultNoNote = noteDef.execute(random, 500, 0.5, Integer.MAX_VALUE);
        assertTrue(resultNoNote.isEmpty());
    }
    
    @Test
    public void testNoteAfterMaximumLength() {
        Note noteDef = new Note.NoteBuilder()
                .setMIDITrack(1)
                .setPitch(10)
                .setStart(new IntegerRangeMock(50))
                .setLength(new IntegerRangeMock(50))
                .build();
        RandomMock random = new RandomMock();
        MIDINoteList resultNoNote = noteDef.execute(random, 100, 1, 49);
        assertTrue(resultNoNote.isEmpty());
    }

    /**
     * Test a note with fixed start and length.
     */
    @Test
    public void testGenerateFixedNote() {
        Note noteDef = new Note.NoteBuilder()
                .setMIDITrack(2)
                .setPitch(30)
                .setStart(new IntegerRangeMock(20))
                .setLength(new IntegerRangeMock(50))
                .setVelocity(new IntegerRangeMock(10))
                .build();
        
        RandomMock rand = new RandomMock();
        rand.addDouble(0.1);
        MIDINoteList result = noteDef.execute(rand, 55, 0.5, 55);
        assertFalse(result.isEmpty());
        assertEquals(30, result.get(0).getPitch());
        assertEquals(10, result.get(0).getVelocity());
        assertEquals(20, result.get(0).getStart());
        assertEquals(35, result.get(0).getLength());
        assertEquals(2, result.get(0).getMIDITrack());
    }
    
    /**
     * Test a note with relative start and length
     */
    @Test
    public void testGenerateRelativeNote() {
        Note noteDef = new Note.NoteBuilder()
                .setPitch(30)
                .setStart(new DoubleRangeMock(.4))
                .setLength(new DoubleRangeMock(.25))
                .setVelocity(new IntegerRangeMock(10))
                .build();
        
        RandomMock rand = new RandomMock();
        rand.addDouble(0.1);
        MIDINoteList result = noteDef.execute(rand, 200, 0.5, Integer.MAX_VALUE);
        assertFalse(result.isEmpty());
        assertEquals(30, result.get(0).getPitch());
        assertEquals(10, result.get(0).getVelocity());
        assertEquals(80, result.get(0).getStart());
        assertEquals(50, result.get(0).getLength());
    }
    
    @Test
    public void testGenerateTransposedNote() {
        Note noteDef = new Note.NoteBuilder()
                .setPitch(30)
                .setTransposition(new IntegerRangeMock(2))
                .setStart(new IntegerRangeMock(0))
                .setLength(new IntegerRangeMock(10))
                .setVelocity(new IntegerRangeMock(10))
                .build();
        MIDINoteList result = noteDef.execute(new RandomMock(), 200, 0.5, Integer.MAX_VALUE);
        assertFalse(result.isEmpty());
        assertEquals(32, result.get(0).getPitch());
    }
}
