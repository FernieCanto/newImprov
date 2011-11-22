package improviso;

import org.w3c.dom.Element;

/**
 *
 * @author fernando
 */
public class Aresta {
    public static int probabilidadePadrao = 1;
    public static int maxExecucoesPadrao = 100;
    public static boolean encerraAposMaxPadrao = false;
    
    protected String secaoDestino;
    protected int probabilidade = 1;
    protected int maxExecucoes = 100;
    protected int execucoes = 0;
    protected boolean encerraAposMaxExecucoes = false;
    
    public Aresta(String destino) {
        secaoDestino = destino;
    }
    
    public Aresta(String destino, int prob) {
        secaoDestino = destino;
        probabilidade = prob;
    }
    
    public Aresta(String destino, int prob, int maxExec) {
        secaoDestino = destino;
        probabilidade = prob;
        maxExecucoes = maxExec;
    }
    
    public Aresta(String destino, int prob, int maxExec, boolean encerra) {
        secaoDestino = destino;
        probabilidade = prob;
        maxExecucoes = maxExec;
        encerraAposMaxExecucoes = encerra;
    }

    public static Aresta produzArestaXML(Element elementoAresta) {
        int probabilidade = Aresta.probabilidadePadrao;
        int maxExecucoes = Aresta.maxExecucoesPadrao;
        boolean encerraAposMax = Aresta.encerraAposMaxPadrao;
        String destino = null;
        
        if(elementoAresta.hasAttribute("to"))
            destino = elementoAresta.getAttribute("to");
        if(elementoAresta.hasAttribute("probability"))
            probabilidade = Integer.parseInt(elementoAresta.getAttribute("probability"));
        if(elementoAresta.hasAttribute("maxExecutions"))
            maxExecucoes = Integer.parseInt(elementoAresta.getAttribute("maxExecutions"));
        if(elementoAresta.hasAttribute("finishAfterMax"))
            encerraAposMax = true;
        return new Aresta(destino, probabilidade, maxExecucoes, encerraAposMax);
    }
    
    public String recuperaDestino() {
        return secaoDestino;
    }
    
    public int recuperaProbabilidade() {
        return probabilidade;
    }
    
    public String executa() {
        if(execucoes < maxExecucoes) {
            execucoes++;
            return secaoDestino;
        }
        else
            return null;
    }
    
    public boolean ativa() {
        return encerraAposMaxExecucoes || (execucoes < maxExecucoes);
    }
}
