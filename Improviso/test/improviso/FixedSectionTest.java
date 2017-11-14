/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.IntegerRangeMock;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class FixedSectionTest {
    @Before
    public void SetUp() {
        
    }
    
    @Test
    public void testSequenceGroupFinishedSignal() {
        FixedSection section;
        FixedSection.FixedSectionBuilder builder = new FixedSection.FixedSectionBuilder();
        builder.setLength(new IntegerRangeMock(500)).setId("sectionTest").setTempo(100);
        section = builder.build();
        
        assertNotNull(section);
    }
}
