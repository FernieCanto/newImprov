package improviso;

/**
 *
 * @author fernando
 */
public class GroupMessage {
    protected String origin = null;
    public boolean signal = false;
    public boolean disableTrack = false;
    public boolean finish = false;
    public boolean interrupt = false;
    
    public GroupMessage(String origin) {
        this.origin = origin;
    }
    
    public void signal() {
        this.signal = true;
    }
    
    public void disable() {
        this.disableTrack = true;
    }
    
    public void finish() {
        this.finish = true;
    }
    
    public void interrupt() {
        this.interrupt = true;
    }
    
    public String getOrigin() {
        return this.origin;
    }
    
    @Override
    public String toString() {
        return (signal ? "Signal" : "Not Signal") + ", " + (finish ? "Finish" : "Not finish");
    }
}
