package improviso;

import java.util.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * RESPONSABILIDADES DA TRILHA
 *  - Buscar próximo padrão e devolver noteDefinitions (todas, ou com determinada duração)
 *  - Identificar corretamente o fim de sua execução
 * @author fernando
 */
public class Track {
    protected String id;
    protected GroupMessage message;
    protected Pattern currentPattern;
    protected Group rootGroup;
    protected int currentPosition;
    protected double relativePosition = 0.0;
   
    Track(Group g) {
        this.rootGroup = g;
        this.currentPosition = 0;
    }

    /**
     * Produces a Track from a "Track" XML element. All underlying elements will
     * be generated as well.
     * @param XMLLib Library of XML elements
     * @param element "Track" element to be processed
     * @return 
     */
    static Track generateTrackXML(ElementLibrary library, Element element)
        throws ImprovisoException {
        Track t;
        Group g;
        
        if(element.hasAttribute("group")) {
            g = library.getGroup(element.getAttribute("group"));
        } else if(element.getChildNodes().item(0).getNodeType() == Node.ELEMENT_NODE) {
            g = Group.generateGroupXML(library, (Element)element.getChildNodes().item(0));
        } else if(element.getChildNodes().item(1).getNodeType() == Node.ELEMENT_NODE) {
            g = Group.generateGroupXML(library, (Element)element.getChildNodes().item(1));
        } else {
            throw new ImprovisoException("No group associated with this track");
        }
        t = new Track(g);
        t.configureTrackXML(element);
        
        return t;
    }

    private void configureTrackXML(Element element) {
        this.id = element.getTagName();
    }
    
    /**
     * Recovers the next Pattern to be executed by sending a message to the
     * root Group of the Track. The Message produced by the Groups will be
     * returned
     */
    public void selectNextPattern() {
        this.message = this.rootGroup.execute();
        this.currentPattern = this.rootGroup.getSelectedPattern();
        this.currentPattern.initialize();
    }
    
    /**
     * Prepares the Track for a new execution of its Section, updating its
     * current position and resetting its Group tree.
     * @param position 
     */
    public void initialize(int position) {
        this.currentPosition = position;
        this.relativePosition = 0.0;
        this.rootGroup.resetGroup();
    }
    
    public String getId() {
        return this.id;
    }
    
    /**
     * Get the Track's current position within the Composition.
     * @return Position in ticks
     */
    public int getCurrentPosition() {
        return this.currentPosition;
    }
    
    public GroupMessage getMessage() {
        return this.message;
    }
    
    /**
     * Obtains the ending position of the currently selected Pattern.
     * @return 
     */
    public int getEnd() {
        return this.currentPosition + this.currentPattern.getLength();
    }
    
    /**
     * Fully executes the last selected Pattern of the Track, given the Track's
     * current position within the Section, receiving the list of Notes produced
     * by the Pattern.
     * @param newRelativePosition The position of the Track in the Section.
     * @return 
     */
    public ArrayList<Note> execute(double newRelativePosition) {
        return this.execute(newRelativePosition, null);
    }
    
    /**
     * Executes the last selected Pattern of the Track, given the Track's
     * current position within the Section and the maximum allowed duration for
     * the Pattern, receiving the list of Notes produced by the Pattern.
     * @param newRelativePosition The position of the Track in the Section.
     * @param length The maximum duration for the Pattern. All notes that exceed
     * that duration will be discarded.
     * @return Sequência de noteDefinitions geradas.
     */
    public ArrayList<Note> execute(double newRelativePosition, Integer length) {
        ArrayList<Note> notes = currentPattern.execute(this.currentPosition, relativePosition, newRelativePosition, length);
        this.currentPosition += currentPattern.getLength();
        relativePosition = newRelativePosition;
        return notes;
    }
}