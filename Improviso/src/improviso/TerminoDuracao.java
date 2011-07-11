package improviso;

/**
 *
 * @author fernando
 */
public class TerminoDuracao extends CondicaoTerminoSecao {
    private int finalSecao;
    
    TerminoDuracao(Secao s, int duracao) {
        super(s);
        this.finalSecao = s.retornaInicio() + duracao;
    }
    
    @Override
    public int obtemFinal() {
        return this.finalSecao;
    }
}
