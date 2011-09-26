package improviso;

import java.util.*;
/**
 * Implementa um grupo cujos grupos filhos podem repetir
 * de acordo com certos parâmetros:
 * @author fernando
 */
public abstract class GrupoRepeticao extends Grupo {
    ArrayList<Integer> repeticoes = new ArrayList<Integer>();
    ArrayList<Float> inercias = new ArrayList<Float>();
    int repeticoesAtuais = 0;
    Grupo grupoSelecionado = null;
    
    GrupoRepeticao(String id) {
        super(id);
    }
    
    @Override
    public boolean adicionaFilho(Grupo G) {
        return this.adicionaFilho(G, 0, 0);
    }
    
    public boolean adicionaFilho(Grupo G, int repeticao, float inercia) {
        super.adicionaFilho(G);
        this.repeticoes.add(repeticao);
        this.inercias.add(inercia);
        return true;
    }
    
    @Override
    public Grupo selecionaGrupo() {
        if(rand == null)
            this.defineSemente();
        
        if(grupoSelecionado != null) {
            if(repeticoes.get(this.indiceGrupoSelecionado) > repeticoesAtuais) {
                repeticoesAtuais++;
                return grupoSelecionado;
            }
            if(inercias.get(this.indiceGrupoSelecionado) > .0) {
                if(rand.nextFloat() < inercias.get(this.indiceGrupoSelecionado))
                    return grupoSelecionado;
            }
        }
        repeticoesAtuais = 0;
        this.selecionaProximoGrupo();
        return grupoSelecionado;
    }
    
    @Override
    public Padrao recuperaPadrao() {
        if(this.grupoSelecionado != null)
            return this.grupoSelecionado.recuperaPadrao();
        else
            return null;
    }
    
    protected abstract boolean selecionaProximoGrupo();
}
