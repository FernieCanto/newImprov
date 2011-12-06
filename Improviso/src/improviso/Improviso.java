package improviso;

public class Improviso {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if(args.length < 2) {
            System.out.println(
                      "Improviso\r\n"
                    + "Usage: improviso [composition_file].xml [MIDI_file].mid");
        }
        else {
            try {
                Composition c = Composition.processXML(args[0]);
                c.execute(args[1]);
            }
            catch(ImprovisoException e) {
                System.out.println("Error in composition file:\r\n"+e.getMessage()+"\r\n"
                        +"Check your composition file and try again.");
            }
            catch(javax.xml.parsers.ParserConfigurationException e) {
                System.out.println("XML error:\r\n"+e.getMessage());
            }
            catch(java.io.IOException e) {
                System.out.println("I/O error:\r\n"+e.getMessage()+"\r\n"
                        +"Check the execution parameters and try again.");
            }
            catch(org.xml.sax.SAXException e) {
                System.out.println("XML parse error:\r\n"+e.getMessage()+"\r\n"
                        +"Check your composition file and try again.");
            }
            catch(javax.sound.midi.InvalidMidiDataException e) {
                System.out.println("Invalid MIDI data error:\r\n"+e.getMessage()+"\r\n"
                        +"Check your composition file and try again.");
            }
        }
    }   
}