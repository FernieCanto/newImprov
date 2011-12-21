package improviso;

import java.util.Random;
import java.util.regex.*;

/**
 * Armazena uma definição de uma pitch componente de um padrão.
 * @author fernando
 */
public class NoteDefinition {
    public static int defaultStart = 0, defaultMaxStart = 0, defaultStartEnd = 0, defaultMaxStartEnd = 0;
    public static int defaultLength = 60, defaultMaxLength = 60, defaultLengthEnd = 60, defaultMaxLengthEnd = 60;
    public static int defaultVelocity = 100, defaultMaxVelocity = 100, defaultVelocityEnd = 100, defaultMaxVelocityEnd = 100;
    public static int defaultMIDITrack = 1;
    
    int pitch;
    int MIDITrack;
    
    protected NumericInterval start = null;
    protected NumericInterval duration = null;
    protected NumericInterval velocity = null;
    
    protected DoubleInterval relativeStart = null;
    protected DoubleInterval relativeDuration = null;
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
            this.start = new NumericInterval(defaultStart, defaultMaxStart, defaultStartEnd, defaultMaxStartEnd);
        
        if(element.hasAttribute("relativeDuration"))
            this.relativeDuration = Composition.createDoubleInterval(element.getAttribute("relativeDuration"));
        else if(element.hasAttribute("duration"))
            this.duration = Composition.createLengthInterval(element.getAttribute("duration"));
        else
            this.duration = new NumericInterval(defaultLength, defaultMaxLength, defaultLengthEnd, defaultMaxLengthEnd);
        
        if(element.hasAttribute("velocity"))
            this.velocity = Composition.createNumericInterval(element.getAttribute("velocity"));
        else
            this.velocity = new NumericInterval(defaultVelocity, defaultMaxVelocity, defaultVelocityEnd, defaultMaxVelocityEnd);
        
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

    public Note generateNote(int start, int patternDuration, double position) {
        return this.generateNote(start, patternDuration, position, null);
    }

    public Note generateNote(int start, int patternDuration, double position, Integer maximumDuration) {
        int nStart, nDuration, nVelocity;

        if(this.rand == null)
            this.rand = new Random();
        
        if(this.probability != null && this.rand.nextDouble() > this.probability.getValue())
            return null;

        nVelocity = this.velocity.getValue(position, rand);
        
        if(this.start != null)
            nStart      = this.start.getValue(position, rand);
        else
            nStart      = (int)(this.relativeStart.getValue(position, rand) * patternDuration);
        
        if(this.duration != null)
            nDuration   = this.duration.getValue(position, rand);
        else
            nDuration   = (int)(this.relativeDuration.getValue(position, rand) * patternDuration);
        
        if(maximumDuration != null) {
            if(nStart + nDuration > maximumDuration)
                nDuration = maximumDuration - nStart;
        }
        
        return new Note(this.pitch, start + nStart, nDuration, nVelocity, this.MIDITrack);
    }
}
