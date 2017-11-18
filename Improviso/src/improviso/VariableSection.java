package improviso;

import java.util.*;
/**
 *
 * @author fernando
 */
public class VariableSection extends Section {
    final private HashMap<Track, Boolean> finishedTracks;
    private Section.SectionEnd end;
    
    public static class VariableSectionBuilder extends Section.SectionBuilder {
        @Override
        public VariableSection build() {
            return new VariableSection(this);
        }
    }
    
    protected VariableSection(VariableSectionBuilder builder) {
        super(builder);
        this.finishedTracks = new HashMap<>();
    }
    
    @Override
    public void initialize(Random random) {
        displayMessage("INITIALIZING");
        this.end = new Section.SectionEnd(null);
        
        this.getTracks().forEach((_item) -> {
            this.finishedTracks.put(_item, false);
        });
        super.initialize(random);
    }

    @Override
    protected void processTrackMessage(Track track) {
        if(track.getMessage().getInterrupt()) {
            this.setEnd(track.getCurrentPosition());
        } else if(track.getMessage().getFinished()) {
            int largestEnd = 0;
            this.finishedTracks.put(track, true);
            
            displayMessage(track.getId() + " FINISHED");
            
            if (!this.end.endIsKnown() && this.allTracksFinished()) {
                displayMessage("  - ALL TRACKS FINISHED! largestEnd @ 0");
                for(Track currentTrack : this.getTracks()) {
                    if(currentTrack.getCurrentPosition() > largestEnd) {
                        largestEnd = currentTrack.getCurrentPosition();
                        displayMessage("      largestEnd now @ " + largestEnd);
                    }
                }
                
                displayMessage("Setting end @ " + largestEnd);
                this.setEnd(largestEnd);
            }
        }
    }
    
    private boolean allTracksFinished() {
        return !this.finishedTracks.containsValue(false);
    }
    
    private void setEnd(Integer newEnd) {
        if (!this.end.endIsKnown() || this.end.compareTo(newEnd) == -1) {
            this.end = new Section.SectionEnd(newEnd);
        }
    }

    @Override
    protected Section.SectionEnd getEnd() {
        return this.end;
    }
}
