package improviso;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.regex.*;

/**
 *
 * @author fernando
 */
public class ProcessadorXML {
    protected static int CANAL_PADRAO = 1;
    protected static int INSTRUMENTO_PADRAO = 0;
    protected static int VOLUME_PADRAO = 100;
    protected static int PAN_PADRAO = 63;
    
    protected static Pattern padraoIntervalo = Pattern.compile("^(\\d)(-(\\d))?(\\|(\\d)(-(\\d))?)?$");
    
    Composicao c;
    File arqXML;
    Document documentoXML;
    String erro = null;
    public ProcessadorXML(String nomeArquivo) throws Exception {
        arqXML = new File(nomeArquivo);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		documentoXML = dBuilder.parse(arqXML);
		documentoXML.getDocumentElement().normalize();
    }
    
    public Composicao geraComposicao() {
        c = new Composicao();
        
        NodeList listasFaixasMIDI = documentoXML.getElementsByTagName("MIDITrackList");
        NodeList listasPadroes = documentoXML.getElementsByTagName("patternList");
        NodeList listasGrupos = documentoXML.getElementsByTagName("groupList");
        NodeList listasTrilhas = documentoXML.getElementsByTagName("trackList");
        NodeList listasSecoes = documentoXML.getElementsByTagName("sectionList");
        
        for(int indice = 0; indice < listasFaixasMIDI.getLength(); indice++) {
            this.leFaixasMIDI(listasFaixasMIDI.item(indice));
        }
        for(int indice = 0; indice < listasPadroes.getLength(); indice++) {
            this.lePadroes(listasPadroes.item(indice));
        }
        
        return c;
    }
    
    protected boolean leFaixasMIDI(Node listaFaixasMIDI) {
        NodeList faixas = listaFaixasMIDI.getChildNodes();
        for(int indice = 0; indice < faixas.getLength(); indice++) {
            Element elementoFaixa = (Element)faixas.item(indice);
            int canal, instrumento, volume, pan;
            
            try {
                canal = this.leAtributo(elementoFaixa, "canal", 1, 16, CANAL_PADRAO);
                instrumento = this.leAtributo(elementoFaixa, "instrumento", 0, 127, INSTRUMENTO_PADRAO);
                volume = this.leAtributo(elementoFaixa, "volume", 0, 127, VOLUME_PADRAO);
                pan = this.leAtributo(elementoFaixa, "pan", 0, 127, PAN_PADRAO);
                
                c.adicionaFaixa(new FaixaMIDI(canal, instrumento, volume, pan));
            }
            catch(Exception e) {
                this.erro = e.getMessage();
                return false;
            }
        }
        return true;
    }
    
    protected boolean lePadroes(Node listaPadroes) {
        NodeList padroes = listaPadroes.getChildNodes();
        
        for(int indice = 0; indice < padroes.getLength(); indice++) {
            Element elementoPadrao = (Element)padroes.item(indice);
            String idPadrao = elementoPadrao.getAttribute("id");
            Padrao padrao = geraPadrao(elementoPadrao);

            try {
                c.adicionaPadrao(idPadrao, padrao);
            }
            catch(Exception e) {
                this.erro = "Erro ao adicionar padrao: "+e.getMessage();
                return false;
            }
        }
        return true;
    }
    
    protected boolean leGrupos(Node listaGrupos) {
        NodeList grupos = listaGrupos.getChildNodes();
        
        for(int indice = 0; indice < grupos.getLength(); indice++) {
            Element elementoGrupo = (Element)grupos.item(indice);
            String idGrupo = elementoGrupo.getAttribute("id");
            Grupo grupo = geraGrupo(elementoGrupo);
        }
        
        return true;
    }
    
    protected Padrao geraPadrao(Element elementoPadrao) {
        Integer vel = 100, velMax = null, velFim = null, velMaxFim = null;
        Integer ini = 0, iniMax = null, iniFim = null, iniMaxFim = null;
        Integer dur = 60, durMax = null, durFim = null, durMaxFim = null;
        int faixa = 1;
        
        int duracaoPadrao = Composicao.interpretaDuracao(elementoPadrao.getAttribute("length"));
        Padrao padrao = new Padrao(duracaoPadrao);

        NodeList notas = elementoPadrao.getElementsByTagName("note");
        for(int indice = 0; indice < notas.getLength(); indice++) {
            Element elementoNota = (Element)notas.item(indice);
            DefinicaoNota defNota = new DefinicaoNota(Integer.parseInt(elementoNota.getNodeValue()));

            if(elementoNota.hasAttribute("start")) {
                Matcher m = ProcessadorXML.padraoIntervalo.matcher(elementoNota.getAttribute("start"));
                ini = new Integer(m.group(1));

                if(m.group(3) != null)
                    iniMax = new Integer(m.group(3));
                else
                    iniMax = null;

                if(m.group(4) != null) {
                    iniFim = new Integer(m.group(5));
                    if(m.group(7) != null)
                        iniMaxFim = new Integer(m.group(7));
                    else
                        iniMaxFim = null;
                }
                else
                    iniFim = iniMaxFim = null;

                defNota.defineInicio(ini, iniMax, iniFim, iniMaxFim);
            }

            if(elementoNota.hasAttribute("length")) {
                Matcher m = ProcessadorXML.padraoIntervalo.matcher(elementoNota.getAttribute("length"));
                dur = new Integer(m.group(1));

                if(m.group(3) != null)
                    durMax = new Integer(m.group(3));
                else
                    durMax = null;

                if(m.group(4) != null) {
                    durFim = new Integer(m.group(5));
                    if(m.group(7) != null)
                        durMaxFim = new Integer(m.group(7));
                    else
                        durMaxFim = null;
                }
                else
                    durFim = durMaxFim = null;

                defNota.defineDuracao(dur, durMax, durFim, durMaxFim);
            }

            if(elementoNota.hasAttribute("velocity")) {
                Matcher m = ProcessadorXML.padraoIntervalo.matcher(elementoNota.getAttribute("velocity"));
                vel = new Integer(m.group(1));

                if(m.group(3) != null)
                    velMax = new Integer(m.group(3));
                else
                    velMax = null;

                if(m.group(4) != null) {
                    velFim = new Integer(m.group(5));
                    if(m.group(7) != null)
                        velMaxFim = new Integer(m.group(7));
                    else
                        velMaxFim = null;
                }
                else
                    velFim = velMaxFim = null;

                defNota.defineVelocidade(vel, velMax, velFim, velMaxFim);
            }

            if(elementoNota.hasAttribute("track"))
                faixa = Integer.parseInt(elementoNota.getAttribute("track"));

            defNota.defineFaixa(faixa);
            padrao.adicionaNota(defNota);
        }
        return padrao;
    }
    
    protected Grupo geraGrupo(Element elemento) {
        Grupo grupo;
        
        if(elemento.getNodeName().equals("sequenceGrupo")) {
            return geraGrupoSequencia(elemento);
        }
                
        return null;
    }
    
    protected GrupoSequencia geraGrupoSequencia(Element elemento) {
        GrupoSequencia grupo = new GrupoSequencia("");
        NodeList gruposFilhos = elemento.getChildNodes();
        
        for(int indice = 0; indice < gruposFilhos.getLength(); indice++) {
            int repeticoes = 1; float inercia = 0;
            Element elementoFilho = (Element)gruposFilhos.item(indice);
            Grupo grupoFilho = geraGrupo(elementoFilho);
            
            if(elementoFilho.hasAttribute("repetitions"))
                repeticoes = Integer.parseInt(elementoFilho.getAttribute("repetitions"));
            if(elementoFilho.hasAttribute("inertia"))
                inercia = Float.parseFloat(elementoFilho.getAttribute("inertia"));
            
            grupo.adicionaFilho(grupoFilho, repeticoes, inercia);
        }
        
        return grupo;
    }
    
    protected int leAtributo(Element elemento, String atributo, int min, int max, int valDefault)
              throws Exception {
        int retorno = valDefault;
        
        if(elemento.hasAttribute(atributo)) {
            int val = Integer.parseInt(elemento.getAttribute(atributo));
            if(val >= min && val <= max)
                retorno = val;
            else
                throw new Exception("Valor excede a faixa");
        }
        
        return retorno;
    }
}
