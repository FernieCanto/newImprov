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
    protected DoubleInterval probability = null;
    
    protected static java.util.regex.Pattern noteNamePattern = java.util.regex.Pattern.compile("^([A-G])([#b])?(-1|\\d)$");
    protected static java.util.regex.Pattern intervalPattern = java.util.regex.Pattern.compile("^(\\d+)(-(\\d+))?(\\|(\\d+)(-(\\d+))?)?$");

    Random rand = null;

    NoteDefinition(int note) {
        this.pitch = note;
        MIDITrack = 1;
    }
    
    public static int interpretNoteName(XMLLibrary XMLLib, String stringNoteName)
        throws ImprovisoException {
        int note = 0;
        Matcher m = noteNamePattern.matcher(stringNoteName);
        
        if(XMLLib.noteAliases.containsKey(stringNoteName)) {
            return XMLLib.noteAliases.get(stringNoteName);
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
    
    public static NoteDefinition generateNoteDefinitionXML(XMLLibrary bibXML, org.w3c.dom.Element element)
        throws ImprovisoException {
        NoteDefinition def = new NoteDefinition(interpretNoteName(bibXML, element.getFirstChild().getNodeValue()));
        def.configureNoteDefinitionXML(element);
        
        return def;
    }
    
    public boolean configureNoteDefinitionXML(org.w3c.dom.Element element)
        throws ImprovisoException {
        if(element.hasAttribute("relativeStart"))
            this.relativeStart = Composition.createDoubleInterval(element.getAttribute("relativeStart"));
        else if(element.hasAttribute("start"))
            this.start = Composition.createLengthInterval(element.getAttribute("start"));
        else
            this.start = defaultStart;
        
        if(element.hasAttribute("relativeLength"))
            this.relativeLength = Composition.createDoubleInterval(element.getAttribute("relativeLength"));
        else if(element.hasAttribute("length"))
            this.length = Composition.createLengthInterval(element.getAttribute("length"));
        else
            this.length = defaultLength;
        
        if(element.hasAttribute("velocity"))
            this.velocity = Composition.createNumericInterval(element.getAttribute("velocity"));
        else
            this.velocity = defaultVelocity;
        
        if(element.hasAttribute("track"))
            this.setMIDITrack(Integer.parseInt(element.getAttribute("track")));
        else
            this.setMIDITrack(defaultMIDITrack);
        
        return true;
    }

    public void setSeed(long seed) {
        if(this.rand == null)
            this.rand = new Random(seed);
        else
            this.rand.setSeed(seed);
    }

    public void setMIDITrack(int faixa) {
        this.MIDITrack = faixa;
    }

    public Note generateNote(int start, int patternLength, double position) {
        return this.generateNote(start, patternLength, position, null);
    }

    public Note generateNote(int start, int patternLength, double position, Integer maximumLength) {
        int nStart, nLength, nVelocity;

        if(this.rand == null)
            this.rand = new Random();
        
        if(this.probability != null && this.rand.nextDouble() > this.probability.getValue())
            return null;

        nVelocity = this.velocity.getValue(position, rand);
        
        if(this.start != null)
            nStart      = this.start.getValue(position, rand);
        else
            nStart      = (int)(this.relativeStart.getValue(position, rand) * patternLength);
        
        if(this.length != null)
            nLength   = this.length.getValue(position, rand);
        else
            nLength   = (int)(this.relativeLength.getValue(position, rand) * patternLength);
        
        if(maximumLength != null) {
            if(nStart + nLength > maximumLength)
                nLength = maximumLength - nStart;
        }
        
        return new Note(this.pitch, start + nStart, nLength, nVelocity, this.MIDITrack);
    }
}
