package improviso;

import java.util.*;
import org.w3c.dom.*;

/**
 *
 * @author fernando
 */
public class Pattern {
    public static final int WHOLE = -1;
  
    ArrayList<NoteDefinition> noteDefinitions;
    NumericInterval duration;
    int currentDuration = 0;
    
    Pattern(NumericInterval duration) {
        this.duration = duration;
        this.noteDefinitions = new ArrayList<NoteDefinition>();
    }
    
    public static Pattern generatePatternXML(XMLLibrary XMLLib, Element element) 
        throws ImprovisoException {
        NumericInterval patternLength = Composition.createLengthInterval(element.getAttribute("length"));
        Pattern pattern = new Pattern(patternLength);
        org.w3c.dom.NodeList noteDefinitionList;
        
        /*
        NoteDefinition.defaultStart = NoteDefinition.defaultMaxStart = NoteDefinition.defaultStartEnd = NoteDefinition.defaultMaxStartEnd = 0;
        NoteDefinition.defaultLength = NoteDefinition.defaultMaxLength = NoteDefinition.defaultLengthEnd = NoteDefinition.defaultMaxLengthEnd = 60;
        NoteDefinition.defaultVelocity = NoteDefinition.defaultMaxVelocity = NoteDefinition.defaultVelocityEnd = NoteDefinition.defaultMaxVelocityEnd = 100; */
                
        noteDefinitionList = element.getChildNodes();
        for(int index = 0; index < noteDefinitionList.getLength(); index++) {
            if(noteDefinitionList.item(index).getNodeType() == Node.ELEMENT_NODE)
                pattern.addNoteDefinition(NoteDefinition.generateNoteDefinitionXML(XMLLib, (Element)noteDefinitionList.item(index)));
        }
        
        return pattern;
    }
    
    public void addNoteDefinition(NoteDefinition noteDef) {
        this.noteDefinitions.add(noteDef);
    }
    
    public ArrayList<Note> execute(int start, double initialPosition, double finalPosition, int length) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        currentDuration = this.duration.getValue();
        for(NoteDefinition def : this.noteDefinitions) {
          if(length == Pattern.WHOLE)
            noteList.add(def.generateNote(start, currentDuration, finalPosition));
          else {
            noteList.add(def.generateNote(start, currentDuration, finalPosition, length));
          }
        }
        return noteList;
    }
    
    public int getLength() {
        return this.currentDuration;
    }
}