package improviso;
import java.util.*;

/**
 *
 * @author fernando
 */
public class ArrowList {
    ArrayList<Arrow> arrows;
    ArrayList<Integer> accumulatedProbabilities;
    int maxProbabilities;
    Random rand = null;
    
    public ArrowList() {
        arrows = new ArrayList<Arrow>();
        accumulatedProbabilities = new ArrayList<Integer>();
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
    
    public void setSeed() {
        rand = new Random();
    }
    
    public void setSeed(long seed) {
        if(rand == null)
            rand = new Random(seed);
        else
            rand.setSeed(seed);
    }
    
    protected void recalculateProbabilities() {
        maxProbabilities = 0;
        accumulatedProbabilities.clear();
        
        for(Arrow a : arrows) {
            maxProbabilities += a.getProbability();
            accumulatedProbabilities.add(maxProbabilities);
        }
    }
    
    public String getNextDestination() {
        int selection;
        int index = 0;
        String destination;
        
        if(arrows.isEmpty()) {
            return null;
        }
        
        if(rand == null) {
            setSeed();
        }
        
        selection = rand.nextInt(maxProbabilities);
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