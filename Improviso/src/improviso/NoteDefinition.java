package improviso;

import java.util.Random;
import java.util.regex.*;

/**
 * Armazena uma definição de uma pitch componente de um padrão.
 * @author fernando
 */
public class NoteDefinition {
    public static NumericInterval defaultStart = new NumericInterval(0, 0, 0, 0);
    public static NumericInterval defaultLength = new NumericInterval(60, 60, 60, 60);
    public static NumericInterval defaultVelocity = new NumericInterval(100, 100, 100, 100);
    
    public static DoubleInterval defaultRelativeStart = null;
    public static DoubleInterval defaultRelativeLength = null;
    
    public static int defaultMIDITrack = 1;
    
    int pitch;
    int MIDITrack;
    
    protected NumericInterval start = null;
    protected NumericInterval length = null;
    protected NumericInterval velocity = null;
    
    protected DoubleInterval relativeStart = null;
    protected DoubleInterval relativeLength = null;
    protected Double probability = null;
    
    protected NumericInterval transposition = null;
    
    protected static java.util.regex.Pattern noteNamePattern = java.util.regex.Pattern.compile("^([A-G])([#b])?(-1|\\d)$");
    protected static java.util.regex.Pattern intervalPattern = java.util.regex.Pattern.compile("^(\\d+)(-(\\d+))?(\\|(\\d+)(-(\\d+))?)?$");

    Random rand = null;

    NoteDefinition(int note) {
        this.pitch = note;
        MIDITrack = 1;
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
        Matcher m = noteNamePattern.matcher(stringNoteName);
        
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
    
    public static NoteDefinition generateNoteDefinitionXML(ElementLibrary library, org.w3c.dom.Element element)
        throws ImprovisoException {
        NoteDefinition def = new NoteDefinition(interpretNoteName(library, element.getFirstChild().getNodeValue()));
        def.configureNoteDefinitionXML(element);
        
        return def;
    }
    
    public boolean configureNoteDefinitionXML(org.w3c.dom.Element element)
        throws ImprovisoException {
        if(element.hasAttribute("relativeStart")) {
            this.relativeStart = StringInterpreter.createDoubleInterval(element.getAttribute("relativeStart"));
        } else if(element.hasAttribute("start")) {
            this.start = StringInterpreter.createLengthInterval(element.getAttribute("start"));
        } else {
            this.start = defaultStart;
        }
        
        if(element.hasAttribute("relativeLength")) {
            this.relativeLength = StringInterpreter.createDoubleInterval(element.getAttribute("relativeLength"));
        } else if(element.hasAttribute("length")) {
            this.length = StringInterpreter.createLengthInterval(element.getAttribute("length"));
        } else {
            this.length = defaultLength;
        }
        
        if(element.hasAttribute("velocity")) {
            this.velocity = StringInterpreter.createNumericInterval(element.getAttribute("velocity"));
        } else {
            this.velocity = defaultVelocity;
        }
        
        if(element.hasAttribute("track")) {
            this.setMIDITrack(Integer.parseInt(element.getAttribute("track")));
        } else {
            this.setMIDITrack(defaultMIDITrack);
        }
        
        if(element.hasAttribute("probability")) {
            this.probability = Double.parseDouble(element.getAttribute("probability"));
        }
        if(element.hasAttribute("transposition")) {
            this.transposition = StringInterpreter.createNumericInterval(element.getAttribute("transposition"));
        }
        
        return true;
    }

    public void setSeed(long seed) {
        if(this.rand == null) {
            this.rand = new Random(seed);
        } else {
            this.rand.setSeed(seed);
        }
    }

    public void setMIDITrack(int track) {
        this.MIDITrack = track;
    }

    public Note generateNote(int start, int patternLength, double position) {
        return this.generateNote(start, patternLength, position, null);
    }

    public Note generateNote(int start, int patternLength, double position, Integer maximumLength) {
        int nPitch, nStart, nLength, nVelocity;

        if(this.rand == null) {
            this.rand = new Random();
        }
        
        if(this.probability != null && this.rand.nextDouble() > this.probability) {
            return null;
        }
        
        nPitch = this.pitch;
        if(this.transposition != null) {
            nPitch += this.transposition.getValue(this.rand);
        }

        nVelocity = this.velocity.getValue(position, this.rand);
        
        if(this.start != null){
            nStart = this.start.getValue(position, this.rand);
        } else {
            nStart = (int)(this.relativeStart.getValue(position, this.rand) * patternLength);
        }
        
        if(this.length != null) {
            nLength = this.length.getValue(position, this.rand);
        } else {
            nLength = (int)(this.relativeLength.getValue(position, this.rand) * patternLength);
        }
        
        if(maximumLength != null) {
            if(nStart + nLength > maximumLength) {
                nLength = maximumLength - nStart;
            }
        }
        
        return new Note(nPitch, start + nStart, nLength, nVelocity, this.MIDITrack);
    }
}
