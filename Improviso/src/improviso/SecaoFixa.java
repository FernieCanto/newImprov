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
        duracao = Composicao.interpretaDuracao(elemento.getAttribute("length"));
        
        if(elemento.hasAttribute("tempo"))
        tempo = Integer.parseInt(elemento.getAttribute("tempo"));
    }
    
    public void defineDuracao(int duracao) {
        finalSecao = inicio + duracao;
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
        return inicio + duracao;
    }
}