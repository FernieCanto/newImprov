package improviso;

/**
 *
 * @author fernando
 */
public class SequenceGroup extends RepetitionGroup {
    protected int currentIndex = 0;
    
    SequenceGroup() {
        super();
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
