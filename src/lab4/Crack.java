package lab4;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Skeleton for the password cracking task
 *
 * @author Fabian
 */
public class Crack {

    /**
     * The name used for the Web service
     */
    public static String name;
    protected static BufferedReader dict;
    protected static BufferedReader nouns;

    /**
     * List of accounts
     */
    public static List<Integer> accounts;

    /**
     * Loads a file as a list of strings, one per line
     */
    public static List<String> load(File f) {
        try {
            return (java.nio.file.Files.readAllLines(f.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to crack the accounts with the password, returns TRUE if it worked
     */
    public static boolean crack(String password) {
        if (Collections.binarySearch(accounts, password.hashCode()) >= 0) {
            try (Scanner s = new Scanner(
                    new URL("http://julienromero.com/ATHENS/submit?name=" + name + "&password=" + password)
                            .openStream())) {
                if (s.nextLine().equals("True")) {
                    accounts.remove(new Integer(password.hashCode()));
                    System.out.println("Password: " + password);
                    try (FileWriter fw = new FileWriter("myfile.txt", true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        out.println("Password: " + password);
                    } catch (IOException e) {
                        //exception handling left as an exercise for the reader
                    }
                    return (true);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (false);
    }

    /**
     * Takes as arguments (1) a file with account numbers and (2) your name (as
     * given on our Web page). Prints the passwords one per line (in any order).
     */
    public static void main(String[] args) throws Exception {
        // Uncomment for your convenience, comment it out again before
        // submitting
        args = new String[]{
                "/Users/dawidsowa/Studia/athens/src/lab4/sowa.txt",
                "sowa",
                "/Users/dawidsowa/Studia/athens/src/lab4/towns.txt"
        };
        accounts = load(new File(args[0])).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        name = args[1];
        Collections.sort(accounts);

        dict = new BufferedReader(new InputStreamReader(new FileInputStream(args[2]), "UTF-8"));

        BruteForcer bf = BruteForcer.createNumericBruteForcer();
        Random r = new Random();
        while (true) {
            String str = bf.computeNextCombination();
            System.out.println(str);

//            String s = dict.readLine();
//            if (s == null) {
//                break;
//            }


            Crack.crack(str);
        }
    }
}