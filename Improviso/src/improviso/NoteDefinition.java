package improviso;

import java.util.Random;
import java.util.regex.*;

/**
 * Armazena uma definição de uma pitch componente de um padrão.
 * @author fernando
 */
public class NoteDefinition {
    final int pitch;
    final int MIDITrack;
    
    final protected IntegerRange start;
    final protected IntegerRange length;
    final protected IntegerRange velocity;
    
    final protected DoubleRange relativeStart;
    final protected DoubleRange relativeLength;
    final protected double probability;
    
    final protected IntegerRange transposition;
    
    final protected static java.util.regex.Pattern NOTE_NAME_PATTERN = java.util.regex.Pattern.compile("^([A-G])([#b])?(-1|\\d)$");
    final protected static java.util.regex.Pattern INTERVAL_PATTERN = java.util.regex.Pattern.compile("^(\\d+)(-(\\d+))?(\\|(\\d+)(-(\\d+))?)?$");
    
    public static class NoteDefinitionBuilder {
        private int pitch;
        private int MIDITrack = 1;
        private IntegerRange start = new IntegerRange(0, 0, 0, 0);
        private IntegerRange length = new IntegerRange(60, 60, 60, 60);
        private IntegerRange velocity = new IntegerRange(100, 100, 100, 100);
        private DoubleRange relativeStart = null;
        private DoubleRange relativeLength = null;
        private double probability = 1.0;
        private IntegerRange transposition = null;
        
        public int getPitch() {
            return pitch;
        }

        public NoteDefinitionBuilder setPitch(int pitch) {
            this.pitch = pitch;
            return this;
        }

        public int getMIDITrack() {
            return MIDITrack;
        }

        public NoteDefinitionBuilder setMIDITrack(int MIDITrack) {
            this.MIDITrack = MIDITrack;
            return this;
        }
        
        public IntegerRange getStart() {
            return start;
        }
        
        public NoteDefinitionBuilder setStart(IntegerRange start) {
            this.start = start;
            if (start != null) {
                this.relativeStart = null;
            }
            return this;
        }

        public DoubleRange getRelativeStart() {
            return relativeStart;
        }
        
        public NoteDefinitionBuilder setRelativeStart(DoubleRange relativeStart) {
            this.relativeStart = relativeStart;
            if (relativeStart != null) {
                this.start = null;
            }
            return this;
        }

        public IntegerRange getLength() {
            return length;
        }

        public NoteDefinitionBuilder setLength(IntegerRange length) {
            this.length = length;
            if (length != null) {
                this.relativeLength = null;
            }
            return this;
        }

        public DoubleRange getRelativeLength() {
            return relativeLength;
        }

        public NoteDefinitionBuilder setRelativeLength(DoubleRange relativeLength) {
            this.relativeLength = relativeLength;
            if (relativeLength != null) {
                this.length = null;
            }
            return this;
        }

        public IntegerRange getVelocity() {
            return velocity;
        }

        public NoteDefinitionBuilder setVelocity(IntegerRange velocity) {
            this.velocity = velocity;
            return this;
        }

        public double getProbability() {
            return probability;
        }

        public NoteDefinitionBuilder setProbability(double probability) {
            this.probability = probability;
            return this;
        }

        public IntegerRange getTransposition() {
            return transposition;
        }

        public NoteDefinitionBuilder setTransposition(IntegerRange transposition) {
            this.transposition = transposition;
            return this;
        }
        
        public NoteDefinition build()
        {
            return new NoteDefinition(this);
        }
    }
    
    NoteDefinition(NoteDefinitionBuilder builder) {
        this.pitch = builder.getPitch();
        this.MIDITrack = builder.getMIDITrack();
        this.start = builder.getStart();
        this.length = builder.getLength();
        this.relativeStart = builder.getRelativeStart();
        this.relativeLength = builder.getRelativeLength();
        this.velocity = builder.getVelocity();
        this.probability = builder.getProbability();
        this.transposition = builder.getTransposition();
    }
    
    /**
     * Produces the MIDI note number associated with the note name
     * @param library
     * @param stringNoteName
     * @return
     * @throws ImprovisoException 
     */
    public static int interpretNoteName(ElementLibrary library, String stringNoteName)
        throws ImprovisoException {
        int note = 0;
        Matcher m = NOTE_NAME_PATTERN.matcher(stringNoteName);
        
        if(library.hasNoteAlias(stringNoteName)) {
            return library.getNoteAlias(stringNoteName);
        }
        
        if(m.matches()) {
            switch(m.group(1).charAt(0)) {
                case 'C': note =  0; break;
                case 'D': note =  2; break;
                case 'E': note =  4; break;
                case 'F': note =  5; break;
                case 'G': note =  7; break;
                case 'A': note =  9; break;
                case 'B': note = 11; break;
            }
            if(m.group(2) != null) {
                if(m.group(2).equals("b"))
                    note--;
                else
                    note++;
            }

            if(!m.group(3).equals("-1")) {
                int octave = Integer.parseInt(m.group(3));
                note += (octave+1) * 12;
            }
            return note;
        }
        
        try {
            return Integer.parseInt(stringNoteName);
        }
        catch(NumberFormatException e) {
            ImprovisoException exception = new ImprovisoException("Invalid note name: "+stringNoteName);
            exception.addSuppressed(e);
            throw exception;
        }
    }
        
    private int getTransposedPitch(Random rand) {
        if(this.transposition != null) {
            return this.pitch + this.transposition.getValue(rand);
        } else {
            return this.pitch;
        }
    }

    public Note generateNote(Random rand, int start, int patternLength, double position, Integer maximumLength) {
        int nStart, nLength, nVelocity;
        
        if(rand.nextDouble() > this.probability) {
            return null;
        }

        nVelocity = this.velocity.getValue(position, rand);
        
        if(this.start != null){
            nStart = this.start.getValue(position, rand);
        } else {
            nStart = (int)(this.relativeStart.getValue(position, rand) * patternLength);
        }
        
        if(this.length != null) {
            nLength = this.length.getValue(position, rand);
        } else {
            nLength = (int)(this.relativeLength.getValue(position, rand) * patternLength);
        }
        
        if(maximumLength != null && nStart + nLength > maximumLength) {
            nLength = maximumLength - nStart;
        }
        
        return new Note(
                getTransposedPitch(rand),
                start + nStart,
                nLength,
                nVelocity,
                this.MIDITrack
        );
    }
}
