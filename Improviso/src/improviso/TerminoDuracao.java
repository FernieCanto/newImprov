package improviso;

/**
 *
 * @author fernando
 */
public class TerminoDuracao extends CondicaoTerminoSecao {
    private int duracao;
    private boolean interrompeTrilhas;
    private int finalSecao;
    
    TerminoDuracao(int duracao, boolean interrompe) {
        this.duracao = duracao;
        this.interrompeTrilhas = interrompe;
    }
    
    @Override
    public void defineSecao(Secao s) {
      this.secao = s;
      this.finalSecao = s.retornaInicio() + this.duracao;
    }
    
    @Override
    public int obtemFinal() {
        return this.finalSecao;
    }
    
    public boolean interrompeTrilhas() {
      return interrompeTrilhas;
    }
}
