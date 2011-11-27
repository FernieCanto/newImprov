package improviso;
import java.util.*;
import org.w3c.dom.*;

/**
 * Implementa um grupo genérico, incluindo o algoritmo de adição de groups
 * children. Um grupo pode ser um Nodo, contendo diversos children (sub-itens) ou
 * uma Folha, contendo um Pattern. Quando um grupo é executado, um novo sub-item
 * é selecionado, e o Pattern correspondente pode ser obtido com o método
 * getSelectedPattern. Uma GroupMessage será retornado, contendo sinalizações de
 * fim de execução ou interrupções da seção.
 * RESPONSABILIDADES DO GRUPO:
 *  - Selecionar sub-itens seguindo um algoritmo próprio
 *  - Retornar mensagem com informações de fim
 * @author fernando
 */
public abstract class Group {
    protected ArrayList<Group> children;
    protected Pattern selectedPattern = null;
    protected Integer selectedGroupIndex = null;
    protected Random rand = null;
    protected int executions = 0;
        
    protected Integer   minExecutionsSignal = new Integer(0);
    protected Double    probabilitySignal   = new Double(1.0);
    protected Integer   maxExecutionsSignal = new Integer(100);
    protected boolean   disableTrack        = false;
    
    protected Integer   minExecutionsFinish = null;
    protected Double    probabilityFinish   = null;
    protected Integer   maxExecutionsFinish = null;
    protected boolean   interruptSection    = false;
    
    Group() {
        children = new ArrayList<Group>();
    }
    
    public static Group generateGroupXML(XMLLibrary XMLLibrary, Element element) {
        Group group;
        NodeList children;
        
        if(element.getNodeName().equals("sequenceGroup"))
            group = new SequenceGroup();
        else if(element.getNodeName().equals("randomGroup"))
            group = new RandomGroup();
        else {
            Pattern p;
            if(element.hasAttribute("pattern"))
                p = Pattern.generatePatternXML(XMLLibrary, XMLLibrary.patterns.get(element.getAttribute("pattern")));
            else
                p = Pattern.generatePatternXML(XMLLibrary, (Element)element.getElementsByTagName("pattern").item(0));
            
            group = new LeafGroup(p);
            group.configureGroupXML(element);
            return group;
        }
        
        children = element.getChildNodes();
        for(int indice = 0; indice < children.getLength(); indice++) {
            if(children.item(indice).getNodeType() == Node.ELEMENT_NODE) {
                Element filho = (Element)children.item(indice);
                group.configureGroupXML(filho);
                group.addChild(Group.generateGroupXML(XMLLibrary, filho));
            }
        }
        
        return group;
    }
    
    public void configureGroupXML(Element element) {
        if(element.hasAttribute("minExecutionsSignal"))
            this.minExecutionsSignal = Integer.parseInt(element.getAttribute("minExecutionsSignal"));
        if(element.hasAttribute("probabilitySignal"))
            this.probabilitySignal = Double.parseDouble(element.getAttribute("probabilitySignal"));
        if(element.hasAttribute("maxExecutionsSignal"))
            this.maxExecutionsSignal = Integer.parseInt(element.getAttribute("maxExecutionsSignal"));

        if(element.hasAttribute("minExecutionsFinish"))
            this.minExecutionsFinish = Integer.parseInt(element.getAttribute("minExecutionsFinish"));
        if(element.hasAttribute("probabilityFinish"))
            this.probabilityFinish = Double.parseDouble(element.getAttribute("probabilityFinish"));
        if(element.hasAttribute("maxExecutionsFinish"))
            this.maxExecutionsFinish = Integer.parseInt(element.getAttribute("maxExecutionsFinish"));
    }
    
    public void setSeed() {
        rand = new Random();
    }
    
    public void setSeed(long seed) {
        if(rand == null)
            rand = new Random(seed);
        else
            rand.setSeed(seed);
    }
    
    public void resetExecutionCounter() {
        executions = 0;
        for(Group g : children)
            g.resetExecutionCounter();
    }
    
    public boolean getIsLeaf() {
        return false;
    }
    
    public boolean addChild(Group g) {
        if(!this.getIsLeaf()) {
            this.children.add(g);
            return true;
        }
        else
            return false;
    }
    
    /**
     * Seleciona o próximo sub-item de acordo com o algoritmo e
     * configurações internas do grupo, retornando a mensagem
     * para a trilha.
     * @return Mensagem com opções de fim de execução
     */
    public GroupMessage execute() {
        GroupMessage message;
        
        if(rand == null)
            setSeed();
        
        if(!this.getIsLeaf()) {
            Group g = this.selectGroup();
            message = g.execute();
            this.selectedPattern = g.getSelectedPattern();
        }
        else {
            message = new GroupMessage();
        }
        
        executions++;
        if((this.maxExecutionsSignal != null) && (this.maxExecutionsSignal <= executions)) {
            message.signal();
            if(disableTrack)
                message.disable();
        }
        else if ((this.minExecutionsSignal != null) && (this.minExecutionsSignal <= executions)
              && (rand.nextDouble() <= this.probabilitySignal)) {
            message.signal();
            if(disableTrack)
                message.disable();
        }
        
        if((this.maxExecutionsFinish != null) && (this.maxExecutionsFinish <= executions)) {
            message.finish();
            if(interruptSection)
                message.interrupt();
        }
        else if ((this.minExecutionsFinish != null) && (this.minExecutionsFinish <= executions)
              && (rand.nextDouble() <= this.probabilityFinish)) {
            message.finish();
            if(interruptSection)
                message.interrupt();
        }
        
        return message;
    }
    
    public Pattern getSelectedPattern() {
        return this.selectedPattern;
    }
    
    public abstract Group selectGroup();
}