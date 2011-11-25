/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 *
 * @author fernando
 */
public class Improviso {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Document documentoXML;
        String nomeArquivo = args[0];
        File arqXML = new File(nomeArquivo);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        documentoXML = dBuilder.parse(arqXML);
        documentoXML.normalizeDocument();
        Composicao c = Composicao.processaXML(documentoXML);
        
        c.executa(args[1]);
    }   
}