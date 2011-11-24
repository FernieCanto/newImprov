package improviso;

import org.w3c.dom.*;

public class FaixaMIDI {
    public static int canalPadrao = 0;
    public static int instrumentoPadrao = 0;
    public static int volumePadrao = 100;
    public static int panPadrao = 64;
    public int canal, instrumento, volume, pan;
    
    public FaixaMIDI(int canal, int instrumento, int volume, int pan) {
        this.canal = canal;
        this.instrumento = instrumento;
        this.volume = volume;
        this.pan = pan;
    }
    
    public static FaixaMIDI produzFaixaMIDIXML(Element elemento) {
        FaixaMIDI faixa = new FaixaMIDI(FaixaMIDI.canalPadrao, FaixaMIDI.instrumentoPadrao, FaixaMIDI.volumePadrao, FaixaMIDI.panPadrao);
        
        if(elemento.hasAttribute("channel"))
            faixa.canal = Integer.parseInt(elemento.getAttribute("channel")) - 1;
        if(elemento.hasAttribute("instrument"))
            faixa.instrumento = Integer.parseInt(elemento.getAttribute("instrument"));
        if(elemento.hasAttribute("volume"))
            faixa.volume = Integer.parseInt(elemento.getAttribute("volume"));
        if(elemento.hasAttribute("pan"))
            faixa.pan = Integer.parseInt(elemento.getAttribute("pan"));
        
        return faixa;
    }
}
