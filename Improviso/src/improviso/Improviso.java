package improviso;

import java.io.*;

public class Improviso {

    /**
     * @param args the command line arguments
		 * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String newline = System.getProperty("line.separator");
        
        if(args.length >= 1) {
            try {
                Composition c = Composition.processXML(args[0]);
                if(args.length == 1) {
                    c.execute();
                } else {
                    c.execute(args[1]);
                }
            }
            catch(ImprovisoException e) {
                System.out.println("Error in composition file:"+newline+e.getMessage()+newline
                        +"Check your composition file and try again.");
            }
            catch(javax.xml.parsers.ParserConfigurationException e) {
                System.out.println("XML error:"+newline+e.getMessage());
            }
            catch(java.io.IOException e) {
                System.out.println("I/O error:"+newline+e.getMessage()+newline
                        +"Check the execution parameters and try again.");
            }
            catch(org.xml.sax.SAXException e) {
                System.out.println("XML parse error:"+newline+e.getMessage()+newline
                        +"Check your composition file and try again.");
            }
            catch(javax.sound.midi.InvalidMidiDataException e) {
                System.out.println("Invalid MIDI data error:"+newline+e.getMessage()+newline
                        +"Check your composition file and try again.");
            }
        }
        else {
            System.out.println(
                  "Improviso" + newline
                + "Usage: improviso [composition_file].xml [MIDI_file].mid");
        }
        
    }   
}