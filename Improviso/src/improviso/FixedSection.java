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
    public void initialize(Random random) {
        this.currentLength = this.length.getValue(random);
        super.initialize(random);
    }
    
    @Override
    protected Section.SectionEnd getEnd() {
        return new Section.SectionEnd(this.currentLength);
    }
    
    public IntegerRange getLength() {
        return this.length;
    }
    
    @Override
    public void accept(SectionVisitor visitor) {
        visitor.visit(this);
    }
}