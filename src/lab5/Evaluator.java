package lab5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Evaluator {
    protected static BufferedReader gold;
    protected static BufferedReader result;

    public static void main(String[] args) throws Exception {
        args = new String[]{
                "/Users/dawidsowa/Studia/athens/src/lab5/gold-standard-sample.tsv",
                "/Users/dawidsowa/Studia/athens/src/lab5/output.tsv"
        };

        gold = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
        result = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]), "UTF-8"));
        result.readLine();

        Double lines = 0.0;
        Double matches = 0.0;
        Double badMatches = 0.0;
        Double nulls = 0.0;

        while (true) {
            String goldLine = gold.readLine();
            if (goldLine == null) break;
            String gLEntity = goldLine.split("\t")[0];
            String gLMatchedEntity = goldLine.split("\t")[1];
            lines++;

            while (true) {
                String resultLine = result.readLine();
                if (resultLine == null) break;
                String rLEntity = resultLine.split("\t")[0];
                String rLMatchedEntity = resultLine.split("\t")[1];

                if (gLEntity.equals(rLEntity)) {
                    if (gLMatchedEntity.equals(rLMatchedEntity)) {
                        matches++;
                    } else if (rLMatchedEntity.equals("null")) {
                        nulls++;
                    } else {
                        badMatches++;
                    }
                }
            }

            result = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]), "UTF-8"));
            result.readLine();
        }

        System.out.println(matches + " / " + lines);
        System.out.println("nulls " + nulls);
        System.out.println("bad matches " + badMatches);
        System.out.println("Accuracy: " + (matches / (badMatches + matches)) * 100.0 + "%");
        System.out.println("Precision: " + (3.0/2.0));
        System.out.println("Recall: " + (3/4));
    }
}
