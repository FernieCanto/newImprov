package improviso;

import java.util.*;
/**
 * Implementa um grupo cujos grupos filhos podem repetir
 * de acordo com certos parâmetros:
 * @author fernando
 */
public abstract class GrupoRepeticao extends Grupo {
    int repeticoesFilho = 0; float inerciaFilho = 0;
    ArrayList<Integer> repeticoes = new ArrayList<Integer>();
    ArrayList<Float> inercias = new ArrayList<Float>();
    int repeticoesAtuais = 0;
    Grupo grupoSelecionado = null;
    
    GrupoRepeticao() {
        super();
    }
    
    @Override
    public void configuraGrupoXML(org.w3c.dom.Element elemento) {
        if(elemento.hasAttribute("repetitions"))
            repeticoesFilho = Integer.parseInt(elemento.getAttribute("repetitions"));
        if(elemento.hasAttribute("inertia"))
            inerciaFilho = Float.parseFloat(elemento.getAttribute("inertia"));
        super.configuraGrupoXML(elemento);
    }
    
    @Override
    public boolean adicionaFilho(Grupo G) {
        super.adicionaFilho(G);
        this.repeticoes.add(repeticoesFilho);
        this.inercias.add(inerciaFilho);
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
