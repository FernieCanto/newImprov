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
    protected final String id;
    protected final Group rootGroup;
    
    protected GroupMessage message;
    protected Pattern currentPattern;
    protected int currentPosition;
    protected double relativePosition = 0.0;
    
    public static class TrackBuilder {
        private String id;
        private Group rootGroup;
        
        public String getId() {
            return this.id;
        }
        
        public TrackBuilder setId(String id) {
            this.id = id;
            return this;
        }
        
        public Group getRootGroup() {
            return this.rootGroup;
        }
        
        public TrackBuilder setRootGroup(Group rootGroup) {
            this.rootGroup = rootGroup;
            return this;
        }
        
        public Track build() {
            return new Track(this);
        }
    }
    
    private Track(TrackBuilder builder) {
        this.id = builder.getId();
        this.rootGroup = builder.getRootGroup();
    }

    
    /**
     * Recovers the next Pattern to be executed by sending a message to the
     * root Group of the Track. The Message produced by the Groups will be
     * returned
     */
    public void selectNextPattern() {
        this.currentPattern = this.rootGroup.execute();
        this.message = this.rootGroup.getMessage();
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
    public ArrayList<MIDINote> execute(double newRelativePosition) {
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
    public ArrayList<MIDINote> execute(double newRelativePosition, Integer length) {
        ArrayList<MIDINote> notes = currentPattern.execute(this.currentPosition, relativePosition, newRelativePosition, length);
        this.currentPosition += currentPattern.getLength();
        relativePosition = newRelativePosition;
        return notes;
    }
}