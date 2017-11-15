package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class VariableSection extends Section {
    final private HashMap<Track, Boolean> finishedTracks;
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
        this.finishedTracks = new HashMap<>();
    }
    
    @Override
    public void initialize(Random random, int position) {
        this.end = null;
        
        this.getTracks().forEach((_item) -> {
            this.finishedTracks.put(_item, false);
        });
        super.initialize(random, position);
    }

    @Override
    protected void processTrackMessage(Track track) {
        if(track.getMessage().finish) {
            this.setEnd(track.getEnd());
        } else if(track.getMessage().signal) {
            int largestEnd = 0;
            boolean allTracksFinished = true;
            
            this.finishedTracks.put(track, true);
            for(Track currentTrack : this.getTracks()) {
                if(this.finishedTracks.get(currentTrack)) {
                    if(currentTrack.getCurrentPosition() > largestEnd) {
                        largestEnd = currentTrack.getCurrentPosition();
                    }
                } else {
                    allTracksFinished = false;
                }
            }
            if(allTracksFinished) {
                this.setEnd(largestEnd);
            }
        }
    }
    private void setEnd(Integer end) {
        if (this.end == null) {
            this.end = end;
        } else if(end < this.end) {
            this.end = end;
        }
    }

    @Override
    protected Integer getEnd() {
        return this.end;
    }
}
