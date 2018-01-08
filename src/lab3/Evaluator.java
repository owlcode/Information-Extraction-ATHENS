package lab3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Skeleton for an evaluator
 */
public class Evaluator {
    protected static BufferedReader gold;
    protected static BufferedReader result;

    /**
     * Takes as arguments (1) the gold standard and (2) the output of a program.
     * Prints to the screen one line with the precision
     * and one line with the recall.
     */
    public static void main(String[] args) throws Exception {
//        args = new String[]{
//                "/Users/dawidsowa/Studia/athens/src/lab3/gold-standard-sample.tsv",
//                "/Users/dawidsowa/Studia/athens/src/lab3/result.tsv"
//        };
        gold = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
        result = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]), "UTF-8"));
        result.readLine();

        Integer lines = 0;
        Integer match = 0;

        while (true) {
            try {
                lines++;

                String proper = gold.readLine();
                String main;
                if (proper == null) {
                    break;
                }
                result = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]), "UTF-8"));
                result.readLine();

                while (true) {
                    main = result.readLine();
                    if (main.split("\t")[0].equals(proper.split("\t")[0])) {
                        break;
                    }
                }

                if (proper.split("\t")[1].equals(main.split("\t")[1])) {
                    match++;
                }

            } catch (Exception s) {

            }
        }

        System.out.println(match + "\t / \t" + lines);
    }
}