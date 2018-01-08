package lab2;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Skeleton class to perform disambiguation
 *
 * @author Jonathan Lajus
 */
public class Disambiguation {

    /**
     * This program takes 3 command line arguments, namely the paths to:
     * - yagoLinks.tsv
     * - yagoLabels.tsv
     * - wikipedia-ambiguous.txt
     * in this order. The program prints statements of the form:
     * <pageTitle>  TAB  <yagoEntity> NEWLINE
     * It is OK to skip articles.
     */
    public static void main(String[] args) throws IOException {
        args = new String[]{
                "/Users/dawidsowa/Studia/athens/src/lab2/yago-ambiguous/yagoLinks.tsv",
                "/Users/dawidsowa/Studia/athens/src/lab2/yago-ambiguous/yagoLabels.tsv",
                "/Users/dawidsowa/Studia/athens/src/lab2/wikipedia-ambiguous.txt"
        };

        if (args.length < 3) {
            System.err.println("usage: Disambiguation <yagoLinks> <yagoLabels> <wikiText>");
            return;
        }
        File dblinks = new File(args[0]);
        File dblabels = new File(args[1]);
        File wiki = new File(args[2]);

        SimpleDatabase db = new SimpleDatabase(dblinks, dblabels);

        try (Parser parser = new Parser(wiki)) {
            while (parser.hasNext()) {
                Page nextPage = parser.next();
                String pageTitle = nextPage.title; // "Clinton_1"
                String pageContent = nextPage.content; // "Hillary Clinton was..."
                String pageLabel = nextPage.label(); // "Clinton"
                String correspondingYagoEntity = Disambiguation.findYagoEntity(db, nextPage);
                System.out.println(pageTitle + "\t" + correspondingYagoEntity);
            }
        }
    }

    private static String findYagoEntity(SimpleDatabase db, Page page) {
        Set<String> possibleYagoEntities = db.reverseLabels.get(page.label());
        String[] content = parseArticleContentToKeywords(page.content.split(" "));
        Integer maxSimilarity = -1;
        ArrayList<Integer> similarities = new ArrayList<>();

        for (String yagoEntity : possibleYagoEntities) {
            Set<String> links = db.links.get(yagoEntity);

            ArrayList<String> keywords = new ArrayList<>();

            for (String link : links) {
                Set<String> set = db.labels.get(link);

                if (set != null) {
                    for (String str : set) {
                        str = str.toLowerCase();
                        String[] keywordsList = str.split(" ");
                        Collections.addAll(keywords, keywordsList);
                    }
                }
            }

            String[] yagoContent = new String[keywords.size()];
            yagoContent = keywords.toArray(yagoContent);
            Integer sim = findSimilarity(content, yagoContent);
            if (sim > maxSimilarity) {
                maxSimilarity = sim;
            }
            similarities.add(sim);
        }

        int position = 0;
        for (int i = 0; i < similarities.size(); i++) {
            if (similarities.get(i).equals(maxSimilarity)) {
                position = i;
                break;
            }
        }

        String[] entitiesArray = possibleYagoEntities.toArray(new String[possibleYagoEntities.size()]);
        return entitiesArray[position];
    }

    private static String[] parseArticleContentToKeywords(String[] content) {
        String[] unuseful = new String[]{"is", "a", "and", "by", "on", "the", "an", "are", "to", "of", "that", "in"," "};
        ArrayList<String> useful = new ArrayList<>();

        for (String s : content) {
            s = s.replaceAll("[.,]", "");
            s = s.toLowerCase();
            int index = Arrays.asList(unuseful).indexOf(s);
            if (index == -1) {
                useful.add(s);
            }
        }

        String[] result = new String[useful.size()];
        result = useful.toArray(result);
        return result;
    }

    private static Integer findSimilarity(String[] content, String[] yagoContent) {
        ArrayList<String> c = new ArrayList<String>(Arrays.asList(content));
        c.retainAll(
                new ArrayList<String>(Arrays.asList(yagoContent))
        );
        return c.size();
    }
}