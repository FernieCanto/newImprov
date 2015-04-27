package improviso;

import org.w3c.dom.*;

public class MIDITrack {
    public final static int defaultChannel = 0;
    public final static int defaultInstrument = 0;
    public final static int defaultVolume = 100;
    public final static int defaultPan = 64;
    private int channel;
    private int instrument;
    private int volume;
    private int pan;
    
    public MIDITrack(int channel, int instrument, int volume, int pan) {
        this.channel = channel;
        this.instrument = instrument;
        this.volume = volume;
        this.pan = pan;
    }
    
    public static MIDITrack generateMIDITrackXML(Element element) {
        MIDITrack track = new MIDITrack(defaultChannel, defaultInstrument, defaultVolume, defaultPan);
        
        if(element.hasAttribute("channel")) {
            track.setChannel(Integer.parseInt(element.getAttribute("channel")) - 1);
        }
        if(element.hasAttribute("instrument")) {
            track.setInstrument(Integer.parseInt(element.getAttribute("instrument")));
        }
        if(element.hasAttribute("volume")) {
            track.setVolume(Integer.parseInt(element.getAttribute("volume")));
        }
        if(element.hasAttribute("pan")) {
            track.setPan(Integer.parseInt(element.getAttribute("pan")));
        }
        
        return track;
    }

    /**
     * @return the channel
     */
    public int getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    /**
     * @return the instrument
     */
    public int getInstrument() {
        return instrument;
    }

    /**
     * @param instrument the instrument to set
     */
    public void setInstrument(int instrument) {
        this.instrument = instrument;
    }

    /**
     * @return the volume
     */
    public int getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * @return the pan
     */
    public int getPan() {
        return pan;
    }

    /**
     * @param pan the pan to set
     */
    public void setPan(int pan) {
        this.pan = pan;
    }
}