package improviso;

import java.util.*;
/**
 * Implementa um grupo cujos grupos children podem repetir
 * de acordo com certos parâmetros:
 * @author fernando
 */
public abstract class RepetitionGroup extends Group {
    public final int DEFAULT_ITERATIONS = 0;
    public final float DEFAULT_INERTIA = 0;
    
    int childIterations = DEFAULT_ITERATIONS; float childInertia = DEFAULT_INERTIA;
    ArrayList<Integer> iterations = new ArrayList<Integer>();
    ArrayList<Float> inertia = new ArrayList<Float>();
    int currentIterations = 0;
    protected boolean resetIterations = true;
    Group selectedGroup = null;
    
    RepetitionGroup() {
        super();
    }
    
    @Override
    public void configureGroupXML(org.w3c.dom.Element element) {
        if(element.hasAttribute("iterations"))
            childIterations = Integer.parseInt(element.getAttribute("iterations"));
        else
            childIterations = DEFAULT_ITERATIONS;

        if(element.hasAttribute("inertia"))
            childInertia = Float.parseFloat(element.getAttribute("inertia"));
        else
            childInertia = DEFAULT_INERTIA;

        super.configureGroupXML(element);
    }
    
    @Override
    public boolean addChild(Group G) {
        super.addChild(G);
        this.iterations.add(childIterations);
        this.inertia.add(childInertia);
        return true;
    }
    
    @Override
    public Group selectGroup() {
        if(rand == null)
            this.setSeed();
        
        if(selectedGroup != null) {
            if(iterations.get(this.selectedGroupIndex) > currentIterations) {
                currentIterations++;
                return selectedGroup;
            }
            if(rand.nextFloat() < inertia.get(this.selectedGroupIndex))
                return selectedGroup;
        }
        currentIterations = 1;
        this.selectNextGroup();
        return selectedGroup;
    }
    
    @Override
    public Pattern getSelectedPattern() {
        if(this.selectedGroup != null)
            return this.selectedGroup.getSelectedPattern();
        else
            return null;
    }
    
    @Override
    public void resetGroup() {
        if(resetIterations)
            currentIterations = 0;
        super.resetGroup();
    }
    
    protected abstract boolean selectNextGroup();
}
