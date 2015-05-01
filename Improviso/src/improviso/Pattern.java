package improviso;

import java.util.*;
import org.w3c.dom.*;

/**
 * 
 * @author Fernie Canto
 */
public class Pattern {
    public static final int WHOLE = -1;

    protected String id;
    private final ArrayList<NoteDefinition> noteDefinitions;
    private final NumericInterval duration;
    private Integer currentDuration;
    
    Pattern(NumericInterval duration) {
        this.duration = duration;
        this.noteDefinitions = new ArrayList<NoteDefinition>();
        this.currentDuration = null;
    }
    
    public static Pattern generatePatternXML(ElementLibrary library, Element element) 
        throws ImprovisoException {
        NumericInterval patternLength = Composition.createLengthInterval(element.getAttribute("length"));
        Pattern pattern = new Pattern(patternLength);
        pattern.configurePatternXML(element);
        NodeList noteDefinitionList = element.getElementsByTagName("note");

        for(int index = 0; index < noteDefinitionList.getLength(); index++) {
            pattern.addNoteDefinition(NoteDefinition.generateNoteDefinitionXML(library, (Element)noteDefinitionList.item(index)));
        }
        
        return pattern;
    }

    private void configurePatternXML(Element element) {
        this.id = element.getTagName();
    }
    
    public void addNoteDefinition(NoteDefinition noteDef) {
        this.noteDefinitions.add(noteDef);
    }
    
    public void initialize() {
        this.currentDuration = this.duration.getValue();
    }
    
    public ArrayList<Note> execute(int start, double initialPosition, double finalPosition, int length) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        for(NoteDefinition def : this.noteDefinitions) {
            if(length == Pattern.WHOLE)
                noteList.add(def.generateNote(start, this.currentDuration, finalPosition));
            else {
                noteList.add(def.generateNote(start, this.currentDuration, finalPosition, length));
            }
        }
 
        return noteList;
    }
    
    public int getLength() {
        return this.currentDuration;
    }
}