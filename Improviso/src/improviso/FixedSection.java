package improviso;
import org.w3c.dom.*;
/**
 *
 * @author fernando
 */
public class FixedSection extends Section {
    int length;
    
    FixedSection() {
        super();
    }
    
    @Override
    public void configureSectionXML(Element element)
        throws ImprovisoException {
        length = Composition.interpretLength(element.getAttribute("length"));
        
        if(element.hasAttribute("tempo"))
            tempo = Integer.parseInt(element.getAttribute("tempo"));
    }
    
    /**
     * Defines whether the Patterns should be cut short at the ending position
     * of the Section, or allowed to be executed in their entirety.
     * @param interrupt 
     */
    public void setInterrupt(boolean interrupt) {
        interruptTracks = interrupt;
    }
    
    @Override
    protected void processMessage(GroupMessage mensagem) {
        return;
    }
    
    @Override
    protected Integer getEnd() {
        return start + length;
    }
}