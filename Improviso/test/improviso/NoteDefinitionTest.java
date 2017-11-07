/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.util.Random;
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
    } /

    /**
     * Test of generateNote method, of class NoteDefinition.
     */
    @Test
    public void testGenerateNote() {
        System.out.println("generateNote");
        
        NoteDefinition noteDef = new NoteDefinition.NoteDefinitionBuilder()
                .setPitch(30)
                .setStart(new IntegerRange(0, 100, 0, 100))
                .setLength(new IntegerRange(0, 100, 0, 100))
                .setVelocity(new IntegerRange(0, 100, 0, 100))
                .setProbability(0.5)
                .build();
        RandomMock rand = new RandomMock();
        rand.addDouble(0.9);
        Note resultNoNote = noteDef.generateNote(rand, 50, 500, 0.5, null);
        assertNull(resultNoNote);
        
        rand.addDouble(0.1);
        rand.addInteger(10); // Velocity
        rand.addInteger(20); // Start (+50 for start of pattern = 70)
        rand.addInteger(50); // Length (end of note = 70)
        Note result = noteDef.generateNote(rand, 50, 55, 0.5, 55);
        assertNotNull(result);
        assertEquals(30, result.pitch);
        assertEquals(10, result.velocity);
        assertEquals(70, result.start);
        assertEquals(35, result.length);
    }
    
}
