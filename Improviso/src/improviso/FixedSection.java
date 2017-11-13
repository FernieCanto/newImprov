package improviso;

import java.util.Random;

/**
 *
 * @author fernando
 */
public class FixedSection extends Section {
    final private IntegerRange length;
    private Integer currentLength;
    
    public static class FixedSectionBuilder extends Section.SectionBuilder {
        private IntegerRange length;
        
        public IntegerRange getLength() {
            return this.length;
        }
        
        public FixedSectionBuilder setLength(IntegerRange length) {
            this.length = length;
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
    
    @Override
    protected void processTrackMessage(Track track) {
    }
    
    @Override
    public void initialize(Random random, int position) {
        this.currentLength = this.length.getValue(random);
        super.initialize(random, position);
    }
    
    @Override
    protected Integer getEnd() {
        return this.currentLength
                + this.start;
    }
}