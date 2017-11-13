package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class VariableSection extends Section {
    protected ArrayList<Boolean> finishedTracks;
    Integer end = null;
    
    public static class VariableSectionBuilder extends Section.SectionBuilder {
        @Override
        public VariableSection build() {
            return new VariableSection(this);
        }
    }
    
    protected VariableSection(VariableSectionBuilder builder) {
        super(builder);
        end = null;
        finishedTracks = new ArrayList<>();
        builder.getTracks().forEach((_item) -> {
            this.finishedTracks.add(false);
        });
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
    protected void processTrackMessage(Track track) {
        Integer newFinalPosition = null;
        GroupMessage message = track.getMessage();
        
        if(message.finish) {
            newFinalPosition = track.getEnd();
        } else if(message.signal) {
            int largestEnd = 0;
            boolean allTracksFinished = true;
            int trackIndex = this.tracks.indexOf(track);
            
            this.finishedTracks.set(trackIndex, true);
            for(int i = 0; i < this.tracks.size(); i++) {
                if(this.finishedTracks.get(i)) {
                    if(this.tracks.get(i).getEnd() > largestEnd) {
                        largestEnd = this.tracks.get(i).getEnd();
                    }
                } else {
                    allTracksFinished = false;
                }
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
