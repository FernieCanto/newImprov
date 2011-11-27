package improviso;

/**
 *
 * @author fernando
 */
public class Note {
    public int pitch, start, length, velocity, MIDITrack;
    
    Note(int pitch, int start, int length, int velocity, int track) {
        this.pitch = pitch;
        this.start = start;
        this.length = length;
        this.velocity = velocity;
        this.MIDITrack = track;
    }
}
