package improviso;

import java.util.*;
import org.w3c.dom.*;

/**
 *
 * @author fernando
 */
public class Padrao {
    public static final int INTEIRO = -1;
  
    ArrayList<DefinicaoNota> notas;
    int duracao;
    
    Padrao(int duracao) {
        this.duracao = duracao;
        this.notas = new ArrayList<DefinicaoNota>();
    }
    
    public static Padrao produzPadraoXML(Element elemento) {
        int duracaoPadrao = Composicao.interpretaDuracao(elemento.getAttribute("length"));
        Padrao padrao = new Padrao(duracaoPadrao);
        org.w3c.dom.NodeList notas;
        
        DefinicaoNota.inicioDef = DefinicaoNota.maxInicioDef = DefinicaoNota.inicioFimDef = DefinicaoNota.maxInicioFimDef = 0;
        DefinicaoNota.duracaoDef = DefinicaoNota.maxDuracaoDef = DefinicaoNota.duracaoFimDef = DefinicaoNota.maxDuracaoFimDef = 60;
        DefinicaoNota.velocidadeDef = DefinicaoNota.maxVelocidadeDef = DefinicaoNota.velocidadeFimDef = DefinicaoNota.maxVelocidadeFimDef = 100;
                
        notas = elemento.getChildNodes();
        for(int indice = 0; indice < notas.getLength(); indice++) {
            if(notas.item(indice).getNodeType() == Node.ELEMENT_NODE)
                padrao.adicionaNota(DefinicaoNota.produzNotaXML((Element)notas.item(indice)));
        }
        
        return padrao;
    }
    
    public void adicionaNota(DefinicaoNota n) {
        this.notas.add(n);
    }
    
    public ArrayList<Nota> geraNotas(int inicio, double posicao, int duracao) {
        ArrayList<Nota> listaNotas = new ArrayList<Nota>();
        for(DefinicaoNota def : this.notas) {
          if(duracao == Padrao.INTEIRO)
            listaNotas.add(def.geraNota(inicio, posicao));
          else {
            listaNotas.add(def.geraNota(inicio, posicao, duracao));
          }
        }
        return listaNotas;
    }
    
    public int recuperaDuracao() {
        return this.duracao;
    }
}