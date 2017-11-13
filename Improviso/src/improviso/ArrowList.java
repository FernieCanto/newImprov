package improviso;
import java.util.*;

/**
 *
 * @author fernando
 */
public class ArrowList {
    final private ArrayList<Arrow> arrows;
    final private ArrayList<Integer> accumulatedProbabilities;
    private int maxProbabilities;
    
    public ArrowList() {
        arrows = new ArrayList<>();
        accumulatedProbabilities = new ArrayList<>();
        maxProbabilities = 0;
    }
    
    public void addArrow(Arrow a) {
        arrows.add(a);
        maxProbabilities += a.getProbability();
        accumulatedProbabilities.add(maxProbabilities);
    }
    
    public int getNumArrows() {
        return this.arrows.size();
    }
    
    protected void recalculateProbabilities() {
        maxProbabilities = 0;
        accumulatedProbabilities.clear();
        
        for(Arrow a : arrows) {
            maxProbabilities += a.getProbability();
            accumulatedProbabilities.add(maxProbabilities);
        }
    }
    
    public String getNextDestination(Random random) {
        int selection;
        int index = 0;
        String destination;
        
        if(arrows.isEmpty()) {
            return null;
        }
        
        selection = random.nextInt(maxProbabilities);
        for(Integer prob : accumulatedProbabilities) {
            if(selection < prob) {
                break;
            } else {
                index++;
            }
        }
        destination = arrows.get(index).execute();
        if(!arrows.get(index).getIsActive()) {
            arrows.remove(index);
        }
        return destination;
    }
}