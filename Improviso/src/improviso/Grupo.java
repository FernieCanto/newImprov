package improviso;

import java.util.ArrayList;

/**
 *
 * @author fernando
 */
public abstract class Grupo {
    protected Padrao padraoFolha;
    protected ArrayList<Grupo> filhos;
    protected Grupo grupoSelecionado = null;
    
    Grupo() {
        this.padraoFolha = null;
        filhos = new ArrayList<Grupo>();
    }
    
    protected Grupo(Padrao p) {
        this.padraoFolha = p;
        filhos = null;
    }
    
    public boolean ehFolha() {
        return this.padraoFolha != null;
    }
    
    public Padrao recuperaPadraoFolha() {
        return this.padraoFolha;
    }
    
    public boolean adicionaFilho(Grupo g) {
        if(!this.ehFolha()) {
            this.filhos.add(g);
            return true;
        }
        else
            return false;
    }
    
    public boolean selecionaGrupo() {
      return false;
    }
    
    public Padrao recuperaPadraoSelecionado() {
        Padrao p;
        if(this.grupoSelecionado.ehFolha()) {
            p = this.grupoSelecionado.recuperaPadraoFolha();
        }
        else {
            p = this.grupoSelecionado.recuperaPadraoSelecionado();
        }
        this.grupoSelecionado = null;
        return p;
    }
}
