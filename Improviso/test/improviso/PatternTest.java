/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

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
                .setDuration(new IntegerRangeMock(100))
                .build();
        
        assertNotNull(pattern);
        assertEquals("empty", pattern.getId());
        
        pattern.initialize();
        assertEquals(100, pattern.getLength());
        
        ArrayList<Note> notes = pattern.execute(0, 0, 1, null);
        assertTrue(notes.isEmpty());
    }
    
}
