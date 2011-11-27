package improviso;

import org.w3c.dom.*;

public class MIDITrack {
    public static int defaultChannel = 0;
    public static int defaultInstrument = 0;
    public static int defaultVolume = 100;
    public static int defaultPan = 64;
    public int channel, instrument, volume, pan;
    
    public MIDITrack(int channel, int instrument, int volume, int pan) {
        this.channel = channel;
        this.instrument = instrument;
        this.volume = volume;
        this.pan = pan;
    }
    
    public static MIDITrack generateMIDITrackXML(Element element) {
        MIDITrack track = new MIDITrack(defaultChannel, defaultInstrument, defaultVolume, defaultPan);
        
        if(element.hasAttribute("channel"))
            track.channel = Integer.parseInt(element.getAttribute("channel")) - 1;
        if(element.hasAttribute("instrument"))
            track.instrument = Integer.parseInt(element.getAttribute("instrument"));
        if(element.hasAttribute("volume"))
            track.volume = Integer.parseInt(element.getAttribute("volume"));
        if(element.hasAttribute("pan"))
            track.pan = Integer.parseInt(element.getAttribute("pan"));
        
        return track;
    }
}