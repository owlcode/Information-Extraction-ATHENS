package lab5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static javax.swing.UIManager.put;

/**
 * Skeleton class for a program that maps the entities from one KB to the
 * entities of another KB.
 *
 * @author Fabian
 */
public class EntityMapper {
    static Map<String, String> links;
    static KnowledgeBase kb1;
    static KnowledgeBase kb2;

    public static void main(String[] args) throws IOException {
        initializeLinks();
//        args = initializeMockArgs();
        kb1 = new KnowledgeBase(new File(args[0]));
        kb2 = new KnowledgeBase(new File(args[1]));

        for (String entity1 : kb1.facts.keySet()) {
            System.out.println(entity1 + "\t" + findMostLikelyCandidate(entity1));
        }
    }

    static String findMostLikelyCandidate(String entity) {
        String mostLikelyCandidate = null;
        Set<String> entityOneRdfsLabel = kb1.facts.get(entity).get("rdfs:label");
        HashMap<String, Integer> ranks = new HashMap<>();

        for (String entity2 : kb2.facts.keySet()) {
            Set<String> entityTwoRdfsLabel = kb2.facts.get(entity2).get("rdfs:label");

            if (areCommonAndNotTrivial(entityOneRdfsLabel, entityTwoRdfsLabel)) {
                mostLikelyCandidate = entity2;
                ranks.put(entity2, 0);

                for (Map.Entry<String, Set<String>> entry : kb1.facts.get(entity).entrySet()) {
                    String yagoKey = entry.getKey();

                    if (yagoKey != null && !yagoKey.equals("rdfs:label")) {

                        // check if the same
                        if (linksHaveSameValue(entity, entity2, yagoKey)) {
                            ranks.put(entity2, ranks.get(entity2) + 1);
                        }
                    }
                }
            }

            Integer maximum = -1;
            for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
                if (entry.getValue().compareTo(maximum) > 0) {
                    maximum = entry.getValue();
                    mostLikelyCandidate = entry.getKey();
                }
            }
        }

        return mostLikelyCandidate;
    }

    private static boolean linksHaveSameValue(String entity, String entity2, String key) {
        String mapping = links.get(key);

        if (mapping == null) {
//            System.err.println(key);
            return false;
        }

        Set<String> yagoFact = kb1.facts.get(entity).get(key);
        Set<String> evilFact = kb2.facts.get(entity2).get(mapping);

        if (evilFact == null) {
            return false;
        }

        return evilFact.equals(yagoFact);
    }

    private static boolean areCommonAndNotTrivial(Set<String> a, Set<String> b) {
        Set<String> intersection = new HashSet<String>(a);
        intersection.retainAll(b);

        return intersection.size() > 0 && !(intersection.contains("\"$\"") && intersection.size() == 1);
    }

    private static void initializeLinks() {
        links = new HashMap<String, String>() {{
            put("<wasCreatedOnDate>", "<releaseDate>");
            put("<wasBornOnDate>", "<birthDate>");
            put("<diedOnDate>", "<deathDate>");
            put("<hasDuration>", "<runtime>");
            put("<hasPages>", "<numberOfPages>");
            put("<hasNumberOfPeople>", "<populationTotal>");
            put("<hasPopulationDensity>", "<populationDensity>");
            put("<happenedOnDate>", "<date>");
            put("<hasArea>", "<areaTotal>");
            put("<hasMotto>", "<motto>");
        }};
    }

    private static String[] initializeMockArgs() {
        return new String[]{
//                "/Users/dawidsowa/Studia/athens/src/lab5/yago.tsv",
                "/Users/dawidsowa/Studia/athens/src/lab5/yago-anonymous.tsv",
                "/Users/dawidsowa/Studia/athens/src/lab5/dbpedia.tsv"
        };
    }
}
