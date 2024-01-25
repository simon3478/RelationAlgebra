import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Operations {

    public Relation select(Query query, Relation targetRelation) {
        String[] conditionParts = query.getCondition().split(" ");
        if (conditionParts.length != 3) {
            // Handle invalid condition format
            return null;
        }

        String columnName = conditionParts[0];
        String operator = conditionParts[1];
        String value = conditionParts[2];

        int columnIndex = targetRelation.getAttributes().indexOf(columnName);
        if (columnIndex == -1) {
            // Handle case where column doesn't exist
            return null;
        }

        List<List<String>> filteredTuples = new ArrayList<>();
        for (List<String> tuple : targetRelation.getTuples()) {
            if (evaluateCondition(tuple.get(columnIndex), operator, value)) {
                filteredTuples.add(tuple);
            }
        }

        // Create and return a new Relation object with the filtered tuples
        return new Relation(targetRelation.getName() + "_filtered", targetRelation.getAttributes(), filteredTuples);
    }

    private boolean evaluateCondition(String tupleValue, String operator, String value) {
        switch (operator) {
            case ">":
                return tupleValue.compareTo(value) > 0;
            case "<":
                return tupleValue.compareTo(value) < 0;
            // Add cases for other operators
            default:
                return false;
        }
    }



    public Relation project(Query query, Relation relation) {
        List<String> selectedAttributes = query.getAttributes();

        // Find indices of the selected attributes in the relation
        List<Integer> attributeIndices = new ArrayList<>();
        for (String attribute : selectedAttributes) {
            int index = relation.getAttributes().indexOf(attribute);
            if (index != -1) {
                attributeIndices.add(index);
            } else {
                System.out.println("attribute not found.");
            }
        }

        // Create a new set of tuples with only the selected attributes
        List<List<String>> projectedTuples = new ArrayList<>();
        for (List<String> tuple : relation.getTuples()) {
            List<String> projectedTuple = new ArrayList<>();
            for (Integer index : attributeIndices) {
                projectedTuple.add(tuple.get(index));
            }
            projectedTuples.add(projectedTuple);
        }
        // Create a new relation with the projected tuples
        Relation projectedRelation = new Relation(relation.getName() + "_projected", selectedAttributes, projectedTuples);

        return projectedRelation;
    }


    public Relation innerJoin(Query query, Relation relation1, Relation relation2) {
        List<String> commonAttributes = findCommonAttributes(relation1, relation2);
        List<String> joinedAttributes = combineAttributes(relation1, relation2, commonAttributes);
        List<List<String>> joinedTuples = new ArrayList<>();

        for (List<String> tuple1 : relation1.getTuples()) {
            for (List<String> tuple2 : relation2.getTuples()) {
                if (matchesOnCommonAttributes(tuple1, tuple2, relation1, relation2, commonAttributes)) {
                    joinedTuples.add(combineTuples(tuple1, tuple2, relation1, relation2, commonAttributes));
                }
            }
        }
        System.out.println("inner test");
        // Create and handle new joined relation
        Relation joinedRelation = new Relation("InnerJoined", joinedAttributes, joinedTuples);
        return joinedRelation;
    }

    public Relation leftOuterJoin(Query query, Relation relation1, Relation relation2) {
        List<String> commonAttributes = findCommonAttributes(relation1, relation2);
        List<String> joinedAttributes = combineAttributes(relation1, relation2, commonAttributes);
        List<List<String>> joinedTuples = new ArrayList<>();

        for (List<String> tuple1 : relation1.getTuples()) {
            boolean matchFound = false;
            for (List<String> tuple2 : relation2.getTuples()) {
                if (matchesOnCommonAttributes(tuple1, tuple2, relation1, relation2, commonAttributes)) {
                    joinedTuples.add(combineTuples(tuple1, tuple2, relation1, relation2, commonAttributes));
                    matchFound = true;
                }
            }
            if (!matchFound) {
                List<String> combinedTuple = new ArrayList<>(tuple1);
                for (int i = 0; i < relation2.getAttributes().size() - commonAttributes.size(); i++) {
                    combinedTuple.add(null); // Add nulls for missing attributes from relation2
                }
                joinedTuples.add(combinedTuple);
            }
        }

        // Create and handle new joined relation
        Relation joinedRelation = new Relation("LeftOuterJoined", joinedAttributes, joinedTuples);
        return joinedRelation;
    }


    public Relation rightOuterJoin(Query query, Relation relation1, Relation relation2) {
        List<String> commonAttributes = findCommonAttributes(relation1, relation2);
        List<String> joinedAttributes = combineAttributes(relation1, relation2, commonAttributes);
        List<List<String>> joinedTuples = new ArrayList<>();

        for (List<String> tuple2 : relation2.getTuples()) {
            boolean matchFound = false;
            for (List<String> tuple1 : relation1.getTuples()) {
                if (matchesOnCommonAttributes(tuple1, tuple2, relation1, relation2, commonAttributes)) {
                    joinedTuples.add(combineTuples(tuple1, tuple2, relation1, relation2, commonAttributes));
                    matchFound = true;
                }
            }
            if (!matchFound) {
                // Create a new tuple with nulls for attributes from relation1
                List<String> combinedTuple = new ArrayList<>();
                for (int i = 0; i < relation1.getAttributes().size(); i++) {
                    combinedTuple.add(null);
                }
                combinedTuple.addAll(tuple2);
                joinedTuples.add(combinedTuple);
            }
        }

        // Create and return the new joined relation
        return new Relation("RightOuterJoined", joinedAttributes, joinedTuples);
    }


    public Relation fullOuterJoin(Query query, Relation relation1, Relation relation2) {
        // Perform left outer join
        Relation leftJoin = leftOuterJoin(query, relation1, relation2);

        // Perform right outer join
        Relation rightJoin = rightOuterJoin(query, relation1, relation2);

        // Combine results of left and right joins
        Set<List<String>> combinedTuplesSet = new HashSet<>(leftJoin.getTuples());
        combinedTuplesSet.addAll(rightJoin.getTuples());

        // Convert the set back to a list
        List<List<String>> combinedTuples = new ArrayList<>(combinedTuplesSet);

        // Create and return the new joined relation
        List<String> joinedAttributes = combineAttributes(relation1, relation2, findCommonAttributes(relation1, relation2));
        return new Relation("FullOuterJoined", joinedAttributes, combinedTuples);
    }


    private List<String> findCommonAttributes(Relation relation1, Relation relation2) {
        List<String> commonAttributes = new ArrayList<>();
        for (String attr1 : relation1.getAttributes()) {
            if (relation2.getAttributes().contains(attr1)) {
                commonAttributes.add(attr1);
            }
        }
        return commonAttributes;
    }

    private List<String> combineAttributes(Relation relation1, Relation relation2, List<String> commonAttributes) {
        List<String> joinedAttributes = new ArrayList<>(relation1.getAttributes());
        for (String attr2 : relation2.getAttributes()) {
            if (!commonAttributes.contains(attr2)) {
                joinedAttributes.add(attr2);
            }
        }
        return joinedAttributes;
    }

    private boolean matchesOnCommonAttributes(List<String> tuple1, List<String> tuple2, Relation relation1, Relation relation2, List<String> commonAttributes) {
        for (String attr : commonAttributes) {
            int index1 = relation1.getAttributes().indexOf(attr);
            int index2 = relation2.getAttributes().indexOf(attr);
            if (!tuple1.get(index1).equals(tuple2.get(index2))) {
                return false;
            }
        }
        return true;
    }

    private List<String> combineTuples(List<String> tuple1, List<String> tuple2, Relation relation1, Relation relation2, List<String> commonAttributes) {
        List<String> combinedTuple = new ArrayList<>(tuple1);
        for (String attr2 : relation2.getAttributes()) {
            if (!commonAttributes.contains(attr2)) {
                combinedTuple.add(tuple2.get(relation2.getAttributes().indexOf(attr2)));
            }
        }
        return combinedTuple;
    }



    public Relation union(Query query, Relation relation1, Relation relation2) {
        // Check if the attributes of both relations are the same
        if (!relation1.getAttributes().equals(relation2.getAttributes())) {
            throw new IllegalArgumentException("Relations do not have the same attributes for union operation.");
        }

        // Create a set to hold the unique tuples
        Set<List<String>> uniqueTuples = new HashSet<>();

        // Add all tuples from both relations to the set
        uniqueTuples.addAll(relation1.getTuples());
        uniqueTuples.addAll(relation2.getTuples());

        // Convert the set back to a list
        List<List<String>> combinedTuples = new ArrayList<>(uniqueTuples);

        // Create and return the new union relation
        return new Relation("Union", relation1.getAttributes(), combinedTuples);
    }


    public Relation intersect(Query query, Relation relation1, Relation relation2) {
        // Check if the attributes of both relations are the same
        if (!relation1.getAttributes().equals(relation2.getAttributes())) {
            throw new IllegalArgumentException("Relations do not have the same attributes for intersection operation.");
        }

        // Create a set to hold tuples from relation1 for easy lookup
        Set<List<String>> tuplesSet1 = new HashSet<>(relation1.getTuples());

        // Create a list to store the intersected tuples
        List<List<String>> intersectedTuples = new ArrayList<>();

        // Iterate over tuples of relation2 and add to the list if present in relation1
        for (List<String> tuple2 : relation2.getTuples()) {
            if (tuplesSet1.contains(tuple2)) {
                intersectedTuples.add(tuple2);
            }
        }

        // Create and return the new intersected relation
        return new Relation("Intersection", relation1.getAttributes(), intersectedTuples);
    }


    public Relation difference(Query query, Relation relation1, Relation relation2) {
        // Check if the attributes of both relations are the same
        if (!relation1.getAttributes().equals(relation2.getAttributes())) {
            throw new IllegalArgumentException("Relations do not have the same attributes for difference operation.");
        }

        // Create a set for tuples from relation2 for easy lookup
        Set<List<String>> tuplesSet2 = new HashSet<>(relation2.getTuples());

        // Create a list to store the tuples that are only in relation1
        List<List<String>> differenceTuples = new ArrayList<>();

        // Iterate over tuples of relation1 and add to the list if not present in relation2
        for (List<String> tuple1 : relation1.getTuples()) {
            if (!tuplesSet2.contains(tuple1)) {
                differenceTuples.add(tuple1);
            }
        }

        // Create and return the new difference relation
        return new Relation("Difference", relation1.getAttributes(), differenceTuples);
    }



}
