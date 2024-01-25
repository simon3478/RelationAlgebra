import java.util.List;

public class Relation {
    private String name;
    private List<String> attributes;
    private List<List<String>> tuples;
    public  Relation(String name, List<String> attributes, List<List<String>> tuples) {
        this.name=name;
        this.attributes=attributes;
        this.tuples=tuples;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<List<String>> getTuples() {
        return tuples;
    }

    public void setTuples(List<List<String>> tuples) {
        this.tuples = tuples;
    }

    public void addTuple(List<String> tuple) {
        tuples.add(tuple);
    }
    public void removeTuple(List<String> tuple) {
        tuples.remove(tuple);
    }
    public void displayRelation() {
        System.out.println("Relation: " + name);
        System.out.println("Attributes: " + attributes);
        for (List<String> tuple : tuples) {
            System.out.println(tuple);
        }
    }
    @Override
    public String toString() {
        return "Relation{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                ", tuples=" + tuples +
                '}';
    }

    public void displayTable() {
        StringBuilder table = new StringBuilder();

        // Add Relation Name at the top
        table.append("Relation: ").append(name).append("\n");

        // Add the attribute names
        table.append("|");
        for (String attribute : attributes) {
            table.append(attribute).append("|");
        }
        table.append("\n");

        // Add a separator line
        for (int i = 0; i < attributes.size(); i++) {
            table.append("----");  // Adjust the length of the separator based on your needs
        }
        table.append("\n");

        // Add the tuples
        for (List<String> tuple : tuples) {
            table.append("|");
            for (String value : tuple) {
                table.append(value).append("|");
            }
            table.append("\n");
        }

        // Print the constructed table
        System.out.println(table.toString());
    }



}
