/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.GroupSignalMock;
import improviso.mocks.IntegerRangeMock;
import improviso.mocks.PatternMock;
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
public class LeafGroupTest {
    
    public LeafGroupTest() {
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
     * Test of generateGroupXML method, of class Group.
     */
    @Test
    public void testBuildLeafGroup() {
        PatternMock.PatternMockBuilder patternBuilder = new PatternMock.PatternMockBuilder();
        PatternMock pattern;
        LeafGroup.LeafGroupBuilder builder = new LeafGroup.LeafGroupBuilder();
        LeafGroup group;
        RandomMock random = new RandomMock();
        
        pattern = patternBuilder.build();
        patternBuilder.setId("test").setDuration(new IntegerRangeMock(100));
        builder.setFinishedSignal(new GroupSignalMock())
                .setInterruptSignal(new GroupSignalMock());
        group = builder.setLeafPattern(pattern).build();
        assertNotNull(group);
        assertEquals(pattern, group.selectPattern(random));
    }
}
