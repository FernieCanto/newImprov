package improviso;

import java.util.Random;
import java.util.regex.*;

/**
 * Armazena uma definição de uma pitch componente de um padrão.
 * @author fernando
 */
public class Note {
    final private int pitch;
    final private int MIDITrack;
    
    final private IntegerRange start;
    final private IntegerRange length;
    final private IntegerRange velocity;
    
    final private DoubleRange relativeStart;
    final private DoubleRange relativeLength;
    final private double probability;
    
    final private IntegerRange transposition;
    
    final private static java.util.regex.Pattern NOTE_NAME_PATTERN = java.util.regex.Pattern.compile("^([A-G])([#b])?(-2|-1|\\d)$");
    
    public static class NoteBuilder {
        private int pitch;
        private int MIDITrack = 1;
        private IntegerRange start = new IntegerRange(0, 0, 0, 0);
        private IntegerRange length = new IntegerRange(60, 60, 60, 60);
        private IntegerRange velocity = new IntegerRange(100, 100, 100, 100);
        private DoubleRange relativeStart = null;
        private DoubleRange relativeLength = null;
        private double probability = 1.0;
        private IntegerRange transposition = new IntegerRange(0, 0, 0, 0);
        
        public int getPitch() {
            return pitch;
        }

        public NoteBuilder setPitch(int pitch) {
            this.pitch = pitch;
            return this;
        }

        public int getMIDITrack() {
            return MIDITrack;
        }

        public NoteBuilder setMIDITrack(int MIDITrack) {
            this.MIDITrack = MIDITrack;
            return this;
        }
        
        public IntegerRange getStart() {
            return start;
        }
        
        public NoteBuilder setStart(IntegerRange start) {
            this.start = start;
            if (start != null) {
                this.relativeStart = null;
            }
            return this;
        }

        public DoubleRange getRelativeStart() {
            return relativeStart;
        }
        
        public NoteBuilder setRelativeStart(DoubleRange relativeStart) {
            this.relativeStart = relativeStart;
            if (relativeStart != null) {
                this.start = null;
            }
            return this;
        }

        public IntegerRange getLength() {
            return length;
        }

        public NoteBuilder setLength(IntegerRange length) {
            this.length = length;
            if (length != null) {
                this.relativeLength = null;
            }
            return this;
        }

        public DoubleRange getRelativeLength() {
            return relativeLength;
        }

        public NoteBuilder setRelativeLength(DoubleRange relativeLength) {
            this.relativeLength = relativeLength;
            if (relativeLength != null) {
                this.length = null;
            }
            return this;
        }

        public IntegerRange getVelocity() {
            return velocity;
        }

        public NoteBuilder setVelocity(IntegerRange velocity) {
            this.velocity = velocity;
            return this;
        }

        public double getProbability() {
            return probability;
        }

        public NoteBuilder setProbability(double probability) {
            this.probability = probability;
            return this;
        }

        public IntegerRange getTransposition() {
            return transposition;
        }

        public NoteBuilder setTransposition(IntegerRange transposition) {
            this.transposition = transposition;
            return this;
        }
        
        public Note build()
        {
            return new Note(this);
        }
    }
    
    protected Note(NoteBuilder builder) {
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

            if(!m.group(3).equals("-2")) {
                int octave = Integer.parseInt(m.group(3));
                note += (octave+2) * 12;
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
        return this.pitch + this.transposition.getValue(rand);
    }

    public MIDINoteList execute(Random rand, int patternLength, double position, int maximumLength) {
        int nStart, nLength;
        
        if(rand.nextDouble() > this.probability) {
            return new MIDINoteList();
        }
        
        if(this.start != null){
            nStart = this.start.getValue(rand, position);
        } else {
            nStart = (int)(this.relativeStart.getValue(position, rand) * patternLength);
        }
        
        if(this.length != null) {
            nLength = this.length.getValue(rand, position);
        } else {
            nLength = (int)(this.relativeLength.getValue(position, rand) * patternLength);
        }
        
        if(nStart + nLength > maximumLength) {
            nLength = maximumLength - nStart;
        }
        
        return new MIDINoteList(new MIDINote(
                getTransposedPitch(rand),
                nStart,
                nLength,
                this.velocity.getValue(rand, position),
                this.MIDITrack
        ));
    }
}
