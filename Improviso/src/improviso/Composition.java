package improviso;
import java.util.*;
import java.io.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * This class implements the a full Improviso Composition, with all its
 * elements. A Composition consists of graph of Sections, with Arrows
 * linking them. The Composition, when executed, will choose one of the
 * starting sections and execute it, then follow one of its Arrows to the
 * next Section, repeating the process until it reaches a Section with no
 * outward Arrows, or follows an explicit ending Arrow. All other elements
 * are placed inside the Sections, so the Composition does not handle
 * them directly.
 * @author Fernie Canto
 */
public class Composition {
    /**
     * The number of ticks in a whole note, used for reference when calculating
     * durations and positions of notes.
     */
    public static final int TICKS_WHOLENOTE = 480;
    
    /**
     * List of MIDI tracks that shall be present in the MIDI file.
     */
    protected ArrayList<MIDITrack> MIDITracks;
    /**
     * Map of all the sections in the composition.
     */
    protected LinkedHashMap<String, Section> sections;
    /**
     * List of Arrows that point to the possible initial Sections of the
     * composition. One of these will be chosen when the composition is
     * executed.
     */
    protected ArrowList initialSections = new ArrowList();
    /**
     * Map of the lists of Arrows that point out of each Section.
     */
    protected HashMap<String, ArrowList> sectionDestinations;
    
    /**
     * Number of ticks at the start of the composition, before the first section
     * is executed.
     */
    protected int offset = 0;
    
    public Composition() {
        this.MIDITracks = new ArrayList<MIDITrack>();
        this.sections = new LinkedHashMap<String, Section>();
        this.sectionDestinations = new HashMap<String, ArrowList>();
    }
    
    public static String showBeatsAndTicks(int ticks) {
        String beats = Integer.toString(ticks / (TICKS_WHOLENOTE / 4));
        String remTicks = Integer.toString(ticks % (TICKS_WHOLENOTE / 4));
        
        return beats+":"+remTicks;
    }
    
    
    /**
     * Define the offset, or "filler", in ticks in the MIDI file, that is, the
     * amount of silence there is before the first note is played.
     * @param offset The offset in ticks
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    /**
     * Adds a MIDITrack to the composition.
     * @param track 
     */
    public void addMIDITrack(MIDITrack track) {
        MIDITracks.add(track);
    }
    
    /**
     * Adds a Section to the composition
     * @param id The section identifier
     * @param section 
     */
    public void addSection(String id, Section section) {
        sections.put(id, section);
        sectionDestinations.put(id, new ArrowList());
    }
    
    /**
     * Adds an Arrow to the composition, with a determined origin Section.
     * @param origin The identifier of the origin Section
     * @param arrow 
     * @throws improviso.ImprovisoException 
     */
    public void addArrow(String origin, Arrow arrow) throws ImprovisoException {
        if(origin == null) {
            initialSections.addArrow(arrow);
        } else if(sectionDestinations.get(origin) != null) {
            sectionDestinations.get(origin).addArrow(arrow);
        } else {
            throw new ImprovisoException("Section not found: "+origin);
        }
    }

    /**
     * Produces a MIDI file from the composition, with the given path and name.
     * @return
     * @throws ImprovisoException
     * @throws InvalidMidiDataException
     * @throws IOException 
     * @throws javax.sound.midi.MidiUnavailableException 
     */
    public MIDIGenerator execute()
            throws ImprovisoException,
                   InvalidMidiDataException,
                   IOException,
                   MidiUnavailableException {
        String currentSectionId;
        Section currentSection;
        int currentPosition = offset;
        MIDIGenerator generator = new MIDIGenerator(this.MIDITracks);
        //generator.setOffset(offset);

        if(initialSections.getNumArrows() > 0) {
            currentSectionId = initialSections.getNextDestination();
        } else {
            if(sections.isEmpty()) {
                throw new ImprovisoException("Composition has no starting sections");
            } else {
                currentSectionId = sections.keySet().iterator().next();
            }
        }

        do {
            currentSection = sections.get(currentSectionId);
            currentSection.initialize(currentPosition);

            generator.setCurrentTick(currentPosition);
            generator.setTempo(currentSection.getTempo());
            generator.setTimeSignature(currentSection.getTimeSignatureNumerator(), currentSection.getTimeSignatureDenominator());
            
            generator.addNotes(currentSection.execute());

            currentPosition = currentSection.getCurrentPosition();
            
            if(sectionDestinations.get(currentSectionId).getNumArrows() > 0) {
                currentSectionId = sectionDestinations.get(currentSectionId).getNextDestination();
            } else {
                currentSectionId = null;
            }

        } while(currentSectionId != null);
        return generator;
    }
}