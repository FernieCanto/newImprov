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
public abstract class Section implements Cloneable {
    final private String id;
    final private int timeSignatureNumerator = 4;
    final private int timeSignatureDenominator = 4;
    final private int tempo;
    final private ArrayList<Track> tracks;
    final private boolean interruptTracks;
    
    protected int start;
    protected int currentPosition;
    
    public abstract static class SectionBuilder {
        private String id;
        private Integer tempo = 120;
        private final ArrayList<Track> tracks = new ArrayList<>();
        private boolean interruptTracks = false;
        
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
        
        public boolean getInterruptTracks() {
            return this.interruptTracks;
        }
        
        public SectionBuilder setInterruptTracks(boolean interrupt) {
            this.interruptTracks = interrupt;
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
        this.interruptTracks = builder.getInterruptTracks();
        this.tracks = builder.getTracks();
        this.start = 0;
        this.currentPosition = 0;
    }
    
    public String getId() {
        return this.id;
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
    
    public ArrayList<Track> getTracks() {
        return this.tracks;
    }
    
    /**
     * Set a new starting point for the next execution of the Section.
     * @param random
     * @param position Position in ticks
     */
    public void initialize(Random random, int position) {
        this.start = position;
        this.currentPosition = position;
        
        this.tracks.forEach((track) -> {
            track.initialize(start);
            track.selectNextPattern(random);
        });
    }
    
    /**
     * Executes the Section, returning the list of all notes produced by all
     * Tracks of the Section. After the execution, the current position of the
     * Section is updated.
     * @param rand
     * @return List of generated Notes
     */
    public MIDINoteList execute(Random rand) {
        MIDINoteList notes = new MIDINoteList();
        
        while(this.trackNotFinished()) {
            Track selectedTrack = this.selectNextTrack();
            
            notes.addAll(
                    selectedTrack.getCurrentPattern().execute(
                            rand,
                            this.getRelativePatternPosition(selectedTrack),
                            (this.getEnd() == null || !interruptTracks) ? null : this.getEnd() - selectedTrack.getCurrentPosition()
                    ).offsetNotes(selectedTrack.getCurrentPosition())
            );
            this.processTrackMessage(selectedTrack);
            selectedTrack.execute();

            this.currentPosition = this.getNewCurrentPosition(selectedTrack);
            selectedTrack.selectNextPattern(rand);
        }
        
        return notes;
    }
    
    private boolean trackNotFinished() {
        return this.getEnd() == null || this.getEnd() > this.currentPosition;
    }
    
    private double getRelativePatternPosition(Track selectedTrack) {
        if (this.getEnd() == null) {
            return 0.0;
        } else {
            return ((double)(selectedTrack.getEnd() - start) / (double)(this.getEnd() - start));
        }
    }

    private Track selectNextTrack() {
        Track selectedTrack = this.tracks.get(0);
            /* We seek the track that ends sooner */
            for(Track track : this.tracks) {
                if(track.getEnd() < selectedTrack.getEnd()) {
                    selectedTrack = track;
                }
            }
        return selectedTrack;
    }
    
    private int getNewCurrentPosition(Track selectedTrack) {
        int newCurrentPosition = selectedTrack.getCurrentPosition();
        for(Track t : this.tracks) {
            if(t.getCurrentPosition() < newCurrentPosition) {
                newCurrentPosition = t.getCurrentPosition();
            }
        }
        return newCurrentPosition;
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
