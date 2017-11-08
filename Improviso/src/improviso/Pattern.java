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
    
    private Pattern(PatternBuilder builder) {
        this.id = builder.getId();
        this.duration = builder.getDuration();
        this.noteDefinitions = builder.getNoteDefinitions();
    }
    
   
    public String getId() {
        return this.id;
    }

    public void initialize() {
        this.currentDuration = this.duration.getValue();
    }

    /**
     * Generates a list of notes according to the note definitions in the pattern.
     * @param start The place in the composition where the pattern is being executed, in ticks
     * @param initialPosition ???
     * @param finalPosition ???
     * @param length The maximum length of this pattern's execution
     * @return The resulting notes
     */
    public ArrayList<Note> execute(int start, double initialPosition, double finalPosition, Integer length) {
        ArrayList<Note> noteList = new ArrayList<>();
        Random rand = new Random();
        this.noteDefinitions.forEach((def) -> {
            noteList.add(def.generateNote(rand, start, this.currentDuration, finalPosition, length));
        });

        return noteList;
    }

    public int getLength() {
        return this.currentDuration;
    }
}
