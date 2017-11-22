package improviso;
import java.util.*;

/**
 *
 * @author fernando
 */
public class ArrowList {
    final private ArrayList<Arrow> arrows;
    final private LinkedHashMap<Arrow, Integer> accumulatedProbabilities;
    private int maxProbabilities;
    boolean verbose = false;
    
    public ArrowList() {
        arrows = new ArrayList<>();
        accumulatedProbabilities = new LinkedHashMap<>();
        maxProbabilities = 0;
    }
    
    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
        maxProbabilities += arrow.getProbability();
        accumulatedProbabilities.put(arrow, maxProbabilities);
    }
    
    public void verbose() {
        this.verbose = true;
    }
    
    public boolean isEmpty() {
        return this.arrows.isEmpty();
    }
    
    public int getNumArrows() {
        return this.arrows.size();
    }
    
    private void calculateProbabilities() {
        maxProbabilities = 0;
        accumulatedProbabilities.clear();
        
        this.arrows.forEach((arrow) -> {
            maxProbabilities += arrow.getProbability();
            accumulatedProbabilities.put(arrow, maxProbabilities);
        });
    }
    
    public String getNextDestination(Random random) {
        if(arrows.isEmpty()) {
            return null;
        }
        
        Arrow selectedArrow = this.selectArrow(random.nextInt());
        String destination = selectedArrow.execute();
        if(!selectedArrow.isActive()) {
            arrows.remove(selectedArrow);
            this.calculateProbabilities();
        }
        return destination;
    }

    private Arrow selectArrow(int selection) {
        Arrow selectedArrow = this.arrows.get(0);
        
        for(Arrow arrow : this.arrows) {
            if(selection < this.accumulatedProbabilities.get(arrow)) {
                selectedArrow = arrow;
                break;
            }
        }
        return selectedArrow;
    }
}