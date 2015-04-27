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
    public void initialize(int position) {
        end = null;
        for(int index = 0; index < finishedTracks.size(); index++) {
            finishedTracks.set(index, false);
        }
        super.initialize(position);
    }

    @Override
    protected void processMessage(GroupMessage message) {
        Integer newFinalPosition = null;
        
        if(message.finish) {
            newFinalPosition = this.selectedTrack.getEnd();
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
            if(allTracksFinished) {
                newFinalPosition = largestEnd;
            }
        }
        if((newFinalPosition != null) && ((end == null) || (newFinalPosition < end))) {
            end = newFinalPosition;
        }
    }

    @Override
    protected Integer getEnd() {
        return end;
    }
}
