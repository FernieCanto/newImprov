package improviso;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import javax.sound.midi.InvalidMidiDataException;
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
    
    static java.util.regex.Pattern numericIntervalPattern = java.util.regex.Pattern.compile("^(?<val>\\d+)(-(?<valMax>\\d+))?$");
    static java.util.regex.Pattern beatsPlusTicksPattern = java.util.regex.Pattern.compile("^(?<seminimas>\\d):(?<ticks>\\d\\d\\d)$");
    static java.util.regex.Pattern timeSignaturesPattern = java.util.regex.Pattern.compile("((?<quant>\\d+)\\s)?(?<num>\\d+)/(?<denom>\\d+)\\s?");
    
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
        XMLLibrary XMLLibrary = new XMLLibrary();
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        File XMLFile = new File(fileName);
        Document XMLDocument = dBuilder.parse(XMLFile);
        XMLDocument.normalizeDocument();
        
        Element compositionElement = XMLDocument.getDocumentElement();
        if(compositionElement.hasAttribute("padding"))
          c.setOffset(Composition.interpretLength(compositionElement.getAttribute("padding")));
        
        NodeList aliasImportList = XMLDocument.getElementsByTagName("importAlias");
        for(int index = 0; index < aliasImportList.getLength(); index++) {
            Element aliasFileElement = (Element)aliasImportList.item(index);
            File aliasXMLFile = new File(aliasFileElement.getAttribute("file"));
            
            Document aliasXMLDocument = dBuilder.parse("src/improviso/GMDrumsAliases.xml");
            aliasXMLDocument.normalizeDocument();
            
            NodeList aliasList = aliasXMLDocument.getElementsByTagName("alias");
            for(int index2 = 0; index2 < aliasList.getLength(); index2++) {
                Element aliasElement = (Element)aliasList.item(index2);
                XMLLibrary.noteAliases.put(aliasElement.getFirstChild().getNodeValue(), Integer.parseInt(aliasElement.getAttribute("note")));
            }
        }
        
        NodeList MIDITracks = XMLDocument.getElementsByTagName("MIDITrack");
        for(int index = 0; index < MIDITracks.getLength(); index++) {
            Element MIDITrackElement = (Element)MIDITracks.item(index);
            
            c.addMIDITrack(MIDITrack.generateMIDITrackXML(MIDITrackElement));
        }
        
        NodeList patternLists = XMLDocument.getElementsByTagName("patternList");
        for(int index = 0; index < patternLists.getLength(); index++) {
            for(int index2 = 0; index2 < patternLists.item(index).getChildNodes().getLength(); index2++) {
                Node patternNode = patternLists.item(index).getChildNodes().item(index2);
                if(patternNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element patternElement = (Element)patternNode;
                    String patternId = patternElement.getAttribute("id");
                    XMLLibrary.patterns.put(patternId, patternElement);
                }
            }
        }
        
        NodeList groupLists = XMLDocument.getElementsByTagName("groupList");
        for(int index = 0; index < groupLists.getLength(); index++) {
            for(int index2 = 0; index2 < groupLists.item(index).getChildNodes().getLength(); index2++) {
                Node groupNode = groupLists.item(index).getChildNodes().item(index2);
                if(groupNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element groupElement = (Element)groupNode;
                    String groupId = groupElement.getAttribute("id");
                    XMLLibrary.groups.put(groupId, groupElement);
                }
            }
        }
        
        NodeList trackLists = XMLDocument.getElementsByTagName("trackList");
        for(int index = 0; index < trackLists.getLength(); index++) {
            for(int index2 = 0; index2 < trackLists.item(index).getChildNodes().getLength(); index2++) {
                Node trackNode = trackLists.item(index).getChildNodes().item(index2);
                if(trackNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element trackElement = (Element)trackNode;
                    String trackId = trackElement.getAttribute("id");
                    XMLLibrary.tracks.put(trackId, trackElement);
                }
            }
        }
        
        NodeList sectionLists = XMLDocument.getElementsByTagName("sectionList");
        for(int index = 0; index < sectionLists.getLength(); index++) {
            for(int index2 = 0; index2 < sectionLists.item(index).getChildNodes().getLength(); index2++) {
                Node sectionNode = sectionLists.item(index).getChildNodes().item(index2);
                if(sectionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element sectionElement = (Element)sectionNode;
                    String sectionId = sectionElement.getAttribute("id");
                    XMLLibrary.sections.put(sectionId, sectionElement);
                }
            }
        }
        
        Element structureElement = (Element)XMLDocument.getElementsByTagName("structure").item(0);
        NodeList sectionElements = structureElement.getElementsByTagName("section");
        for(int index = 0; index < sectionElements.getLength(); index++) {
            Element sectionElement = (Element)sectionElements.item(index);
            c.addSection(sectionElement.getAttribute("id"), Section.generateSectionXML(XMLLibrary, XMLLibrary.sections.get(sectionElement.getAttribute("after"))));
        }
        NodeList arrowElements = structureElement.getElementsByTagName("arrow");
        for(int index = 0; index < arrowElements.getLength(); index++) {
            Element arrowElement = (Element)arrowElements.item(index);
            Arrow a = Arrow.generateArrowXML(arrowElement);
            if(arrowElement.hasAttribute("from"))
                c.addArrow(arrowElement.getAttribute("from"), a);
            else if(a.getDestination() != null)
                c.addArrow(null, a);
        }
        
        return c;
    }
    
    /**
     * Interprets a numeric interval String of the kind "A - B | C - D", where
     * A, B, C and D are integer numbers, and generates a NumericInterval from it.
     * @param intervalString The String to be read
     * @return The corresponding NumericInterval
     */
    public static NumericInterval createNumericInterval(String intervalString) {
        int minVal, maxVal, minEndVal, maxEndVal;
        
        String[] beginEnd = intervalString.split(" \\| ");
        String[] minMax = beginEnd[0].split(" - ");
        minVal = Integer.parseInt(minMax[0]);
        if(minMax.length > 1)
            maxVal = Integer.parseInt(minMax[1]);
        else
            maxVal = minVal;
        
        if(beginEnd.length > 1) {
            String[] minMaxEnd = beginEnd[2].split(" - ");
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
    
    /**
     * Interprets a length or position interval String of the kind "A - B | C - D",
     * where A, B, C and D are lengths or positions, and generates a NumericInterval
     * in ticks.
     * @param intervalString The String to be read
     * @return The corresponding NumericInterval
     */
    public static NumericInterval createLengthInterval(String intervalString) {
        int minVal, maxVal, minEndVal, maxEndVal;
        
        String[] beginEnd = intervalString.split(" \\| ");
        String[] minMax = beginEnd[0].split(" - ");
        minVal = interpretLength(minMax[0]);
        if(minMax.length > 1)
            maxVal = interpretLength(minMax[2]);
        else
            maxVal = minVal;
        
        if(beginEnd.length > 1) {
            String[] minMaxEnd = beginEnd[2].split(" - ");
            minEndVal = interpretLength(minMaxEnd[0]);
            if(minMaxEnd.length > 1)
                maxEndVal = interpretLength(minMaxEnd[2]);
            else
                maxEndVal = minEndVal;
        }
        else {
            minEndVal = minVal;
            maxEndVal = maxVal;
        }
        
        return new NumericInterval(minVal, maxVal, minEndVal, maxEndVal);
    }
    
    /**
     * Reads a length or position string, of any form, and returns the corresponding
     * length in ticks.
     * @param lengthString The String to be read
     * @return The corresponding length or position in ticks
     * @throws NumberFormatException 
     */
    public static int interpretLength(String lengthString)
           throws NumberFormatException {
        int ticks = 0;
        
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
     */
    public void addArrow(String origin, Arrow arrow) {
        if(origin == null)
            initialSections.adicionaAresta(arrow);
        else
            sectionDestinations.get(origin).adicionaAresta(arrow);
    }

    /**
     * Produces a MIDI file from the composition, with the given path and name.
     * @param fileName The name of the MIDI file to be produced
     * @return
     * @throws ImprovisoException
     * @throws InvalidMidiDataException
     * @throws IOException 
     */
    public boolean execute(String fileName)
            throws ImprovisoException,
                   InvalidMidiDataException,
                   IOException {
        String currentSectionId;
        Section currentSection;
        int currentPosition = 0;
        MIDIGenerator generator = new MIDIGenerator(MIDITracks);
        generator.setOffset(offset);

        if(initialSections.getNumArrows() > 0)
            currentSectionId = initialSections.getNextDestination();
        else {
            if(sections.isEmpty())
                return false;
            else
                currentSectionId = sections.keySet().iterator().next();
        }
        System.out.println("Seção inicial: "+currentSectionId);

        do {
            currentSection = sections.get(currentSectionId);
            currentSection.setStart(currentPosition);

            generator.setCurrentTick(currentPosition);
            generator.setTempo(currentSection.getTempo());
            generator.setTimeSignature(currentSection.getTimeSignatureNumerator(), currentSection.getTimeSignatureDenominator());

            generator.addNotes(currentSection.execute());

            currentPosition = currentSection.getCurrentPosition();

            if(sectionDestinations.get(currentSectionId).getNumArrows() > 0)
                currentSectionId = sectionDestinations.get(currentSectionId).getNextDestination();
            else
                currentSectionId = null;

        } while(currentSectionId != null);

        generator.generateFile(fileName);
        return true;
    }
}