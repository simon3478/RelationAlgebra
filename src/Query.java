import java.util.List;

public class Query {
    private String operation; // e.g., "select", "project"
    private String targetRelation; // The main relation to operate on
    private String condition; // e.g., "Age > 30", applicable for selection
    private List<String> attributes; // Applicable for projection
    private String secondaryRelation; // Used in join and set operations

    public Query() {
        // Default constructor
    }

    // Getters and setters for operation
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    // Getters and setters for targetRelation
    public String getTargetRelation() {
        return targetRelation;
    }

    public void setTargetRelation(String targetRelation) {
        this.targetRelation = targetRelation;
    }

    // Getters and setters for condition
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Getters and setters for attributes
    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    // Getters and setters for secondaryRelation
    public String getSecondaryRelation() {
        return secondaryRelation;
    }

    public void setSecondaryRelation(String secondaryRelation) {
        this.secondaryRelation = secondaryRelation;
    }
}
