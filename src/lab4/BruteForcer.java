package lab4;

import java.util.Arrays;

public class BruteForcer {
    private final char[] alphabet;
    private final int alphabetLength;
    private int[] indices;
    private char[] combination;

    public static final String NUMERIC_ALPHABET = "0123456789";
    public static final String LOWER_CASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_CASE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String SPECIAL_CHARACTERS = "!@$%";

    private BruteForcer(String alphabet) {
        this.alphabet = alphabet.toCharArray();
        this.alphabetLength = alphabet.length();

        indices = new int[10];
        combination = new char[]{'0','0','0','0','0','0','0','0','0','0'};
    }

    public static BruteForcer createNumericBruteForcer(){
        return new BruteForcer(NUMERIC_ALPHABET);
    }

    public static BruteForcer createAlphaBruteForcer(){
        return new BruteForcer(LOWER_CASE_ALPHABET + UPPER_CASE_ALPHABET);
    }

    public static BruteForcer createAlphaNumericBruteForcer(){
        return new BruteForcer(LOWER_CASE_ALPHABET + UPPER_CASE_ALPHABET + NUMERIC_ALPHABET);
    }


    public static BruteForcer createGenericBruteForcer(String alphabet){
        return new BruteForcer(alphabet);
    }

    public String computeNextCombination() {
        combination[0] = alphabet[indices[0]];
        String nextCombination = String.valueOf(combination);

        if (indices[0] < alphabetLength - 1) {
            indices[0]++;
        } else {
            for (int i = 0; i < indices.length; i++) {
                if (indices[i] < alphabetLength - 1) {
                    indices[i]++;
                    combination[i] = alphabet[indices[i]];
                    break;
                } else {
                    indices[i] = 0;
                    combination[i] = alphabet[indices[i]];

                    if (i == indices.length - 1) {
                        indices = Arrays.copyOf(indices, indices.length + 1);
                        combination = Arrays.copyOf(combination, combination.length + 1);
                        combination[combination.length - 1] = alphabet[indices[indices.length - 1]];
                        break;
                    }
                }
            }
        }

        return nextCombination;
    }
}