/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package improviso;

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.util.*;
import javax.swing.JFileChooser;
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
        
        c.executa("D:\\musica.mid");
      
      /*
      f1 = new GrupoFolha("Folha.padrao1", geraPadrao1());
      f1.defineOpcoesSinaliza(100, 1.0, 100);
      f2 = new GrupoFolha("Folha.padrao2", geraPadrao2());
      f2.defineOpcoesSinaliza(100, 1.0, 100);
      f3 = new GrupoFolha("Folha.padrao3", geraPadrao3());
      f3.defineOpcoesSinaliza(100, 1.0, 100);
      f4 = new GrupoFolha("Folha.padrao4", geraPadrao4());
      f4.defineOpcoesSinaliza(100, 1.0, 100);
      f5 = new GrupoFolha("Folha.padrao5", geraPadrao5());
      f5.defineOpcoesSinaliza(100, 1.0, 100);

      g1 = new GrupoSorteio("Sorteio1");
      g1.adicionaFilho(f1, 3, 0, 0);
      g1.adicionaFilho(f2, 4, 0, 0);
      g1.defineOpcoesSinaliza(8, 0.4, 100);
      
      g2 = new GrupoSorteio("Sorteio2");
      g2.adicionaFilho(f3, 3, 0, 0);
      g2.adicionaFilho(f4, 2, 0, 0);
      g2.adicionaFilho(f5, 5, 0, 0);
      g2.defineOpcoesSinaliza(8, 0.4, 100);
      
      t1 = new Trilha(g1, 0);
      t2 = new Trilha(g2, 0);
      secao = new SecaoVariavel("secao1", 0);
      secao.adicionaTrilha(t1);
      secao.adicionaTrilha(t2);
      
      notas = secao.geraNotas();
      Collections.sort(notas, new OrdenadorNota());
      
      try {
        gerador = new GeradorMIDI(1);
        gerador.defineTempo(130);
        gerador.defineCompasso(4, 4);
        gerador.insereNotas(notas);
        gerador.geraArquivo("D:\\musica.mid");

      }
      catch(Exception e) {
        System.out.println("Erro! "+e.getMessage());
      } */
    }
    
    /*
    private static Padrao geraPadrao1() {
      Padrao p1 = new Padrao("teste", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(60);
      nota.defineInicio(0);
      nota.defineDuracao(120);
      nota.defineVelocidade(100);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(64);
      nota.defineInicio(120);
      nota.defineDuracao(120);
      nota.defineVelocidade(100, 10);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(67);
      nota.defineInicio(240);
      nota.defineDuracao(100, 20);
      nota.defineVelocidade(100, 15);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(72);
      nota.defineInicio(360);
      nota.defineDuracao(120);
      nota.defineVelocidade(90, 30);
      p1.adicionaNota(nota);

      return p1;
    }
    
    private static Padrao geraPadrao2() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(81);
      nota.defineInicio(0);
      nota.defineDuracao(240);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(77);
      nota.defineInicio(240);
      nota.defineDuracao(240);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
    
    private static Padrao geraPadrao3() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(48);
      nota.defineInicio(0);
      nota.defineDuracao(240);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(55);
      nota.defineInicio(240);
      nota.defineDuracao(240);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
    
    private static Padrao geraPadrao4() {
      Padrao p1 = new Padrao("teste", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(59);
      nota.defineInicio(0);
      nota.defineDuracao(120);
      nota.defineVelocidade(100);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(55);
      nota.defineInicio(120);
      nota.defineDuracao(120);
      nota.defineVelocidade(100, 10);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(50);
      nota.defineInicio(240);
      nota.defineDuracao(100, 20);
      nota.defineVelocidade(100, 15);
      p1.adicionaNota(nota);

      nota = new DefinicaoNota(47);
      nota.defineInicio(360);
      nota.defineDuracao(120);
      nota.defineVelocidade(90, 30);
      p1.adicionaNota(nota);

      return p1;
    }
    
    private static Padrao geraPadrao5() {
      Padrao p2 = new Padrao("teste2", new Compasso("4/4"), 1);
      DefinicaoNota nota;

      nota = new DefinicaoNota(36);
      nota.defineInicio(0);
      nota.defineDuracao(450);
      nota.defineVelocidade(100);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(40);
      nota.defineInicio(0);
      nota.defineDuracao(450);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);

      nota = new DefinicaoNota(43);
      nota.defineInicio(0);
      nota.defineDuracao(450);
      nota.defineVelocidade(80, 30);
      p2.adicionaNota(nota);
      
      return p2;
    }
    */
}
