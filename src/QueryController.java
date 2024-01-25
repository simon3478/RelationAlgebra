public class QueryController {

    private Parser parser = new Parser();
    private RelationController relationController;
    private Operations operations = new Operations();

    public QueryController(RelationController relationController) {
        this.relationController = relationController;
    }

    public void executeQuery(String queryText) {
        Query query = parser.parseQuery(queryText);
        switch (query.getOperation().toLowerCase()) {
            case "select":
            case "project":
                executeSingleRelationOperation(query);
                break;
            case "left outer join":
            case "inner join":
            case "right outer join":
            case "full outer join":

            case "union":
            case "intersect":
            case "difference":
                executeDualRelationOperation(query);
                break;
            default:
                //unknown or invalid operations
                System.out.println("Unknown or unsupported operation: " + query.getOperation());

        }
    }

        private void executeSingleRelationOperation(Query query) {
            Relation relation = relationController.getRelation(query.getTargetRelation());
            if (relation != null) {
                switch (query.getOperation().toLowerCase()) {
                    case "select":
                        Relation resultRelation = operations.select(query, relation);
                        // Display the result as a table
                        resultRelation.displayTable();
                        break;

                    case "project":
                        Relation resultRelationProj =operations.project(query, relation);
                        resultRelationProj.displayTable();
                        break;
                }
            } else {
                System.out.println("relation not found.");
            }
        }
        private void executeDualRelationOperation(Query query) {
            Relation relation1 = relationController.getRelation(query.getTargetRelation());
            Relation relation2 = relationController.getRelation(query.getSecondaryRelation());
            if (relation1 != null && relation2 != null) {
                switch (query.getOperation().toLowerCase()) {
                    case "inner join":
                        Relation resultRelation=operations.innerJoin(query, relation1, relation2);
                        resultRelation.displayTable();
                        break;
                    case "left outer join":
                        resultRelation=operations.leftOuterJoin(query, relation1, relation2);
                        resultRelation.displayTable();
                        break;
                    case "right outer join":
                        resultRelation=operations.rightOuterJoin(query, relation1, relation2);
                        resultRelation.displayTable();

                        break;
                    case "full outer join":
                        resultRelation=operations.fullOuterJoin(query, relation1, relation2);
                        resultRelation.displayTable();
                        break;

                    // Other dual relation operations
                    case "union":
                        resultRelation=operations.union(query, relation1, relation2);
                        resultRelation.displayTable();
                        break;
                    case "intersect":
                        resultRelation=operations.intersect(query, relation1, relation2);
                        resultRelation.displayTable();
                        break;
                    case "difference":
                        resultRelation=operations.difference(query, relation1, relation2);
                        resultRelation.displayTable();
                        break;
                    default:
                        // unknown or invalid operations
                        System.out.println("Unknown or unsupported operation: " + query.getOperation());
                        break;

                }
            } else {
                //null relations or not found
                System.out.println("One or both relations not found: " + query.getTargetRelation() + ", " + query.getSecondaryRelation());
            }
    }
}
