package improviso;
import java.util.*;
import org.w3c.dom.*;

/**
 * Defines a generic group, implementing the algorithm for adding children
 * groups. A group can be a Node, having multiple children (subgroups) or a
 * Leaf, containing a single Pattern. When a Group is executed, a new subitem
 * is selected
 * 
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
    protected String id;
    protected boolean debug = false;
    protected ArrayList<Group> children;
    protected Pattern selectedPattern = null;
    protected Integer selectedGroupIndex = null;
    protected Random rand = null;
    protected int executions = 0;
        
    protected Integer   minExecutionsSignal = 100;
    protected Double    probabilitySignal   = 1.0;
    protected Integer   maxExecutionsSignal = 100;
    protected boolean   disableTrack        = false;
    
    protected Integer   minExecutionsFinish = null;
    protected Double    probabilityFinish   = null;
    protected Integer   maxExecutionsFinish = null;
    protected boolean   interruptSection    = false;
    
    Group() {
        children = new ArrayList<Group>();
    }
    
    public static Group generateGroupXML(ElementLibrary library, Element element)
        throws ImprovisoException {
        Group group;
        NodeList children;
        String groupType = element.getAttribute("type");
        if(groupType.equals("sequence")) {
            group = new SequenceGroup();
        } else if(groupType.equals("random")) {
            group = new RandomGroup();
        } else {
            Pattern p;
            if(element.hasAttribute("pattern")) {
                p = library.getPattern(element.getAttribute("pattern"));
            } else {
                p = Pattern.generatePatternXML(library, (Element)element.getElementsByTagName("pattern").item(0));
            }
            
            group = new LeafGroup(p);
            group.configureGroupXML(element);
            return group;
        }
        
        children = element.getChildNodes();
        for(int indice = 0; indice < children.getLength(); indice++) {
            if(children.item(indice).getNodeType() == Node.ELEMENT_NODE) {
                Element filho = (Element)children.item(indice);
                group.addChild(Group.generateGroupXML(library, filho));
            }
        }
        
        group.configureGroupXML(element);
        return group;
    }
    
    public void configureGroupXML(Element element) {
        this.id = element.getTagName();
        this.debug = element.hasAttribute("debug");
        if(element.hasAttribute("minExecutionsSignal")) {
            this.minExecutionsSignal = Integer.parseInt(element.getAttribute("minExecutionsSignal"));
        }
        if(element.hasAttribute("probabilitySignal")) {
            this.probabilitySignal = Double.parseDouble(element.getAttribute("probabilitySignal"));
        }
        if(element.hasAttribute("maxExecutionsSignal")) {
            this.maxExecutionsSignal = Integer.parseInt(element.getAttribute("maxExecutionsSignal"));
        }

        if(element.hasAttribute("minExecutionsFinish")) {
            this.minExecutionsFinish = Integer.parseInt(element.getAttribute("minExecutionsFinish"));
        }
        if(element.hasAttribute("probabilityFinish")) {
            this.probabilityFinish = Double.parseDouble(element.getAttribute("probabilityFinish"));
        }
        if(element.hasAttribute("maxExecutionsFinish")) {
            this.maxExecutionsFinish = Integer.parseInt(element.getAttribute("maxExecutionsFinish"));
        }
    }
    
    public void setSeed() {
        this.rand = new Random();
    }
    
    public void setSeed(long seed) {
        if(this.rand == null) {
            this.rand = new Random(seed);
        } else {
            this.rand.setSeed(seed);
        }
    }
    
    public void resetGroup() {
        this.executions = 0;
        for(Group g : children) {
            g.resetGroup();
        }
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
        
        if(rand == null) {
            setSeed();
        }
        
        if(!this.getIsLeaf()) {
            Group g = this.selectGroup();
            message = g.execute();
            this.selectedPattern = g.getSelectedPattern();
        }
        else {
            message = new GroupMessage(this.id);
        }
        
        executions++;
        if((this.maxExecutionsSignal != null) && (this.maxExecutionsSignal <= executions)) {
            message.signal();
            if(disableTrack) {
                message.disable();
            }
        }
        else if ((this.minExecutionsSignal != null) && (this.minExecutionsSignal <= executions)
              && (rand.nextDouble() <= this.probabilitySignal)) {
            message.signal();
            if(disableTrack) {
                message.disable();
            }
        }
        
        if((this.maxExecutionsFinish != null) && (this.maxExecutionsFinish <= executions)) {
            message.finish();
            if(interruptSection) {
                message.interrupt();
            }
        }
        else if ((this.minExecutionsFinish != null) && (this.minExecutionsFinish <= executions)
              && (rand.nextDouble() <= this.probabilityFinish)) {
            message.finish();
            if(interruptSection) {
                message.interrupt();
            }
        }
        
        return message;
    }
    
    public Pattern getSelectedPattern() {
        return this.selectedPattern;
    }
    
    public abstract Group selectGroup();
}