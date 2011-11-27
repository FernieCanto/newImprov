package improviso;

import java.util.*;
import org.w3c.dom.*;

/**
 * This class implements a generic, abstract Section, which is a temporal
 * division of a Composition. A Section has a determined beginning and an ending,
 * though its ending may not be initially known, depending on the concrete
 * Section being used. Sections contain Tracks, which are executed simultaneously.
 * When the section is executed, it returns the sum of all notes produced by
 * its Tracks. Sections are directly subordinated to a Composition.
 * @author Fernie Canto
 */
public abstract class Section {
    protected ArrayList<Track> tracks;
    protected Track selectedTrack;
    protected int selectedTrackIndex;
    protected int start, currentPosition;
    protected int tempo = 120;
    protected int timeSignatureNumerator = 4, timeSignatureDenominator = 4;
    protected boolean interruptTracks = false;
    
    Section() {
        this.start = 0;
        this.currentPosition = 0;
        this.tracks = new ArrayList<Track>();
    }

    /**
     * Produces a Section from a "Section" element in an XML file, as well as all
     * elements defined inside it.
     * @param XMLLib Library of XML elements
     * @param element XML element to produce the Section from
     * @return 
     */
    public static Section generateSectionXML(XMLLibrary XMLLib, Element element) {
        Section s;
        NodeList tracks;
        
        if(element.getNodeName().equals("fixedSection"))
            s = new FixedSection();
        else
            s = new VariableSection();
        s.configureSectionXML(element);
        
        tracks = element.getChildNodes();
        for(int index = 0; index < tracks.getLength(); index++) {
            if(tracks.item(index).getNodeType() == Node.ELEMENT_NODE) {
                Element trackElement = (Element)tracks.item(index);
                if(trackElement.hasAttribute("after"))
                    s.addTrack(Track.generateTrackXML(XMLLib, XMLLib.tracks.get(trackElement.getAttribute("after"))));
                else
                    s.addTrack(Track.generateTrackXML(XMLLib, trackElement));
            }
        }
        return s;
    }
    
    /**
     * Define properties of a Section from a "Section" XML element.
     * @param element 
     */
    public void configureSectionXML(Element element) {
        if(element.hasAttribute("tempo"))
            tempo = Integer.parseInt(element.getAttribute("tempo"));
    }
    
    /**
     * Return the starting point of the current execution of the Section.
     * @return Start in ticks
     */
    public int getStart() {
        return this.start;
    }
    
    /**
     * Return the tempo of the section.
     * @return Tempo in BPM
     */
    public int getTempo() {
        return this.tempo;
    }
    
    /**
     * Get the upper part (numerator) of the Section's time signature.
     * @return Numerator
     */
    public int getTimeSignatureNumerator() {
        return this.timeSignatureNumerator;
    }
    
    /**
     * Get the lower part (denominator) of the Section's time signature.
     * @return Denominator
     */
    public int getTimeSignatureDenominator() {
        return this.timeSignatureDenominator;
    }
    
    /**
     * Get the current position in ticks of the Section within the Composition.
     * @return Position in ticks
     */
    public int getCurrentPosition() {
        return this.currentPosition;
    }
    
    /**
     * Set a new starting point for the next execution of the Section.
     * @param Position Position in ticks
     */
    public void setStart(int Position) {
        this.start = Position;
        this.currentPosition = Position;
    }
    
    /**
     * Add a new Track to the section.
     * @param t Track to add
     * @return 
     */
    public boolean addTrack(Track t) {
        this.tracks.add(t);
        return true;
    }
    
    /**
     * Executes the Section, returning the list of all notes produced by all
     * Tracks of the Section. After the execution, the current position of the
     * Section is updated.
     * @return List of generated Notes
     */
    public ArrayList<Note> execute() {
        ArrayList<Note> notes = new ArrayList<Note>();
        Integer endPosition, newCurrentPosition;
    
        /* Inicializa todas as tracks */
        for(Track t : this.tracks) {
            t.initialize(start);
            this.processMessage(t.selectNextPattern());
        }
        endPosition = this.getEnd();
        
        /* Enquanto o fim da seção for desconhecido ou maior que a posição atual */
        while(endPosition == null || endPosition > currentPosition) {
            //System.out.println("Section.java: posicaoFim = "+posicaoFim+", currentPosition = "+currentPosition);
            selectedTrack = null;
            /* Buscamos a trilha que termina de executar mais cedo */
            for(int i = 0; i < this.tracks.size(); i++) {
                if(selectedTrack == null || this.tracks.get(i).getEnd() < selectedTrack.getEnd()) {
                    selectedTrack = this.tracks.get(i);
                    selectedTrackIndex = i;
                }
            }
          
            if(endPosition == null || !interruptTracks)
                notes.addAll(selectedTrack.execute(0.0));
            else
                notes.addAll(selectedTrack.execute(0.0, endPosition - selectedTrack.getCurrentPosition()));

            this.processMessage(selectedTrack.selectNextPattern());
            newCurrentPosition = selectedTrack.getCurrentPosition();
            for(Track t : this.tracks) {
                if(t.getCurrentPosition() < newCurrentPosition)
                    newCurrentPosition = t.getCurrentPosition();
            }
            currentPosition = newCurrentPosition;
            endPosition = this.getEnd();
        }
        
        return notes;
    }
    
    /**
     * Process and interpret the Message received by the executed Group, updating
     * its internal state.
     * @param message 
     */
    protected abstract void processMessage(GroupMessage message);
    /**
     * Returns the ending position of the Section. Return NULL if the ending is
     * not yet known.
     * @return 
     */
    protected abstract Integer getEnd();
}
