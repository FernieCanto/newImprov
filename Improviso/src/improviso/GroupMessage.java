package improviso;

/**
 *
 * @author fernando
 */
public class GroupMessage {
    public boolean signal = false;
    public boolean disableTrack = false;
    public boolean finish = false;
    public boolean interrupt = false;
    
    public GroupMessage() {
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
}
