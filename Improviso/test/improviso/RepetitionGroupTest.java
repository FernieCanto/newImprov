/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import improviso.mocks.IntegerRangeMock;
import improviso.mocks.PatternMock;
import improviso.mocks.RandomMock;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author User
 */
public class RepetitionGroupTest {
    private Pattern pattern1;
    private Pattern pattern2;
        
    @Before
    public void setUp() {
        PatternMock.PatternMockBuilder patternBuilder1 = new PatternMock.PatternMockBuilder();
        PatternMock.PatternMockBuilder patternBuilder2 = new PatternMock.PatternMockBuilder();
        
        patternBuilder1.setId("pattern1").setDuration(new IntegerRangeMock(100));
        pattern1 = patternBuilder1.build();
        patternBuilder2.setId("pattern2").setDuration(new IntegerRangeMock(100));
        pattern2 = patternBuilder2.build();
    }
    
    @Test
    public void testSequenceGroupFinishedSignal() {
        RandomMock random = new RandomMock();
        LeafGroup leafGroup1;
        LeafGroup leafGroup2;
        
        LeafGroup.LeafGroupBuilder leafBuilder1 = new LeafGroup.LeafGroupBuilder();
        leafBuilder1.setId("leafGroup1").setFinishedSignal(new GroupSignal(5, 5, 1.0));
        leafGroup1 = leafBuilder1.setLeafPattern(pattern1).build();
        
        LeafGroup.LeafGroupBuilder leafBuilder2 = new LeafGroup.LeafGroupBuilder();
        leafBuilder2.setId("leafGroup2").setFinishedSignal(new GroupSignal(1, 1, 1.0));
        leafGroup2 = leafBuilder2.setLeafPattern(pattern2).build();
        
        SequenceGroup.SequenceGroupBuilder seqBuilder = new SequenceGroup.SequenceGroupBuilder();
        SequenceGroup seqGroup;
        seqBuilder.setId("seqGroup");
        seqBuilder.addChild(leafGroup1, null, null).addChild(leafGroup2, null, null);
        seqGroup = seqBuilder.build();
        
        GroupMessage message;
        seqGroup.execute(random);
        message = seqGroup.getMessage();
        assertFalse(message.signal);
        assertFalse(message.finish);
        
        seqGroup.execute(random);
        message = seqGroup.getMessage();
        assertTrue(message.signal);
        assertFalse(message.finish);
    }
    
    @Test
    public void testSequenceGroupInterruptSignal() {
        RandomMock random = new RandomMock();
        LeafGroup leafGroup1;
        LeafGroup leafGroup2;
        
        LeafGroup.LeafGroupBuilder leafBuilder1 = new LeafGroup.LeafGroupBuilder();
        leafBuilder1.setId("leafGroup1").setInterruptSignal(new GroupSignal(5, 5, 1.0));
        leafGroup1 = leafBuilder1.setLeafPattern(pattern1).build();
        
        LeafGroup.LeafGroupBuilder leafBuilder2 = new LeafGroup.LeafGroupBuilder();
        leafBuilder2.setId("leafGroup2").setInterruptSignal(new GroupSignal(1, 1, 1.0));
        leafGroup2 = leafBuilder2.setLeafPattern(pattern2).build();
        
        SequenceGroup.SequenceGroupBuilder seqBuilder = new SequenceGroup.SequenceGroupBuilder();
        SequenceGroup seqGroup;
        seqBuilder.setId("seqGroup");
        seqBuilder.addChild(leafGroup1, null, null).addChild(leafGroup2, null, null);
        seqGroup = seqBuilder.build();
        
        GroupMessage message;
        seqGroup.execute(random);
        message = seqGroup.getMessage();
        assertFalse(message.signal);
        assertFalse(message.finish);
        
        seqGroup.execute(random);
        message = seqGroup.getMessage();
        assertFalse(message.signal);
        assertTrue(message.finish);
    }
}
