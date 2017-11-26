package improviso;

import java.util.Random;

/**
 *
 * @author FernieCanto
 */
public interface ExecutableSection {
    public String getId();
    public int getTempo();
    public int getTimeSignatureNumerator();
    public int getTimeSignatureDenominator();
    
    public void initialize(Random random);
    public MIDINoteList execute(Random random);
    public int getActualEnd();
    
    public void accept(SectionVisitor visitor);
}
