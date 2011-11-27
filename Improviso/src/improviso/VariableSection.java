package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class VariableSection extends Section {
    protected ArrayList<Boolean> finishedTracks;
    Integer end = null;
    
    VariableSection() {
        super();
        end = null;
        finishedTracks = new ArrayList<Boolean>();
    }
    
    @Override
    public boolean addTrack(Track t) {
        finishedTracks.add(false);
        return super.addTrack(t);
    }

    @Override
    protected void processMessage(GroupMessage message) {
        Integer newFinalPosition = null;
        if(message.finish) {
            newFinalPosition = new Integer(this.selectedTrack.getEnd());
        }
        else if(message.signal) {
            int largestEnd = 0;
            boolean allTracksFinished = true;
            
            finishedTracks.set(selectedTrackIndex, true);
            for(int i = 0; i < tracks.size(); i++) {
                if(finishedTracks.get(i)) {
                    if(tracks.get(i).getEnd() > largestEnd)
                        largestEnd = tracks.get(i).getEnd();
                }
                else
                    allTracksFinished = false;
            }
            if(allTracksFinished)
                newFinalPosition = new Integer(largestEnd);
        }
        if((newFinalPosition != null) && ((end == null) || (newFinalPosition < end)))
            end = newFinalPosition;
    }

    @Override
    protected Integer getEnd() {
        return end;
    }
}
