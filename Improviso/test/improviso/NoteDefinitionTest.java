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
import org.w3c.dom.Element;

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
     */
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
    }

    /**
     * Test of generateNoteDefinitionXML method, of class NoteDefinition.
     */
    @Test
    public void testGenerateNoteDefinitionXML() throws Exception {
        System.out.println("generateNoteDefinitionXML");
        ElementLibrary library = null;
        Element element = null;
        NoteDefinition expResult = null;
        NoteDefinition result = XMLCompositionParser.generateNoteDefinitionXML(library, element);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransposedPitch method, of class NoteDefinition.
     */
    @Test
    public void testGetTransposedPitch() {
        System.out.println("getTransposedPitch");
        Random rand = null;
        NoteDefinition instance = null;
        int expResult = 0;
        int result = instance.getTransposedPitch(rand);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of generateNote method, of class NoteDefinition.
     */
    @Test
    public void testGenerateNote() {
        System.out.println("generateNote");
        Random rand = null;
        int start = 0;
        int patternLength = 0;
        double position = 0.0;
        Integer maximumLength = null;
        NoteDefinition instance = null;
        Note expResult = null;
        Note result = instance.generateNote(rand, start, patternLength, position, maximumLength);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
