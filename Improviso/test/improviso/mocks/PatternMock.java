package improviso.mocks;

import improviso.Pattern;

/**
 *
 * @author User
 */
public class PatternMock extends Pattern {
    public static class PatternMockBuilder extends PatternBuilder {
        @Override
        public PatternMock build() {
            return new PatternMock(this);
        }
    }
    
    private PatternMock(PatternMockBuilder builder) {
        super(builder);
    }
}
