package improviso;

/**
 *
 * @author fernando
 */
public class Aresta {
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
