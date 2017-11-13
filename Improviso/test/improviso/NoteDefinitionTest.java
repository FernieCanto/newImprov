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

    /**
     * Test of interpretNoteName method, of class NoteDefinition.
     *
    @Test
    public void testInterpretNoteName() throws Exception {
        System.out.println("interpretNoteName");
        ElementLibrary library = null;
        String stringNoteName = "";
        int expResult = 0;
        int result = NoteDefinition.interpretNoteName(library, stringNoteName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    } */
    
    /**
     * Test a note that's not generated due to the probability.
     */
    @Test
    public void testNotGenerateNote() {
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
                .setPitch(30)
                .setProbability(0.5)
                .build();
        RandomMock rand = new RandomMock();
        rand.addDouble(0.9);
        MIDINote resultNoNote = noteDef.generateNote(rand, 50, 500, 0.5, null);
        assertNull(resultNoNote);
    }

    /**
     * Test a note with fixed start and length.
     */
    @Test
    public void testGenerateFixedNote() {
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
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
        MIDINote result = noteDef.generateNote(rand, 0, 200, 0.5, null);
        assertNotNull(result);
        assertEquals(30, result.getPitch());
        assertEquals(10, result.getVelocity());
        assertEquals(80, result.getStart());
        assertEquals(50, result.getLength());
    }
}
