package improviso;

import org.w3c.dom.Element;

/**
 *
 * @author fernando
 */
public class Arrow {
    public static final int defaultProbability = 1;
    public static final int defaultMaxExecutions = 100;
    public static final boolean defaultFinishAfterMax = false;
    
    protected String destinationSection;
    protected int probability = 1;
    protected int maxExecutions = 100;
    protected int executions = 0;
    protected boolean finishAfterMax = false;
    
    public Arrow(String destination) {
        destinationSection = destination;
    }
    
    public Arrow(String destination, int prob) {
        destinationSection = destination;
        probability = prob;
    }
    
    public Arrow(String destination, int prob, int maxExec) {
        destinationSection = destination;
        probability = prob;
        maxExecutions = maxExec;
    }
    
    public Arrow(String destination, int prob, int maxExec, boolean finish) {
        destinationSection = destination;
        probability = prob;
        maxExecutions = maxExec;
        finishAfterMax = finish;
    }

    public static Arrow generateArrowXML(Element arrowElement) {
        int probability = Arrow.defaultProbability;
        int maxExecutions = Arrow.defaultMaxExecutions;
        boolean finishAfterMax = Arrow.defaultFinishAfterMax;
        String destination = null;
        
        if(arrowElement.hasAttribute("to"))
            destination = arrowElement.getAttribute("to");
        if(arrowElement.hasAttribute("probability"))
            probability = Integer.parseInt(arrowElement.getAttribute("probability"));
        if(arrowElement.hasAttribute("maxExecutions"))
            maxExecutions = Integer.parseInt(arrowElement.getAttribute("maxExecutions"));
        if(arrowElement.hasAttribute("finishAfterMax"))
            finishAfterMax = true;
        return new Arrow(destination, probability, maxExecutions, finishAfterMax);
    }
    
    public String getDestination() {
        return destinationSection;
    }
    
    public int getProbability() {
        return probability;
    }
    
    public String execute() {
        if(executions < maxExecutions) {
            executions++;
            return destinationSection;
        } else {
            return null;
        }
    }
    
    public boolean getIsActive() {
        return finishAfterMax || (executions < maxExecutions);
    }
}
