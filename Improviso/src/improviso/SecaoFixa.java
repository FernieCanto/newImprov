package improviso;

/**
 *
 * @author fernando
 */
public class SecaoFixa extends Secao {
    int finalSecao;
    
    SecaoFixa(String id, int posicao) {
        super(id, posicao);
    }
    
    public void defineDuracao(int duracao) {
        finalSecao = inicio + duracao;
    }
    
    public void defineDuracao(Compasso compasso, int numCompassos) {
        finalSecao = inicio + (compasso.calculaDuracao() * numCompassos);
    }
    
    public void defineInterrompe(boolean interrompe) {
        interrompeTrilhas = interrompe;
    }
    
    @Override
    protected void processaMensagem(MensagemGrupo mensagem) {
        return;
    }
    
    @Override
    protected Integer obtemFinal() {
        return finalSecao;
    }
}