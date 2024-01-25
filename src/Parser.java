import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public Relation parseRelation(String relationText) {
        // Remove any leading or trailing whitespace
        relationText = relationText.trim();

        // Extracting the relation name
        String name = relationText.substring(0, relationText.indexOf('(')).trim();

        // Extracting the attributes
        int startAttributes = relationText.indexOf('(') + 1;
        int endAttributes = relationText.indexOf(')');
        String[] attributes = relationText.substring(startAttributes, endAttributes).split("\\s*,\\s*");

        // Extracting the tuples
        int startTuples = relationText.indexOf('{') + 1;
        int endTuples = relationText.indexOf('}');
        String tuplesText = relationText.substring(startTuples, endTuples).trim();
        String[] tupleArray = tuplesText.split("\\s*;\\s*");

        List<List<String>> tuples = new ArrayList<>();
        for (String tuple : tupleArray) {
            String[] values = tuple.split("\\s*,\\s*");
            tuples.add(Arrays.asList(values));
        }

        return new Relation(name, Arrays.asList(attributes), tuples);

    }

    public Query parseQuery(String queryText) {
        // Parsing logic to create and return a Query object
        queryText = queryText.trim();
        Query query = new Query();

        if (queryText.toLowerCase().startsWith("select")) {
            query.setOperation("select");
            parseSelectQuery(queryText, query);
        } else if (queryText.toLowerCase().startsWith("project")) {
            query.setOperation("project");
            parseProjectQuery(queryText, query);
        } else if (queryText.contains(" inner join ")) {
            query.setOperation("inner join");
            parseJoinQuery(queryText, query);
        } else if (queryText.contains(" left outer join ")) {
            query.setOperation("left outer join");
            parseJoinQuery(queryText, query);
        } else if (queryText.contains(" right outer join ")) {
            query.setOperation("right outer join");
            parseJoinQuery(queryText, query);
        } else if (queryText.contains(" full outer join ")) {
            query.setOperation("full outer join");
            parseJoinQuery(queryText, query);

        } else  if (queryText.toLowerCase().contains(" union ") ||
                queryText.toLowerCase().contains(" intersect ") ||
                queryText.toLowerCase().contains(" difference ")) {
            parseSetOperationQuery(queryText, query);
        }
        return query;
    }

    private void parseSelectQuery(String queryText, Query query) {
        Pattern pattern = Pattern.compile("select\\s+(.*)\\((.*)\\)");
        Matcher matcher = pattern.matcher(queryText);
        if (matcher.find()) {
            query.setCondition(matcher.group(1).trim());
            query.setTargetRelation(matcher.group(2).trim());
        }
    }

    private void parseProjectQuery(String queryText, Query query) {
        Pattern pattern = Pattern.compile("project\\s+\\((.*)\\)\\((.*)\\)");
        Matcher matcher = pattern.matcher(queryText);
        if (matcher.find()) {
            String[] attributes = matcher.group(1).trim().split("\\s*,\\s*");
            query.setAttributes(Arrays.asList(attributes));
            query.setTargetRelation(matcher.group(2).trim());
        }
    }


    private void parseJoinQuery(String queryText, Query query) {
        Pattern pattern = Pattern.compile("(\\w+)\\s+(inner join|left outer join|right outer join|full outer join)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(queryText);
        if (matcher.find()) {
            query.setTargetRelation(matcher.group(1).trim()); // First relation
            query.setOperation(matcher.group(2).trim()); // Join type
            query.setSecondaryRelation(matcher.group(3).trim()); // Second relation
        }
    }



    private void parseSetOperationQuery(String queryText, Query query) {
        Pattern pattern = Pattern.compile("(\\w+)\\s+(union|intersect|difference)\\s+(\\w+)");
        Matcher matcher = pattern.matcher(queryText);
        if (matcher.find()) {
            query.setTargetRelation(matcher.group(1).trim());
            query.setOperation(matcher.group(2).trim());
            query.setSecondaryRelation(matcher.group(3).trim());
        }
    }


}

