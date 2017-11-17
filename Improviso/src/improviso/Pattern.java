package improviso;

import java.util.*;

/**
 *
 * @author Fernie Canto
 */
public class Pattern {
    private final String id;
    private final ArrayList<Note> notes;
    private final IntegerRange duration;
    
    private Integer currentDuration;
    
    public static class PatternBuilder {
        private String id;
        private IntegerRange duration;
        private final ArrayList<Note> noteDefinitions = new ArrayList<>();

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
        
        public ArrayList<Note> getNotes() {
            return this.noteDefinitions;
        }
        
        public PatternBuilder addNote(Note noteDef) {
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
        this.notes = builder.getNotes();
    }
    
   
    public String getId() {
        return this.id;
    }

    public void initialize(Random random) {
        this.currentDuration = this.duration.getValue(random);
    }
    
    public MIDINoteList execute(Random rand, double finalPosition, Integer length) {
        MIDINoteList noteList = new MIDINoteList();
        this.notes.forEach((note) -> {
            noteList.addAll(
                    note.execute(
                            rand,
                            this.currentDuration,
                            finalPosition, 
                            length != null ? length : Integer.MAX_VALUE
                    ));
        });

        return noteList;
    }

    public int getLength() {
        return this.currentDuration;
    }
}
