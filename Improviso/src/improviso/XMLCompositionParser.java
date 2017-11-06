package improviso;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Fernie Canto
 */
public class XMLCompositionParser {

    private final String fileName;
    private final Composition composition;
    private final ElementLibrary library;

    public XMLCompositionParser(String fileName) {
        this.fileName = fileName;
        this.composition = new Composition();
        this.library = new ElementLibrary();
    }

    /**
     * Read an XML file and produces a Composition from it.
     *
     * @return The generated Composition
     * @throws ImprovisoException
     * @throws ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws IOException
     */
    public Composition processXML()
            throws ImprovisoException,
            ParserConfigurationException,
            org.xml.sax.SAXException,
            IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        File XMLFile = new File(this.fileName);
        Document XMLDocument = dBuilder.parse(XMLFile);
        XMLDocument.normalizeDocument();

        this.validateXML(XMLDocument);

        Element compositionElement = XMLDocument.getDocumentElement();
        if (compositionElement.hasAttribute("padding")) {
            this.composition.setOffset(StringInterpreter.parseLength(compositionElement.getAttribute("padding")));
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
        for (int index = 0; index < aliasList.getLength(); index++) {
            Element aliasElement = (Element) aliasList.item(index);
            this.library.addNoteAlias(aliasElement.getFirstChild().getNodeValue().trim(), Integer.parseInt(aliasElement.getAttribute("note")));
        }

        NodeList MIDITracks = XMLDocument.getElementsByTagName("MIDITrack");
        for (int index = 0; index < MIDITracks.getLength(); index++) {
            Element MIDITrackElement = (Element) MIDITracks.item(index);
            this.composition.addMIDITrack(MIDITrack.generateMIDITrackXML(MIDITrackElement));
        }

        Node patternList = XMLDocument.getElementsByTagName("patternList").item(0);
        if (patternList != null) {
            this.loadPatterns(patternList);
        }

        Node groupList = XMLDocument.getElementsByTagName("groupList").item(0);
        if (groupList != null) {
            this.loadGroups(groupList);
        }

        Node trackList = XMLDocument.getElementsByTagName("trackList").item(0);
        if (trackList != null) {
            this.loadTracks(trackList);
        }

        Node sectionList = XMLDocument.getElementsByTagName("sectionList").item(0);
        if (sectionList != null) {
            this.loadSections(sectionList);
        }

        this.loadStructure(XMLDocument);

        return this.composition;
    }

    private void validateXML(Document XMLDocument)
            throws ImprovisoException {
        if (!XMLDocument.getDocumentElement().getTagName().equals("composition")) {
            throw new ImprovisoException("The root element of the composition file must be of the type 'composition'");
        }
        if (XMLDocument.getElementsByTagName("MIDITrack").getLength() == 0) {
            throw new ImprovisoException("The composition must have at least one MIDI track");
        }
        if (XMLDocument.getElementsByTagName("patternList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one pattern list");
        }
        if (XMLDocument.getElementsByTagName("groupList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one group list");
        }
        if (XMLDocument.getElementsByTagName("trackList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one track list");
        }
        if (XMLDocument.getElementsByTagName("sectionList").getLength() > 1) {
            throw new ImprovisoException("The composition can't have more than one section list");
        }
        if (XMLDocument.getElementsByTagName("structure").getLength() != 1) {
            throw new ImprovisoException("The composition must have exactly one 'structure' tag");
        }

        Element structure = (Element) XMLDocument.getElementsByTagName("structure").item(0);
        if (structure.getElementsByTagName("section").getLength() == 0) {
            throw new ImprovisoException("The structure of the composition must have at least one section");
        }
        
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    }

    private void loadStructure(Document XMLDocument) throws ImprovisoException {
        Element structureElement = (Element) XMLDocument.getElementsByTagName("structure").item(0);
        NodeList sectionElements = structureElement.getElementsByTagName("section");
        for (int index = 0; index < sectionElements.getLength(); index++) {
            Element sectionElement = (Element) sectionElements.item(index);
            Section section = this.library.getSection(sectionElement.getAttribute("after"));
            if (section == null) {
                throw new ImprovisoException("Invalid section: " + sectionElement.getAttribute("after"));
            }
            this.composition.addSection(sectionElement.getAttribute("id"), section);

            NodeList arrows = sectionElement.getElementsByTagName("arrow");
            for (int index2 = 0; index2 < arrows.getLength(); index2++) {
                Element arrowElement = (Element) arrows.item(index2);
                Arrow a = Arrow.generateArrowXML(arrowElement);
                this.composition.addArrow(sectionElement.getAttribute("id"), a);
            }
        }
    }

    private void loadSections(Node sectionList) throws ImprovisoException {
        for (int index = 0; index < sectionList.getChildNodes().getLength(); index++) {
            Node sectionNode = sectionList.getChildNodes().item(index);
            if (sectionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sectionElement = (Element) sectionNode;
                String sectionId = sectionElement.getTagName();
                this.library.addSection(sectionId, Section.generateSectionXML(this.library, sectionElement));
            }
        }
    }

    private void loadTracks(Node trackList) throws ImprovisoException {
        for (int index = 0; index < trackList.getChildNodes().getLength(); index++) {
            Node trackNode = trackList.getChildNodes().item(index);
            if (trackNode.getNodeType() == Node.ELEMENT_NODE) {
                Element trackElement = (Element) trackNode;
                String trackId = trackElement.getTagName();
                this.library.addTrack(trackId, Track.generateTrackXML(this.library, trackElement));
            }
        }
    }

    private void loadGroups(Node groupList) throws ImprovisoException {
        for (int index = 0; index < groupList.getChildNodes().getLength(); index++) {
            Node groupNode = groupList.getChildNodes().item(index);
            if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
                Element groupElement = (Element) groupNode;
                String groupId = groupElement.getTagName();
                this.library.addGroup(groupId, Group.generateGroupXML(this.library, groupElement));
            }
        }
    }

    private void loadPatterns(Node patternList) throws ImprovisoException {
        for (int index = 0; index < patternList.getChildNodes().getLength(); index++) {
            Node patternNode = patternList.getChildNodes().item(index);
            if (patternNode.getNodeType() == Node.ELEMENT_NODE) {
                Element patternElement = (Element) patternNode;
                String patternId = patternElement.getTagName();
                this.library.addPattern(patternId, Pattern.generatePatternXML(this.library, patternElement));
            }
        }
    }
}
