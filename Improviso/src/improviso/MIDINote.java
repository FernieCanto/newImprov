package improviso;

import java.io.Serializable;

/**
 *
 * @author Fernie Canto
 */
public class MIDINote implements Serializable {
    final private int pitch;
    final private int start;
    final private int length;
    final private int velocity;
    final private int MIDITrack;
    
    public MIDINote(int pitch, int start, int length, int velocity, int track) {
        this.pitch = pitch;
        this.start = start;
        this.length = length;
        this.velocity = velocity;
        this.MIDITrack = track;
    }
    
    @Override
    public String toString() {
        return this.getPitch()+" at "+this.getStart()+", length "+this.getLength();
    }

    public int getPitch() {
        return pitch;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getMIDITrack() {
        return MIDITrack;
    }
}
