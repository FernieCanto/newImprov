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
    private ArrayList<Integer> accumulatedProbabilities = new ArrayList<>();
    private int maxProbability = 0;
    
    public static class RandomGroupBuilder extends RepetitionGroup.RepetitionGroupBuilder {
        private final ArrayList<Integer> accumulatedProbs = new ArrayList<>();
        private int maxProb = 0;
        
        public ArrayList<Integer> getAccumulatedProgs() {
            return this.accumulatedProbs;
        }
        
        public Integer getMaxProb() {
            return this.maxProb;
        }
        
        public RandomGroupBuilder addChild(Group child, Integer prob, Integer iterations, Double inertia) {
            this.maxProb += (prob != null ? prob : 1);
            this.accumulatedProbs.add(this.maxProb);
            super.addChild(child, iterations, inertia);
            return this;
        }
        
        @Override
        public RandomGroup build() {
            return new RandomGroup(this);
        }
    }
    
    private RandomGroup(RandomGroupBuilder builder) {
        super(builder);
        this.accumulatedProbabilities = builder.getAccumulatedProgs();
        this.maxProbability = builder.getMaxProb();
    }
    
    @Override
    protected boolean selectNextGroup(Random rand) {
        int selection;
        int index = 0;

        selection = rand.nextInt(this.maxProbability);
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
}