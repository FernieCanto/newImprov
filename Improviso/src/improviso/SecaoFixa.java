package improviso;
import org.w3c.dom.*;
/**
 *
 * @author fernando
 */
public class SecaoFixa extends Secao {
    int duracao;
    int finalSecao;
    
    SecaoFixa() {
        super();
    }
    
    @Override
    public void configuraSecaoXML(Element elemento) {
        if(elemento.hasAttribute("tempo"))
        tempo = Integer.parseInt(elemento.getAttribute("tempo"));
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