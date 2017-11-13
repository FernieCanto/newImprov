package improviso;
/**
 *
 * @author fernando
 */
public class FixedSection extends Section {
    int length;
    
    public static class FixedSectionBuilder extends Section.SectionBuilder {
        private Integer length;
        
        public Integer getLength() {
            return this.length;
        }
        
        public FixedSectionBuilder setLength(Integer length) {
            return this;
        }
        
        @Override
        public FixedSection build() {
            return new FixedSection(this);
        }
    }
    
    protected FixedSection(FixedSectionBuilder builder) {
        super(builder);
        this.length = builder.getLength();
    }
    
    /**
     * Defines whether the Patterns should be cut short at the ending position
     * of the Section, or allowed to be executed in their entirety.
     * @param interrupt 
     */
    public void setInterrupt(boolean interrupt) {
        interruptTracks = interrupt;
    }
    
    @Override
    protected void processTrackMessage(Track track) {
    }
    
    @Override
    protected Integer getEnd() {
        return this.length + this.start;
    }
}