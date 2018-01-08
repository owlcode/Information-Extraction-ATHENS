package lab2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

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
        args = new String[]{
                "/Users/dawidsowa/Studia/athens/src/lab2/goldstandard-sample.tsv",
                "/Users/dawidsowa/Studia/athens/src/lab2/result.tsv"
        };

        gold = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
        result = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]), "UTF-8"));
        result.readLine();
        double lines = 0;
        double match = 0;
        while (true) {
            try {
                lines++;

                String proper = gold.readLine();
                String main = result.readLine();

                if (proper == null || main == null) {
                    throw new Exception();
                }

                proper = proper.split("\t")[1];
                main = main.split("\t")[1];

                if (proper.equals(main)) {
                    match++;
                }
            } catch (Exception e) {
                break;
            }
        }

        System.out.println((match / lines) * 100 + " %");
        System.out.println(match + "\t / \t" + lines);
    }
}