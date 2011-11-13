package improviso;
import java.util.*;

/**
 *
 * @author fernando
 */
public class ListaArestas {
    ArrayList<Aresta> arestas;
    ArrayList<Integer> probsAcumuladas;
    int probabilidadeMax;
    Random rand = null;
    
    public ListaArestas() {
        arestas = new ArrayList<Aresta>();
        probsAcumuladas = new ArrayList<Integer>();
        probabilidadeMax = 0;
    }
    
    public void adicionaAresta(Aresta a) {
        arestas.add(a);
        probabilidadeMax += a.recuperaProbabilidade();
        probsAcumuladas.add(probabilidadeMax);
    }
    
    public int numArestas() {
        return this.arestas.size();
    }
    
    public void defineSemente() {
        rand = new Random();
    }
    
    public void defineSemente(long semente) {
        if(rand == null)
            rand = new Random(semente);
        else
            rand.setSeed(semente);
    }
    
    protected void recalculaProbabilidades() {
        probabilidadeMax = 0;
        probsAcumuladas.clear();
        
        for(Aresta a : arestas) {
            probabilidadeMax += a.recuperaProbabilidade();
            probsAcumuladas.add(probabilidadeMax);
        }
    }
    
    public String buscaNovoDestino() {
        int selecao;
        int indice = 0;
        String destino = null;
        
        if(arestas.isEmpty())
            return null;
        
        if(rand == null)
            defineSemente();
        
        selecao = rand.nextInt(probabilidadeMax);
        for(Integer prob : probsAcumuladas) {
            if(selecao < prob)
                break;
            else
                indice++;
        }
        destino = arestas.get(indice).executa();
        if(!arestas.get(indice).ativa())
            arestas.remove(indice);
        return destino;
    }
}