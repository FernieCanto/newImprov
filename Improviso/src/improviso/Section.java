package improviso;

import java.util.*;

/**
 * This class implements a generic, abstract Section, which is a temporal
 * division of a Composition. A Section has a determined beginning and an ending,
 * though its ending may not be initially known, depending on the concrete
 * Section being used. Sections contain Tracks, which are executed simultaneously.
 * When the section is executed, it returns the sum of all notes produced by
 * its Tracks. Sections are directly subordinated to a Composition.
 * @author Fernie Canto
 */
public abstract class Section {
    protected String id;
    protected int tempo = 120;
    protected ArrayList<Track> tracks;
    
    protected Track selectedTrack;
    protected int selectedTrackIndex;
    protected int start, currentPosition;
    protected int timeSignatureNumerator = 4, timeSignatureDenominator = 4;
    protected boolean interruptTracks = false;
    
    public abstract static class SectionBuilder {
        private String id;
        private Integer tempo;
        private final ArrayList<Track> tracks = new ArrayList<>();
        
        public String getId() {
            return this.id;
        }
        
        public SectionBuilder setId(String id) {
            this.id = id;
            return this;
        }
        
        public Integer getTempo() {
            return this.tempo;
        }
        
        public SectionBuilder setTempo(Integer tempo) {
            this.tempo = tempo;
            return this;
        }
        
        public ArrayList<Track> getTracks() {
            return this.tracks;
        }
        
        public SectionBuilder addTrack(Track track) {
            this.tracks.add(track);
            return this;
        }
        
        abstract public Section build();
    }
    
    protected Section(SectionBuilder builder) {
        this.id = builder.getId();
        this.tempo = builder.getTempo();
        this.tracks = builder.getTracks();
        this.start = 0;
        this.currentPosition = 0;
    }
    
    /**
     * Return the starting point of the current execution of the Section.
     * @return Start in ticks
     */
    public int getStart() {
        return this.start;
    }
    
    /**
     * Return the tempo of the section.
     * @return Tempo in BPM
     */
    public int getTempo() {
        return this.tempo;
    }
    
    /**
     * Get the upper part (numerator) of the Section's time signature.
     * @return Numerator
     */
    public int getTimeSignatureNumerator() {
        return this.timeSignatureNumerator;
    }
    
    /**
     * Get the lower part (denominator) of the Section's time signature.
     * @return Denominator
     */
    public int getTimeSignatureDenominator() {
        return this.timeSignatureDenominator;
    }
    
    /**
     * Get the current position in ticks of the Section within the Composition.
     * @return Position in ticks
     */
    public int getCurrentPosition() {
        return this.currentPosition;
    }
    
    /**
     * Set a new starting point for the next execution of the Section.
     * @param position Position in ticks
     */
    public void initialize(int position) {
        this.start = position;
        this.currentPosition = position;
        
        for(Track t : this.tracks) {
            t.initialize(start);
        }
    }
    
    /**
     * Add a new Track to the section.
     * @param t Track to add
     * @return 
     */
    public boolean addTrack(Track t) {
        this.tracks.add(t);
        return true;
    }
    
    /**
     * Executes the Section, returning the list of all notes produced by all
     * Tracks of the Section. After the execution, the current position of the
     * Section is updated.
     * @return List of generated Notes
     */
    public ArrayList<MIDINote> execute() {
        ArrayList<MIDINote> notes = new ArrayList<>();
        Integer endPosition, newCurrentPosition;
    
        /* Initialize all tracks */
        for (Track t : this.tracks) {
            t.initialize(this.currentPosition);
            t.selectNextPattern();
        }
        endPosition = this.getEnd();
        
        /* While the section ending is unknown or ahead of the current position */
        while(endPosition == null || endPosition > this.currentPosition) {
            this.selectedTrack = null;
            /* We seek the track that ends sooner */
            for(int i = 0; i < this.tracks.size(); i++) {
                if(this.selectedTrack == null || this.tracks.get(i).getEnd() < this.selectedTrack.getEnd()) {
                    this.selectedTrack = this.tracks.get(i);
                    this.selectedTrackIndex = i;
                }
            }
            
            this.processTrackMessage(this.selectedTrack);
            if(endPosition == null) {
                notes.addAll(this.selectedTrack.execute(0.0));
            } else {
                double newRelativePosition = ((this.selectedTrack.getEnd() - start) / (endPosition - start));
                if(interruptTracks)
                    notes.addAll(this.selectedTrack.execute(newRelativePosition, endPosition - this.selectedTrack.getCurrentPosition()));
                else
                    notes.addAll(this.selectedTrack.execute(newRelativePosition));
            }

            newCurrentPosition = this.selectedTrack.getCurrentPosition();
            for(Track t : this.tracks) {
                if(t.getCurrentPosition() < newCurrentPosition)
                    newCurrentPosition = t.getCurrentPosition();
            }
            this.selectedTrack.selectNextPattern();
            this.currentPosition = newCurrentPosition;
            endPosition = this.getEnd();
        }
        
        return notes;
    }
    
    /**
     * Process and interpret the Message received by the executed Track, updating
     * its internal state.
     * @param track
     */
    protected abstract void processTrackMessage(Track track);
    
    /**
     * Returns the ending position of the Section. Return NULL if the ending is
     * not yet known.
     * @return 
     */
    protected abstract Integer getEnd();
}
