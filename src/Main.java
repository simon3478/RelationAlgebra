public class Main {
    public static void main(String[] args) {
        // Initialize controllers and other classes
        RelationController relationController = new RelationController();
        QueryController queryController = new QueryController(relationController);

        // Add sample relations
        relationController.addRelation("Employees (EID, Name, Age) = {E1, John, 32; E2, Alice, 28; E3, Bob, 29;}");
        relationController.addRelation("MoreEmployees (EID, Name, Age) = {E4, Dave, 35; E5, Emma, 30;}");
        relationController.addRelation("Departments (DID, DName) = {D1, HR; D2, IT;}");
        relationController.addRelation("Projects (PID, PName) = {P1, ProjectX; P2, ProjectY;}");

        //various types of queries
        String selectQuery = "select Age > 30 (Employees)";
        String projectQuery = "project (EID, Name)(Employees)";
        String innerJoinQuery = "Employees inner join Departments";
        String leftOuterJoinQuery = "Employees left outer join Projects";
        String unionQuery = "Employees union MoreEmployees";
        String intersectQuery = "Employees intersect MoreEmployees";
        String differenceQuery = "Employees difference MoreEmployees";


        System.out.println("Select Query Result:");
        queryController.executeQuery(selectQuery);

        System.out.println("\nProject Query Result:");
        queryController.executeQuery(projectQuery);
//
        System.out.println("\nInner Join Query Result:");
        queryController.executeQuery(innerJoinQuery);
//
        System.out.println("\nLeft Outer Join Query Result:");
        queryController.executeQuery(leftOuterJoinQuery);
//
        System.out.println("\nUnion Query Result:");
        queryController.executeQuery(unionQuery);
//
        System.out.println("\nIntersect Query Result:");
        queryController.executeQuery(intersectQuery);
//
        System.out.println("\nDifference Query Result:");
        queryController.executeQuery(differenceQuery);
    }
}
