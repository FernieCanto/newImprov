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
    int length;
    
    Pattern(int length) {
        this.length = length;
        this.noteDefinitions = new ArrayList<NoteDefinition>();
    }
    
    public static Pattern generatePatternXML(XMLLibrary XMLLib, Element element) {
        int patternLength = Composition.interpretLength(element.getAttribute("length"));
        Pattern pattern = new Pattern(patternLength);
        org.w3c.dom.NodeList noteDefinitionList;
        
        NoteDefinition.defaultStart = NoteDefinition.defaultMaxStart = NoteDefinition.defaultStartEnd = NoteDefinition.defaultMaxStartEnd = 0;
        NoteDefinition.defaultLength = NoteDefinition.defaultMaxLength = NoteDefinition.defaultLengthEnd = NoteDefinition.defaultMaxLengthEnd = 60;
        NoteDefinition.defaultVelocity = NoteDefinition.defaultMaxVelocity = NoteDefinition.defaultVelocityEnd = NoteDefinition.defaultMaxVelocityEnd = 100;
                
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
    
    public ArrayList<Note> generateNotes(int start, double position, int length) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        for(NoteDefinition def : this.noteDefinitions) {
          if(length == Pattern.WHOLE)
            noteList.add(def.generateNote(start, position));
          else {
            noteList.add(def.generateNote(start, position, length));
          }
        }
        return noteList;
    }
    
    public int getLength() {
        return this.length;
    }
}