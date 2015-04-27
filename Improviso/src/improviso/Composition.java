package improviso;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * This class implements the a full Improviso Composition, with all its
 * elements. A Composition is basically a graph of Sections, with Arrows
 * linking them. The Composition, when executed, will start from one of the
 * starting sections and execute it, and then follow one of its Arrows to the
 * next Section, repeating the process until it reaches a Section with no
 * outward Arrows, or until it follows an explicit ending Arrow. All other
 * elements are placed inside the Sections, so the Composition does not handle
 * directly with them.
 * @author Fernie Canto
 */
public class Composition {
    public static final int TICKS_WHOLENOTE = 480;
    
    protected ArrayList<Note> notes;
    protected ArrayList<MIDITrack> MIDITracks;
    protected LinkedHashMap<String, Section> sections;
    protected ArrowList initialSections = new ArrowList();
    protected HashMap<String, ArrowList> sectionDestinations;
    
    static java.util.regex.Pattern floatPercentPattern      = java.util.regex.Pattern.compile("^(?<val>(\\d+)|(\\d+\\.\\d+)|(\\.\\d+))(?<percent>%)?$");
    static java.util.regex.Pattern numericIntervalPattern   = java.util.regex.Pattern.compile("^(?<val>\\d+)(-(?<valMax>\\d+))?$");
    static java.util.regex.Pattern beatsPlusTicksPattern    = java.util.regex.Pattern.compile("^(?<seminimas>\\d+):(?<ticks>\\d\\d\\d)$");
    static java.util.regex.Pattern timeSignaturesPattern    = java.util.regex.Pattern.compile("((?<quant>\\d+)\\s)?(?<num>\\d+)/(?<denom>\\d+)\\s?");
    
    int offset = 0;
    
    public Composition() {
        notes = new ArrayList<Note>();
        MIDITracks = new ArrayList<MIDITrack>();
        sections = new LinkedHashMap<String, Section>();
        sectionDestinations = new HashMap<String, ArrowList>();
    }
    
    /**
     * Read an XML file and produces a Composition from it.
     * @param fileName The path and name of the XML file to be processed
     * @return The generated Composition
     * @throws ImprovisoException
     * @throws ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws IOException 
     */
    public static Composition processXML(String fileName)
            throws ImprovisoException,
                   ParserConfigurationException,
                   org.xml.sax.SAXException,
                   IOException
                   {
        Composition c = new Composition();
        ElementLibrary library = new ElementLibrary();
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        File XMLFile = new File(fileName);
        Document XMLDocument = dBuilder.parse(XMLFile);
        XMLDocument.normalizeDocument();
        
        validateXML(XMLDocument);
        
        Element compositionElement = XMLDocument.getDocumentElement();
        if(compositionElement.hasAttribute("padding")) {
            c.setOffset(Composition.interpretLength(compositionElement.getAttribute("padding")));
        }
        
        /*
        NodeList aliasImportList = XMLDocument.getElementsByTagName("importAlias");
        for(int index = 0; index < aliasImportList.getLength(); index++) {
            Element aliasFileElement = (Element)aliasImportList.item(index);
            //File aliasXMLFile = new File(aliasFileElement.getAttribute("file"));
        } */
        
        Document aliasXMLDocument = dBuilder.parse(dBuilder.getClass().getResourceAsStream("/improviso/GMDrumsAliases.xml"));
        aliasXMLDocument.normalizeDocument();
        NodeList aliasList = aliasXMLDocument.getElementsByTagName("alias");
        for(int index = 0; index < aliasList.getLength(); index++) {
            Element aliasElement = (Element)aliasList.item(index);
            library.addNoteAlias(aliasElement.getFirstChild().getNodeValue().trim(), Integer.parseInt(aliasElement.getAttribute("note")));
        }
        
        NodeList MIDITracks = XMLDocument.getElementsByTagName("MIDITrack");
        for(int index = 0; index < MIDITracks.getLength(); index++) {
            Element MIDITrackElement = (Element)MIDITracks.item(index);
            c.addMIDITrack(MIDITrack.generateMIDITrackXML(MIDITrackElement));
        }
        
        Node patternList = XMLDocument.getElementsByTagName("patternList").item(0);
        if(patternList != null) {
            loadPatterns(patternList, library);
        }
        
        Node groupList = XMLDocument.getElementsByTagName("groupList").item(0);
        if(groupList != null) {
            loadGroups(groupList, library);
        }
        
        Node trackList = XMLDocument.getElementsByTagName("trackList").item(0);
        if(trackList != null) {
            loadTracks(trackList, library);
        }
        
        Node sectionList = XMLDocument.getElementsByTagName("sectionList").item(0);
        if(sectionList != null) {
            loadSections(sectionList, library);
        }
        
        loadStructure(XMLDocument, library, c);
        
        return c;
    }

    private static void loadStructure(Document XMLDocument, ElementLibrary library, Composition c) throws ImprovisoException {
        Element structureElement = (Element)XMLDocument.getElementsByTagName("structure").item(0);
        NodeList sectionElements = structureElement.getElementsByTagName("section");
        for(int index = 0; index < sectionElements.getLength(); index++) {
            Element sectionElement = (Element)sectionElements.item(index);
            Section section = library.getSection(sectionElement.getAttribute("after"));
            if(section == null) {
                throw new ImprovisoException("Invalid section: "+sectionElement.getAttribute("after"));
            }
            c.addSection(sectionElement.getAttribute("id"), section);
            
            NodeList arrows = sectionElement.getElementsByTagName("arrow");
            for(int index2 = 0; index2 < arrows.getLength(); index2++) {
                Element arrowElement = (Element)arrows.item(index2);
                Arrow a = Arrow.generateArrowXML(arrowElement);
                c.addArrow(sectionElement.getAttribute("id"), a);
            }
        }
    }

    private static void loadSections(Node sectionList, ElementLibrary library) throws ImprovisoException {
        for(int index = 0; index < sectionList.getChildNodes().getLength(); index++) {
            Node sectionNode = sectionList.getChildNodes().item(index);
            if(sectionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sectionElement = (Element)sectionNode;
                String sectionId = sectionElement.getTagName();
                library.addSection(sectionId, Section.generateSectionXML(library, sectionElement));
            }
        }
    }

    private static void loadTracks(Node trackList, ElementLibrary library) throws ImprovisoException {
        for(int index = 0; index < trackList.getChildNodes().getLength(); index++) {
            Node trackNode = trackList.getChildNodes().item(index);
            if(trackNode.getNodeType() == Node.ELEMENT_NODE) {
                Element trackElement = (Element)trackNode;
                String trackId = trackElement.getTagName();
                library.addTrack(trackId, Track.generateTrackXML(library, trackElement));
            }
        }
    }

    private static void loadGroups(Node groupList, ElementLibrary library) throws ImprovisoException {
        for(int index = 0; index < groupList.getChildNodes().getLength(); index++) {
            Node groupNode = groupList.getChildNodes().item(index);
            if(groupNode.getNodeType() == Node.ELEMENT_NODE) {
                Element groupElement = (Element)groupNode;
                String groupId = groupElement.getTagName();
                library.addGroup(groupId, Group.generateGroupXML(library, groupElement));
            }
        }
    }

    private static void loadPatterns(Node patternList, ElementLibrary library) throws ImprovisoException {
        for(int index = 0; index < patternList.getChildNodes().getLength(); index++) {
            Node patternNode = patternList.getChildNodes().item(index);
            if(patternNode.getNodeType() == Node.ELEMENT_NODE) {
                Element patternElement = (Element)patternNode;
                String patternId = patternElement.getTagName();
                library.addPattern(patternId, Pattern.generatePatternXML(library, patternElement));
            }
        }
    }

    static private void validateXML(Document XMLDocument) throws ImprovisoException {
        if(!XMLDocument.getDocumentElement().getTagName().equals("composition")) {
            throw new ImprovisoException("The root element of the composition file must be of the type 'composition'");
        }
        if(XMLDocument.getElementsByTagName("MIDITrack").getLength() == 0) {
            throw new ImprovisoException("The composition must have at least one MIDI track");
        }
        if(XMLDocument.getElementsByTagName("patternList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one pattern list");
        }
        if(XMLDocument.getElementsByTagName("groupList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one group list");
        }
        if(XMLDocument.getElementsByTagName("trackList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one track list");
        }
        if(XMLDocument.getElementsByTagName("sectionList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one section list");
        }
        if(XMLDocument.getElementsByTagName("structure").getLength() != 1) {
            throw new ImprovisoException("The composition must have exactly one 'structure' tag");
        }
        
        Element structure = (Element)XMLDocument.getElementsByTagName("structure").item(0);
        if(structure.getElementsByTagName("section").getLength() == 0) {
            throw new ImprovisoException("The structure of the composition must have at least one section");
        }
    }
    
    /**
     * Interprets a numeric interval String of the kind "A - B | C - D", where
     * A, B, C and D are integer numbers, and generates a NumericInterval from it.
     * @param intervalString The String to be read
     * @return The corresponding NumericInterval
	   * @throws improviso.ImprovisoException
     */
    public static NumericInterval createNumericInterval(String intervalString)
        throws ImprovisoException {
        int minVal, maxVal, minEndVal, maxEndVal;
        
        try {
            String[] beginEnd = intervalString.split(" \\| ");
            String[] minMax = beginEnd[0].split(" - ");
            minVal = Integer.parseInt(minMax[0]);
            if(minMax.length > 1)
                maxVal = Integer.parseInt(minMax[1]);
            else
                maxVal = minVal;

            if(beginEnd.length > 1) {
                String[] minMaxEnd = beginEnd[1].split(" - ");
                minEndVal = Integer.parseInt(minMaxEnd[0]);
                if(minMaxEnd.length > 1)
                    maxEndVal = Integer.parseInt(minMaxEnd[1]);
                else
                    maxEndVal = minEndVal;
            }
            else {
                minEndVal = minVal;
                maxEndVal = maxVal;
            }

            return new NumericInterval(minVal, maxVal, minEndVal, maxEndVal);
        }
        catch(NumberFormatException e) {
            ImprovisoException exception = new ImprovisoException("Invalid values: "+intervalString);
            exception.addSuppressed(e);
            throw exception;
        }
    }
    
    /**
     * Interprets a length or position interval String of the kind "A - B | C - D",
     * where A, B, C and D are lengths or positions, and generates a NumericInterval
     * in ticks.
     * @param intervalString The String to be read
     * @return The corresponding NumericInterval
     * @throws improviso.ImprovisoException
     */
    public static NumericInterval createLengthInterval(String intervalString)
        throws ImprovisoException {
        int minVal, maxVal, minEndVal, maxEndVal;
        
        try {
            String[] beginEnd = intervalString.split(" \\| ");
            String[] minMax = beginEnd[0].split(" - ");
            minVal = interpretLength(minMax[0]);
            if(minMax.length > 1)
                maxVal = interpretLength(minMax[1]);
            else
                maxVal = minVal;

            if(beginEnd.length > 1) {
                String[] minMaxEnd = beginEnd[1].split(" - ");
                minEndVal = interpretLength(minMaxEnd[0]);
                if(minMaxEnd.length > 1)
                    maxEndVal = interpretLength(minMaxEnd[1]);
                else
                    maxEndVal = minEndVal;
            }
            else {
                minEndVal = minVal;
                maxEndVal = maxVal;
            }

            return new NumericInterval(minVal, maxVal, minEndVal, maxEndVal);
        }
        catch(NumberFormatException e) {
            ImprovisoException exception = new ImprovisoException("Invalid length: "+intervalString);
            exception.addSuppressed(e);
            throw exception;
        }
    }
    
    /**
     * Interprets a double interval String of the kind "A - B | C - D", where
     * A, B, C and D are double values or percentages, and generates a DoubleInterval.
     * @param intervalString The String to be read
     * @return The corresponding DoubleInterval
		 * @throws improviso.ImprovisoException
     */
    public static DoubleInterval createDoubleInterval(String intervalString)
        throws ImprovisoException {
        double minVal, maxVal, minEndVal, maxEndVal;
        
        try {
            String[] beginEnd = intervalString.split(" \\| ");
            String[] minMax = beginEnd[0].split(" - ");
            minVal = interpretFloatPercentage(minMax[0]);
            if(minMax.length > 1)
                maxVal = interpretFloatPercentage(minMax[1]);
            else
                maxVal = minVal;

            if(beginEnd.length > 1) {
                String[] minMaxEnd = beginEnd[1].split(" - ");
                minEndVal = interpretFloatPercentage(minMaxEnd[0]);
                if(minMaxEnd.length > 1)
                    maxEndVal = interpretFloatPercentage(minMaxEnd[1]);
                else
                    maxEndVal = minEndVal;
            }
            else {
                minEndVal = minVal;
                maxEndVal = maxVal;
            }

            return new DoubleInterval(minVal, maxVal, minEndVal, maxEndVal);
        }
        catch(NumberFormatException e) {
            ImprovisoException exception = new ImprovisoException("Invalid length: "+intervalString);
            exception.addSuppressed(e);
            throw exception;
        }
    }
    
    /**
     * Reads a length or position string, of any form, and returns the corresponding
     * length in ticks.
     * @param lengthString The String to be read
     * @return The corresponding length or position in ticks
     * @throws ImprovisoException 
     */
    public static int interpretLength(String lengthString)
           throws ImprovisoException {
        int ticks = 0;
        
        try {
            Matcher m = beatsPlusTicksPattern.matcher(lengthString);
            if(m.matches()) {
                ticks += Integer.parseInt(m.group("seminimas")) * Composition.TICKS_WHOLENOTE / 4; /* Número antes do : */
                ticks += Integer.parseInt(m.group("ticks")); /* Número após o : */
                return ticks;
            }

            Matcher m2 = timeSignaturesPattern.matcher(lengthString);
            if(m2.find()) {
                do {
                    int quant = 1;
                    int numerator, denominator;
                    if(m2.group("quant") != null)
                        quant = Integer.parseInt(m2.group("quant"));
                    numerator = Integer.parseInt(m2.group("num"));
                    denominator = Integer.parseInt(m2.group("denom"));

                    ticks += quant * numerator * (Composition.TICKS_WHOLENOTE / denominator);
                } while(m2.find());

                return ticks;
            }

            ticks = Integer.parseInt(lengthString);
            return ticks;
        }
        catch(NumberFormatException e) {
            ImprovisoException exception = new ImprovisoException("Invalid length: "+lengthString);
            exception.addSuppressed(e);
            throw exception;
        }
    }
    
		/**
		 * 
		 * @param valueString
		 * @return
		 * @throws ImprovisoException 
		 */
    public static double interpretFloatPercentage(String valueString)
           throws ImprovisoException {
        double value;
        Matcher m = floatPercentPattern.matcher(valueString);
        if(m.matches()) {
            value = Double.parseDouble(m.group("val"));
            if(m.group("percent") != null)
                value /= 100.0;
            
            return value;
        }
        else throw new ImprovisoException("Invalid float value: "+valueString);
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
     * @param fileName The name of the MIDI file to be produced
     * @return
     * @throws ImprovisoException
     * @throws InvalidMidiDataException
     * @throws IOException 
     * @throws javax.sound.midi.MidiUnavailableException 
     */
    public boolean execute(String fileName)
            throws ImprovisoException,
                   InvalidMidiDataException,
                   IOException,
                   MidiUnavailableException {
        String currentSectionId;
        Section currentSection;
        int currentPosition = 0;
        MIDIGenerator generator = new MIDIGenerator(MIDITracks);
        generator.setOffset(offset);

        if(initialSections.getNumArrows() > 0) {
            currentSectionId = initialSections.getNextDestination();
        } else {
            if(sections.isEmpty())
                return false;
            else
                currentSectionId = sections.keySet().iterator().next();
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

        if(fileName == null) {
            generator.play();
        } else {
            generator.generateFile(fileName);
        }
        return true;
    }
    
    public boolean execute()
            throws ImprovisoException,
                   InvalidMidiDataException,
                   IOException,
                   MidiUnavailableException {
        return this.execute(null);
    }
}