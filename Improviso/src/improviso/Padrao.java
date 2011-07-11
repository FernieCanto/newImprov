package improviso;

import java.util.*;

/**
 *
 * @author fernando
 */
public class Padrao {
    String id;
    ArrayList<DefinicaoNota> notas;
    Compasso compasso;
    int numCompassos;
    
    Padrao(String id, Compasso compasso, int numCompassos) {
        this.id = id;
        this.compasso = compasso;
        this.numCompassos = numCompassos;
        this.notas = new ArrayList<DefinicaoNota>();
    }
    
    public void adicionaNota(DefinicaoNota n) {
        this.notas.add(n);
    }
    
    public ArrayList<Nota> geraNotas(int inicio, float posicao) {
        Iterator<DefinicaoNota> iterNota = this.notas.iterator();
        ArrayList<Nota> listaNotas = new ArrayList<Nota>(this.notas.size());
        while(iterNota.hasNext()) {
            listaNotas.add(iterNota.next().geraNota(inicio, posicao));
        }
        return listaNotas;
    }
    
    public int recuperaDuracao() {
        return this.compasso.calculaDuracao() * this.numCompassos;
    }
}