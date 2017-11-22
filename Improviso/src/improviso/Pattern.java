package improviso;

import java.util.*;

/**
 * The definition of a fragment of music to be played within a composition. A
 * pattern consists of a list of Notes and a length, being that this length can
 * be a range of possible values. Once a pattern is initialized, it chooses a
 * length among this range, and when it's executed, it produces a list of
 * MIDINotes according to the notes included in it, as well as the length chosen
 * at initialization.
 * @author Fernie Canto
 */
public class Pattern {
    private final String id;
    private final ArrayList<Note> notes;
    private final IntegerRange duration;
    
    private Integer currentDuration;
    
    /**
     * The builder class for the pattern. Patterns are created exclusively
     * through this class, and all its attributes must be set through the
     * adequate accessors.
     */
    public static class PatternBuilder {
        private String id;
        private IntegerRange duration;
        private final ArrayList<Note> notes = new ArrayList<>();

        public String getId() {
            return id;
        }

        /**
         * Sets an identification for the pattern. The id can be any string of
         * characters.
         * @param id The identification of the pattern.
         * @return The builder object
         */
        public PatternBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public IntegerRange getDuration() {
            return duration;
        }

        /**
         * Sets the range of lengths for the pattern, in ticks. Every time the
         * pattern is initialized before execution, its actual length is chosen
         * from this range.
         * @param duration
         * @return The builder object
         */
        public PatternBuilder setDuration(IntegerRange duration) {
            this.duration = duration;
            return this;
        }
        
        public ArrayList<Note> getNotes() {
            return this.notes;
        }
        
        /**
         * Adds a Note to the pattern.
         * @param note The Note to be added
         * @return The builder object
         */
        public PatternBuilder addNote(Note note) {
            this.notes.add(note);
            return this;
        }
        
        /**
         * Builds the Pattern object according to the settings defined in this
         * object.
         * @return The resulting Pattern object
         */
        public Pattern build() {
            return new Pattern(this);
        }
    }
    
    protected Pattern(PatternBuilder builder) {
        this.id = builder.getId();
        this.duration = builder.getDuration();
        this.notes = builder.getNotes();
    }
    
   /**
    * Gets the identification string of the pattern.
    * @return The identification string
    */
    public String getId() {
        return this.id;
    }

    /**
     * Chooses a length for the next execution of this pattern. This method must
     * be invoked before every execution.
     * @param random The Random object
     */
    public void initialize(Random random) {
        this.currentDuration = this.duration.getValue(random);
    }
    
    /**
     * Produces a list of MIDINote objects according to the notes included in
     * the pattern and the last length defined during initialization. It's
     * possible to forcibly reduce the length of the pattern by specifying a
     * length in ticks. In this case, the resulting MIDINotes may be cut short
     * or removed from the pattern. The positions of the resulting notes are in
     * relation to the Pattern, which always starts at 0.
     * @param random The random object
     * @param finalPosition The position of the end of this pattern in relation
     * to the Section that's being executed.
     * @param length The maximum length for the pattern as imposed by the
     * Section.
     * @return The list of resulting MIDINotes.
     */
    public MIDINoteList execute(Random random, double finalPosition, Integer length) {
        MIDINoteList noteList = new MIDINoteList();
        this.notes.forEach((note) -> {
            noteList.addAll(
                    note.execute(
                            random,
                            this.currentDuration,
                            finalPosition, 
                            length != null ? length : Integer.MAX_VALUE
                    ));
        });

        return noteList;
    }

    /**
     * Gets the last length defined during initialization, in ticks.
     * @return The length in ticks
     */
    public int getLength() {
        return this.currentDuration;
    }
}
