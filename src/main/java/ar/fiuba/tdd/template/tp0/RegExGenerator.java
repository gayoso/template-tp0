package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;

public class RegExGenerator {

    private int maxLength;
    private int index;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public RegExGenerator() {
        this.maxLength = 5;
    }

    private ArrayList<Character> getPossibleChars(String regEx) {
        ArrayList<Character> possibleChars = new ArrayList<Character>();

        // here we assemble the lists of possible chars to match the token or list
        if (regEx.charAt(index) == '.') {
            possibleChars.add('.'); // instead of having a list of all possible chars, it is simulated later
        } else if (regEx.charAt(index) == '[') {
            ++index;
            while (index < regEx.length() && regEx.charAt(index) != ']') {
                possibleChars.add(regEx.charAt(index));
                ++index;
            }
        } else {
            if (regEx.charAt(index) == '\\') {
                ++index;
            }
            possibleChars.add(regEx.charAt(index));
        }

        return possibleChars;
    }

    private int getMinFromPossibleQuantifier(char possibleQuantifier) {
        switch (possibleQuantifier) {
            case '*':
                ++index;
                return 0;
            case '+':
                ++index;
                return 1;
            case '?':
                ++index;
                return 0;
            default:
                return 1;
        }
    }

    private int getMaxFromPossibleQuantifier(char possibleQuantifier) {
        switch (possibleQuantifier) {
            case '*':
                return this.maxLength;
            case '+':
                return this.maxLength;
            case '?':
                return 1;
            default:
                return 1;
        }
    }

    private int getQuantifier(String regEx) {
        char possibleQuant = 'a';
        if (index + 1 < regEx.length()) {
            possibleQuant = regEx.charAt(index + 1);
        }
        int min = getMinFromPossibleQuantifier(possibleQuant);
        int max = getMaxFromPossibleQuantifier(possibleQuant);

        Random rand = new Random();
        int quantifier = rand.nextInt((max - min) + 1) + min;

        return quantifier;
    }

    private StringBuffer generateCharsFromToken(ArrayList<Character> possibleChars, int quantifier) {
        StringBuffer chars = new StringBuffer("");
        Random rand = new Random();
        for (int q = 0; q < quantifier; ++q) {
            int index = rand.nextInt((possibleChars.size() - 0)) + 0;
            char charToAdd = possibleChars.get(index);
            if (charToAdd == '.') {
                charToAdd = (char) (rand.nextInt((255 - 32) + 1) + 32);
            }
            chars.append(charToAdd);
        }

        return chars;
    }

    private void parseNextToken(String regEx, int numberOfResults, ArrayList<StringBuffer> result) {
        ArrayList<Character> possibleChars = getPossibleChars(regEx);

        int quantifier = getQuantifier(regEx);

        // adds the generatres charcters for the element to each result
        for (int n = 0; n < numberOfResults; ++n) {
            result.set(n, result.get(n).append(generateCharsFromToken(possibleChars, quantifier)));
        }
    }

    private ArrayList<String> stringBufferArrayToStringArray(ArrayList<StringBuffer> buf) {
        ArrayList<String> resultAsString = new ArrayList<String>();
        for (int i = 0; i < buf.size(); ++i) {
            resultAsString.add(buf.get(i).toString());
        }
        return resultAsString;
    }

    public List<String> generate(String regEx, int numberOfResults) {

        // initializes the results array
        ArrayList<StringBuffer> result = new ArrayList<StringBuffer>();
        for (int n = 0; n < numberOfResults; ++n) {
            result.add(new StringBuffer(""));
        }

        // loops through each 'element' (as in symbol + quantifier) and generates a valid expression for it
        for (index = 0; index < regEx.length(); ++index) {
            parseNextToken(regEx, numberOfResults, result);
        }

        return stringBufferArrayToStringArray(result);
    }
}