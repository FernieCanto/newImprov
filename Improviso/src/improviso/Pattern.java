package improviso;

import java.util.*;
import org.w3c.dom.*;

/**
 * 
 * @author Fernie Canto
 */
public class Pattern {
    public static final int WHOLE = -1;

    private final ArrayList<NoteDefinition> noteDefinitions;
    private final NumericInterval duration;
    private int currentDuration;
    
    Pattern(NumericInterval duration) {
        this.duration = duration;
        this.noteDefinitions = new ArrayList<NoteDefinition>();
        this.currentDuration = 0;
    }
    
    public static Pattern generatePatternXML(ElementLibrary library, Element element) 
        throws ImprovisoException {
        NumericInterval patternLength = Composition.createLengthInterval(element.getAttribute("length"));
        Pattern pattern = new Pattern(patternLength);
        org.w3c.dom.NodeList noteDefinitionList = element.getChildNodes();

        for(int index = 0; index < noteDefinitionList.getLength(); index++) {
            if(noteDefinitionList.item(index).getNodeType() == Node.ELEMENT_NODE) {
                pattern.addNoteDefinition(NoteDefinition.generateNoteDefinitionXML(library, (Element)noteDefinitionList.item(index)));
            }
        }
        
        return pattern;
    }
    
    public void addNoteDefinition(NoteDefinition noteDef) {
        this.noteDefinitions.add(noteDef);
    }
    
    public ArrayList<Note> execute(int start, double initialPosition, double finalPosition, int length) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        this.currentDuration = this.duration.getValue();
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