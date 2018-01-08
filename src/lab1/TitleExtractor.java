package lab1;

import java.io.File;
import java.io.IOException;

/**
 * Skeleton for a title extractor
 *
 * @author Fabian M. Suchanek
 */
public class TitleExtractor {

    /**
     * Given a Wikipedia corpus as argument, prints the titles of the articles,
     * one per line
     */
    public static void main(String args[]) throws IOException {
//         args = new String[] { "/Users/dawidsowa/Studia/athens/src/lab1/wikipedia-first.txt"};

         File file = new File(args[0]);
         Parser parser = new Parser(file);

         while (parser.hasNext()) {
             System.out.println(parser.next().title);
         }
    }

}