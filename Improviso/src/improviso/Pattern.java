package improviso;

import java.util.*;

/**
 *
 * @author Fernie Canto
 */
public class Pattern {
    private final String id;
    private final ArrayList<NoteDefinition> noteDefinitions;
    private final IntegerRange duration;
    
    private Integer currentDuration;
    
    public static class PatternBuilder {
        private String id;
        private IntegerRange duration;
        private final ArrayList<NoteDefinition> noteDefinitions = new ArrayList<>();

        public String getId() {
            return id;
        }

        public PatternBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public IntegerRange getDuration() {
            return duration;
        }

        public PatternBuilder setDuration(IntegerRange duration) {
            this.duration = duration;
            return this;
        }
        
        public ArrayList<NoteDefinition> getNoteDefinitions() {
            return this.noteDefinitions;
        }
        
        public PatternBuilder addNoteDefinition(NoteDefinition noteDef) {
            this.noteDefinitions.add(noteDef);
            return this;
        }
        
        public Pattern build() {
            return new Pattern(this);
        }
    }
    
    protected Pattern(PatternBuilder builder) {
        this.id = builder.getId();
        this.duration = builder.getDuration();
        this.noteDefinitions = builder.getNoteDefinitions();
    }
    
   
    public String getId() {
        return this.id;
    }

    public void initialize(Random random) {
        this.currentDuration = this.duration.getValue(random);
    }
    
    public ArrayList<MIDINote> execute(Random rand, int start, double initialPosition, double finalPosition, Integer length) {
        ArrayList<MIDINote> noteList = new ArrayList<>();
        this.noteDefinitions.forEach((noteDef) -> {
            noteList.add(noteDef.generateNote(rand, start, this.currentDuration, finalPosition, length != null ? length : Integer.MAX_VALUE));
        });

        return noteList;
    }

    public int getLength() {
        return this.currentDuration;
    }
}
