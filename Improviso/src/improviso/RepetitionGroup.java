package improviso;

import java.util.*;
/**
 * Implementa um grupo cujos grupos children podem repetir
 * de acordo com certos parâmetros:
 * @author fernando
 */
public abstract class RepetitionGroup extends Group {
    HashMap<Group, Integer> iterationsMap = new HashMap<>();
    HashMap<Group, Double> inertiaMap = new HashMap<>();
    ArrayList<Integer> iterations = new ArrayList<>();
    ArrayList<Double> inertia = new ArrayList<>();
    int currentIterations = 0;
    protected boolean resetIterations = true;
    Group selectedGroup = null;
    
    abstract public static class RepetitionGroupBuilder extends Group.GroupBuilder {
        final private HashMap<Group, Integer> iterationsMap = new HashMap<>();
        final private HashMap<Group, Double> inertiaMap = new HashMap<>();
        final private ArrayList<Integer> iterations = new ArrayList<>();
        final private ArrayList<Double> inertia = new ArrayList<>();
        
        public HashMap<Group, Integer> getIterationsMap() {
            return this.iterationsMap;
        }
        
        public HashMap<Group, Double> getInertiaMap() {
            return this.inertiaMap;
        }
        
        public ArrayList<Integer> getIterations() {
            return this.iterations;
        }
        
        public ArrayList<Double> getInertia() {
            return this.inertia;
        }
        
        public RepetitionGroupBuilder addChild(Group child, Integer iterations, Double inertia) {
            this.iterations.add(iterations != null ? iterations : 0);
            this.iterationsMap.put(child, iterations != null ? iterations : 0);
            this.inertia.add(inertia != null ? inertia : 0.0);
            this.inertiaMap.put(child, inertia != null ? inertia : 0.0);
            super.addChild(child);
            return this;
        }
    }
    
    protected RepetitionGroup(RepetitionGroupBuilder builder) {
        super(builder);
        this.iterations = builder.getIterations();
        this.inertia = builder.getInertia();
        this.iterationsMap = builder.getIterationsMap();
        this.inertiaMap = builder.getInertiaMap();
    }
    
    public Group selectGroup(Random rand) {
        if(selectedGroup != null) {
            if(iterations.get(this.selectedGroupIndex) > currentIterations) {
                currentIterations++;
                return selectedGroup;
            }
            if(rand.nextFloat() < inertia.get(this.selectedGroupIndex))
                return selectedGroup;
        }
        currentIterations = 1;
        this.selectNextGroup(rand);
        return selectedGroup;
    }
    
    @Override
    protected Pattern selectPattern(Random rand) {
        selectedGroup = this.selectGroup(rand);
        return selectedGroup.execute();
    }
    
    @Override
    protected GroupMessage generateMessage() {
        return selectedGroup.getMessage();
    }
    
    @Override
    public void resetGroup() {
        if(resetIterations)
            currentIterations = 0;
        super.resetGroup();
    }
    
    protected abstract boolean selectNextGroup(Random rand);
}
