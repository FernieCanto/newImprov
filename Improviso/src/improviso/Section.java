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
    final private boolean verbose;
    
    private int start;
    
    public abstract static class SectionBuilder {
        private String id;
        private Integer tempo = 120;
        private final ArrayList<Track> tracks = new ArrayList<>();
        private boolean interruptTracks = false;
        private boolean verbose = false;
        
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
        
        public boolean getVerbose() {
            return this.verbose;
        }
        
        public void verbose() {
            this.verbose = true;
        }
        
        abstract public Section build();
    }
    
    public static class SectionEnd implements Comparable<Integer> {
        private final Integer value;
        public SectionEnd(Integer value) {
            this.value = value;
        }

        @Override
        public int compareTo(Integer o) {
            if (!this.endIsKnown()) {
                return 1;
            } else {
                return this.value.compareTo(o);
            }
        }
        
        public boolean endIsKnown() {
            return this.value != null;
        }
        
        public int intValue() {
            if (this.endIsKnown()) {
                return this.value;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        
        public String toString() {
            if (this.endIsKnown()) {
                return this.value.toString();
            } else {
                return "unknown";
            }
        }
    }
    
    protected Section(SectionBuilder builder) {
        this.id = builder.getId();
        this.tempo = builder.getTempo();
        this.interruptTracks = builder.getInterruptTracks();
        this.tracks = builder.getTracks();
        this.verbose = builder.getVerbose();
        this.start = 0;
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
        
        while(this.sectionNotFinished()) {
            Track selectedTrack = this.selectNextTrack();
            
            displayMessage("Executing " + selectedTrack.getId() + " @ " + selectedTrack.getCurrentPosition());
            
            notes.addAll(
                    selectedTrack.getCurrentPattern().execute(
                            rand,
                            this.getRelativePatternPosition(selectedTrack),
                            (!this.getEnd().endIsKnown() || !this.interruptTracks) ? null : this.getEnd().intValue() - selectedTrack.getCurrentPosition()
                    ).offsetNotes(selectedTrack.getCurrentPosition())
            );
            selectedTrack.execute();
            this.processTrackMessage(selectedTrack);
            
            displayMessage("  Executed " + selectedTrack.getId() + " now @ " + selectedTrack.getCurrentPosition());
            displayMessage("  Section @ " + this.getCurrentPosition() + ", end @ " + this.getEnd().toString());

            selectedTrack.selectNextPattern(rand);
        }
        
        return notes;
    }
    
    private boolean sectionNotFinished() {
        return this.getEnd().compareTo(this.getCurrentPosition()) == 1;
    }
    
    private double getRelativePatternPosition(Track selectedTrack) {
        if (!this.getEnd().endIsKnown()) {
            return 0.0;
        } else {
            return ((double)(selectedTrack.getEnd() - this.start) / (double)(this.getEnd().intValue() - this.start));
        }
    }

    private Track selectNextTrack() {
        Track selectedTrack = this.tracks.get(0);
        for(Track track : this.tracks) {
            if(track.getEnd() < selectedTrack.getEnd()) {
                selectedTrack = track;
            }
        }
        return selectedTrack;
    }
    
    public int getCurrentPosition() {
        Track selectedTrack = this.tracks.get(0);
        for(Track t : this.tracks) {
            if(t.getCurrentPosition() < selectedTrack.getCurrentPosition()) {
                selectedTrack = t;
            }
        }
        return selectedTrack.getCurrentPosition();
    }
    
    public int getActualEnd() {
        Track selectedTrack = this.tracks.get(0);
        for(Track t : this.tracks) {
            if(t.getCurrentPosition() > selectedTrack.getCurrentPosition()) {
                selectedTrack = t;
            }
        }
        return selectedTrack.getCurrentPosition();
    }
    
    protected void displayMessage(String message) {
        if (this.verbose) {
            System.out.println(message);
        }
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
    protected abstract Section.SectionEnd getEnd();
}
