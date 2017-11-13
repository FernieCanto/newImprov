package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class VariableSection extends Section {
    private ArrayList<Boolean> finishedTracks;
    private Integer end = null;
    
    public static class VariableSectionBuilder extends Section.SectionBuilder {
        @Override
        public VariableSection build() {
            return new VariableSection(this);
        }
    }
    
    protected VariableSection(VariableSectionBuilder builder) {
        super(builder);
        this.end = null;
        this.finishedTracks = new ArrayList<>();
        builder.getTracks().forEach((_item) -> {
            this.finishedTracks.add(false);
        });
    }
    
    @Override
    public void initialize(Random random, int position) {
        this.end = null;
        
        for(int index = 0; index < this.finishedTracks.size(); index++) {
            this.finishedTracks.set(index, false);
        }
        super.initialize(random, position);
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
            int trackIndex = this.getTracks().indexOf(track);
            
            this.finishedTracks.set(trackIndex, true);
            for(int i = 0; i < this.getTracks().size(); i++) {
                if(this.finishedTracks.get(i)) {
                    if(this.getTracks().get(i).getEnd() > largestEnd) {
                        largestEnd = this.getTracks().get(i).getEnd();
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
