package improviso;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 *
 * @author Fernie Canto
 */
public class XMLCompositionParser {
    private final String fileName;
    private final ElementLibrary library;

    public XMLCompositionParser(String fileName) {
        this.fileName = fileName;
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
     * @throws java.lang.CloneNotSupportedException
     */
    public Composition processXML()
            throws ImprovisoException,
            ParserConfigurationException,
            org.xml.sax.SAXException,
            IOException,
            CloneNotSupportedException {
        Composition composition;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        File XMLFile = new File(this.fileName);
        Document XMLDocument = dBuilder.parse(XMLFile);
        XMLDocument.normalizeDocument();

        this.validateXML(XMLDocument);

        Element compositionElement = XMLDocument.getDocumentElement();
        
        int offset = 0;
        Long randomSeed = null;
        if (compositionElement.hasAttribute("padding")) {
            offset = StringInterpreter.parseLength(compositionElement.getAttribute("padding"));
        }
        if (compositionElement.hasAttribute("randomSeed")) {
            randomSeed = Long.parseLong(compositionElement.getAttribute("randomSeed"));
        }
        composition = new Composition(offset, randomSeed);

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
            composition.addMIDITrack(this.generateMIDITrackXML(MIDITrackElement));
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

        this.loadStructure(composition, XMLDocument);

        return composition;
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
    }

    private MIDITrack generateMIDITrackXML(Element element) {
        return new MIDITrack(
                Integer.parseInt(element.getAttribute("channel")) - 1,
                Integer.parseInt(element.getAttribute("instrument")),
                Integer.parseInt(element.getAttribute("volume")),
                Integer.parseInt(element.getAttribute("pan"))
        );
    }

    /**
     * Produces a Section from a "Section" element in an XML file, as well as all
     * elements defined inside it.
     * @param element XML element to produce the Section from
     * @return
     * @throws improviso.ImprovisoException
     */
    private Section generateSectionXML(Element element) throws ImprovisoException {
        Section.SectionBuilder builder;
        NodeList tracks;
        
        if (element.hasAttribute("length")) {
            builder = new FixedSection.FixedSectionBuilder().setLength(StringInterpreter.createLengthInterval(element.getAttribute("length")));
        } else {
            builder = new VariableSection.VariableSectionBuilder();
        }
        builder.setId(element.getTagName());
        if (element.hasAttribute("verbose")) {
            builder.verbose();
        }
        
        if (element.hasAttribute("tempo")) {
            try {
                builder.setTempo(Integer.parseInt(element.getAttribute("tempo")));
            } catch (NumberFormatException e) {
                ImprovisoException exception = new ImprovisoException("Invalid tempo: " + element.getAttribute("tempo"));
                exception.addSuppressed(e);
                throw exception;
            }
        }
        tracks = element.getChildNodes();
        for (int index = 0; index < tracks.getLength(); index++) {
            if (tracks.item(index).getNodeType() == Node.ELEMENT_NODE) {
                Element trackElement = (Element) tracks.item(index);
                if (trackElement.hasAttribute("after")) {
                    builder.addTrack(library.getTrack(trackElement.getAttribute("after")));
                } else {
                    builder.addTrack(this.generateTrackXML(trackElement));
                }
            }
        }
        return builder.build();
    }

    private Group generateGroupXML(Element element) throws ImprovisoException {
        Group.GroupBuilder builder;
        switch (element.getAttribute("type")) {
            case "sequence":
                builder = this.configureSequenceGroup(element);
                break;
            case "random":
                builder = this.configureRandomGroup(element);
                break;
            default:
                builder = this.configureLeafGroup(element);
                break;
        }
        if (element.hasAttribute("minExecutionsSignal")) {
            builder.setFinishedSignal(new GroupSignal(Integer.parseInt(element.getAttribute("minExecutionsSignal")), Integer.parseInt(element.getAttribute("maxExecutionsSignal")), Double.parseDouble(element.getAttribute("probabilitySignal"))));
        }
        if (element.hasAttribute("minExecutionsFinish")) {
            builder.setInterruptSignal(new GroupSignal(Integer.parseInt(element.getAttribute("minExecutionsFinish")), Integer.parseInt(element.getAttribute("probabilityFinish")), Double.parseDouble(element.getAttribute("probabilityFinish"))));
        }
        return builder.build();
    }

    private LeafGroup.LeafGroupBuilder configureLeafGroup(Element element) throws ImprovisoException {
        Pattern p;
        if (element.hasAttribute("pattern")) {
            p = library.getPattern(element.getAttribute("pattern"));
        } else {
            p = this.generatePatternXML((Element) element.getElementsByTagName("pattern").item(0));
        }
        return new LeafGroup.LeafGroupBuilder().setLeafPattern(p);
    }
    
    private RandomGroup.RandomGroupBuilder configureRandomGroup(Element element) throws ImprovisoException {
        RandomGroup.RandomGroupBuilder builder = new RandomGroup.RandomGroupBuilder();
        NodeList children = element.getChildNodes();
        for (int indice = 0; indice < children.getLength(); indice++) {
            if (children.item(indice).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) children.item(indice);
                builder.addChild(this.generateGroupXML(child), child.hasAttribute("probability") ? Integer.parseInt(child.getAttribute("probability")) : null, child.hasAttribute("iterations") ? Integer.parseInt(child.getAttribute("iterations")) : null, child.hasAttribute("inertia") ? Double.parseDouble(child.getAttribute("inertia")) : null);
            }
        }
        return builder;
    }

    private SequenceGroup.SequenceGroupBuilder configureSequenceGroup(Element element) throws ImprovisoException {
        SequenceGroup.SequenceGroupBuilder builder = new SequenceGroup.SequenceGroupBuilder();
        NodeList children = element.getChildNodes();
        for (int indice = 0; indice < children.getLength(); indice++) {
            if (children.item(indice).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) children.item(indice);
                builder.addChild(this.generateGroupXML(child), child.hasAttribute("iterations") ? Integer.parseInt(child.getAttribute("iterations")) : null, child.hasAttribute("inertia") ? Double.parseDouble(child.getAttribute("inertia")) : null);
            }
        }
        return builder;
    }

    private Pattern generatePatternXML(Element element)
            throws ImprovisoException {
        Pattern.PatternBuilder builder = new Pattern.PatternBuilder()
                .setId(element.getTagName())
                .setDuration(
                        StringInterpreter.createLengthInterval(
                                element.getAttribute("length")
                        )
                );
        NodeList noteDefinitionList = element.getElementsByTagName("note");
        for (int index = 0; index < noteDefinitionList.getLength(); index++) {
            builder.addNote(this.generateNoteDefinitionXML(
                            (Element) noteDefinitionList.item(index)
                    )
            );
        }
        return builder.build();
    }

    private Note generateNoteDefinitionXML(Element element) throws ImprovisoException {
        Note.NoteBuilder builder = new Note.NoteBuilder().setPitch(Note.interpretNoteName(library, element.getFirstChild().getNodeValue()));
        if (element.hasAttribute("relativeStart")) {
            builder.setRelativeStart(StringInterpreter.createDoubleInterval(element.getAttribute("relativeStart")));
        } else if (element.hasAttribute("start")) {
            builder.setStart(StringInterpreter.createLengthInterval(element.getAttribute("start")));
        }
        if (element.hasAttribute("relativeLength")) {
            builder.setRelativeLength(StringInterpreter.createDoubleInterval(element.getAttribute("relativeLength")));
        } else if (element.hasAttribute("length")) {
            builder.setLength(StringInterpreter.createLengthInterval(element.getAttribute("length")));
        }
        if (element.hasAttribute("velocity")) {
            builder.setVelocity(StringInterpreter.createNumericInterval(element.getAttribute("velocity")));
        }
        if (element.hasAttribute("track")) {
            builder.setMIDITrack(Integer.parseInt(element.getAttribute("track")));
        }
        if (element.hasAttribute("probability")) {
            builder.setProbability(Double.parseDouble(element.getAttribute("probability")));
        }
        if (element.hasAttribute("transposition")) {
            builder.setTransposition(StringInterpreter.createNumericInterval(element.getAttribute("transposition")));
        }
        return builder.build();
    }
    
    /**
     * Produces a Track from a "Track" XML element. All underlying elements will
     * be generated as well.
     * @param XMLLib Library of XML elements
     * @param element "Track" element to be processed
     * @return
     */
    private Track generateTrackXML(Element element) throws ImprovisoException {
        Group rootGroup;
        if (element.hasAttribute("group")) {
            rootGroup = library.getGroup(element.getAttribute("group"));
        } else if (element.getChildNodes().item(0).getNodeType() == Node.ELEMENT_NODE) {
            rootGroup = this.generateGroupXML((Element) element.getChildNodes().item(0));
        } else if (element.getChildNodes().item(1).getNodeType() == Node.ELEMENT_NODE) {
            rootGroup = this.generateGroupXML((Element) element.getChildNodes().item(1));
        } else {
            throw new ImprovisoException("No group associated with this track");
        }
        Track.TrackBuilder builder = new Track.TrackBuilder().setId(element.getTagName()).setRootGroup(rootGroup);
        return builder.build();
    }

    private void loadStructure(Composition composition, Document XMLDocument)
            throws ImprovisoException, CloneNotSupportedException {
        Element structureElement = (Element) XMLDocument.getElementsByTagName("structure").item(0);
        NodeList sectionElements = structureElement.getElementsByTagName("section");
        for (int index = 0; index < sectionElements.getLength(); index++) {
            Element sectionElement = (Element) sectionElements.item(index);
            if (!this.library.hasSection(sectionElement.getAttribute("after"))) {
                throw new ImprovisoException("Invalid section: " + sectionElement.getAttribute("after"));
            }
            composition.addSection(sectionElement.getAttribute("id"), this.library.getSection(sectionElement.getAttribute("after")));

            NodeList arrows = sectionElement.getElementsByTagName("arrow");
            for (int index2 = 0; index2 < arrows.getLength(); index2++) {
                Element arrowElement = (Element) arrows.item(index2);
                Arrow a = this.generateArrowXML(arrowElement);
                composition.addArrow(sectionElement.getAttribute("id"), a);
            }
        }
    }

    private Arrow generateArrowXML(Element arrowElement) {
        Arrow.ArrowBuilder arrowBuilder = new Arrow.ArrowBuilder();
        if (arrowElement.hasAttribute("to")) {
            arrowBuilder.setDestinationSection(arrowElement.getAttribute("to"));
        }
        if (arrowElement.hasAttribute("probability")) {
            arrowBuilder.setProbability(Integer.parseInt(arrowElement.getAttribute("probability")));
        }
        if (arrowElement.hasAttribute("maxExecutions")) {
            arrowBuilder.setMaxExecutions(Integer.parseInt(arrowElement.getAttribute("maxExecutions")));
        }
        if (arrowElement.hasAttribute("finishAfterMax")) {
            arrowBuilder.setEndCompositionAfterMax(true);
        }
        return arrowBuilder.build();
    }

    private void loadSections(Node sectionList) throws ImprovisoException {
        for (int index = 0; index < sectionList.getChildNodes().getLength(); index++) {
            Node sectionNode = sectionList.getChildNodes().item(index);
            if (sectionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sectionElement = (Element) sectionNode;
                String sectionId = sectionElement.getTagName();
                this.library.addSection(sectionId, this.generateSectionXML(sectionElement));
            }
        }
    }

    private void loadTracks(Node trackList) throws ImprovisoException {
        for (int index = 0; index < trackList.getChildNodes().getLength(); index++) {
            Node trackNode = trackList.getChildNodes().item(index);
            if (trackNode.getNodeType() == Node.ELEMENT_NODE) {
                Element trackElement = (Element) trackNode;
                String trackId = trackElement.getTagName();
                this.library.addTrack(trackId, generateTrackXML(trackElement));
            }
        }
    }

    private void loadGroups(Node groupList) throws ImprovisoException {
        for (int index = 0; index < groupList.getChildNodes().getLength(); index++) {
            Node groupNode = groupList.getChildNodes().item(index);
            if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
                Element groupElement = (Element) groupNode;
                String groupId = groupElement.getTagName();
                this.library.addGroup(groupId, generateGroupXML(groupElement));
            }
        }
    }

    private void loadPatterns(Node patternList) throws ImprovisoException {
        for (int index = 0; index < patternList.getChildNodes().getLength(); index++) {
            Node patternNode = patternList.getChildNodes().item(index);
            if (patternNode.getNodeType() == Node.ELEMENT_NODE) {
                Element patternElement = (Element) patternNode;
                String patternId = patternElement.getTagName();
                this.library.addPattern(patternId, this.generatePatternXML(patternElement));
            }
        }
    }
}
