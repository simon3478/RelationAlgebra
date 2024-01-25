import java.util.HashMap;
import java.util.Map;

public class RelationController {
    private Map<String, Relation> relations = new HashMap<>();
    private Parser parser = new Parser();

    public void addRelation(String relationText) {
        Relation relation = parser.parseRelation(relationText);
        relations.put(relation.getName(), relation);
    }

    public Relation getRelation(String name) {
        return relations.get(name);
    }


}
