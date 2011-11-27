package improviso;

/**
 *
 * @author fernando
 */
public class SequenceGroup extends RepetitionGroup {
    protected int currentIndex = 0;
    protected boolean resetOrder = true;
    
    SequenceGroup() {
        super();
    }
    
    @Override
    public void resetGroup() {
        if(resetOrder) {
            currentIndex = 0;
            selectedGroup = null;
        }
        super.resetGroup();
    }
    
    @Override
    public boolean selectNextGroup() {
        selectedGroup = children.get(currentIndex);
        selectedGroupIndex = currentIndex;

        currentIndex++;
        if(currentIndex == children.size())
            currentIndex = 0;
        return true;
    }
}
