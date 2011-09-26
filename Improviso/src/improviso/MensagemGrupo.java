package improviso;

/**
 *
 * @author fernando
 */
public class MensagemGrupo {
    public boolean sinalizaFim = false;
    public boolean desabilitaTrilha = false;
    public boolean obrigaFim = false;
    public boolean interrompeSecao = false;
    
    public MensagemGrupo() {
    }
    
    public void sinaliza() {
        this.sinalizaFim = true;
    }
    
    public void desabilita() {
        this.desabilitaTrilha = true;
    }
    
    public void obriga() {
        this.obrigaFim = true;
    }
    
    public void interrompe() {
        this.interrompeSecao = true;
    }
}
