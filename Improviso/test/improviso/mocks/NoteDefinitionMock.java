/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso.mocks;

import improviso.*;
import java.util.Random;

/**
 *
 * @author fernando
 */
public class NoteDefinitionMock extends NoteDefinition {
    private MIDINote note;
    
    public static class NoteDefinitionMockBuilder extends NoteDefinitionBuilder {
        @Override
        public NoteDefinitionMock build() {
            return new NoteDefinitionMock(this);
        }
    }
    
    public NoteDefinitionMock(NoteDefinition.NoteDefinitionBuilder builder) {
        super(builder);
    }
    
    public void setNote(MIDINote note) {
        this.note = note;
    }
    
    @Override
    public MIDINote generateNote(Random rand, int start, int patternLength, double position, int maximumLength) {
        return new MIDINote(
                note.getPitch(),
                note.getStart() + start,
                note.getStart() + note.getLength() > maximumLength
                        ? maximumLength - note.getStart()
                        : note.getLength(),
                note.getVelocity(),
                note.getMIDITrack()
        );
    }
}
