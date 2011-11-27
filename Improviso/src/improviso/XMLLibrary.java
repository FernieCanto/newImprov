package improviso;

import java.util.*;
import org.w3c.dom.*;
/**
 *
 * @author fernando
 */
public class XMLLibrary {
    HashMap<String, Element> patterns       = new HashMap<String, Element>();
    HashMap<String, Element> groups         = new HashMap<String, Element>();
    HashMap<String, Element> tracks         = new HashMap<String, Element>();
    HashMap<String, Element> sections       = new HashMap<String, Element>();
    HashMap<String, Integer> noteAliases    = new HashMap<String, Integer>();
}
