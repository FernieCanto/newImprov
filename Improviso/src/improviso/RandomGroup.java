/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;
import java.util.*;
/**
 *
 * @author fernando
 */
public class RandomGroup extends RepetitionGroup {
    protected int childProbability = 1;
    private ArrayList<Integer> accumulatedProbabilities = new ArrayList<Integer>();
    private int maxProbability = 0;
    
    RandomGroup() {
        super();
    }
    
    @Override
    public void configureGroupXML(org.w3c.dom.Element element) {
        if(element.hasAttribute("probability"))
            childProbability = Integer.parseInt(element.getAttribute("probability"));
        
        super.configureGroupXML(element);
    }
    
    @Override
    public boolean selectNextGroup() {
        int selection;
        int index = 0;

        selection = this.rand.nextInt(this.maxProbability);
        for(int probability : this.accumulatedProbabilities) {
            if(selection < probability)
                break;
            else
                index++;
        }
        this.selectedGroup = this.children.get(index);
        this.selectedGroupIndex = index;
        return true;
    }

    @Override
    public boolean addChild(Group g) {
        if(super.addChild(g)) {
            this.maxProbability += childProbability;
            this.accumulatedProbabilities.add(new Integer(this.maxProbability));
            return true;
        }
        else {
            return false;
        }
    }
}