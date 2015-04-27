package improviso;

import java.util.HashMap;

/**
 *
 * @author User
 */
public class ElementLibrary {
    HashMap<String, Integer> noteAliases = new HashMap<String, Integer>();
    HashMap<String, Pattern> patterns = new HashMap<String, Pattern>();
    HashMap<String, Group> groups = new HashMap<String, Group>();
    HashMap<String, Track> tracks = new HashMap<String, Track>();
    HashMap<String, Section> sections = new HashMap<String, Section>();
    
    public void addNoteAlias(String id, Integer n) {
        this.noteAliases.put(id, n);
    }
    
    public boolean hasNoteAlias(String id) {
        return this.noteAliases.containsKey(id);
    }
    
    public Integer getNoteAlias(String id) {
        return this.noteAliases.get(id);
    }
    
    public void addPattern(String id, Pattern p) {
        this.patterns.put(id, p);
    }
    
    public Pattern getPattern(String id) {
        return this.patterns.get(id);
    }

    public void addGroup(String id, Group g) {
        this.groups.put(id, g);
    }

    public Group getGroup(String id) {
        return this.groups.get(id);
    }

    public void addTrack(String id, Track t) {
        this.tracks.put(id, t);
    }

    public Track getTrack(String id) {
        return this.tracks.get(id);
    }

    public void addSection(String id, Section s) {
        this.sections.put(id, s);
    }

    public Section getSection(String id) {
        return this.sections.get(id);
    }
}
