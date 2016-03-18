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

    private ArrayList<Character> getPossibleChars(String regEx){
        // creo lista vacia de caracteres posibles a usar
        ArrayList<Character> possible_chars = new ArrayList<Character>();

        // armo lista de posibles segun el tipo de expresion (cualquier letra, lista, o literal)
        if(regEx.charAt(index) == '.') {
            possible_chars.add('.'); // serian todos, lo simulo con un if al final para no meter una lista con los 255 caracteres ASCII
        } else if(regEx.charAt(index) == '['){
            ++index;
            while(index < regEx.length() && regEx.charAt(index) != ']'){
                possible_chars.add(regEx.charAt(index));
                ++index;
            }
        } else {
            if(regEx.charAt(index) == '\\') {
                ++index;
            }
            possible_chars.add(regEx.charAt(index));
        }

        return possible_chars;
    }

    private int getQuantifier(String regEx){
        // veo si hay quantifier, si no hay es 1
        char posible_quant = 'a';
        if(index+1 < regEx.length()) {
            posible_quant = regEx.charAt(index + 1);
        }
        int min = 1, max = 1;
        if(posible_quant == '*'){
            min = 0;
            max = this.maxLength;
            ++index;
        } else if(posible_quant == '+'){
            min = 1;
            max = this.maxLength;
            ++index;
        } else if(posible_quant == '?'){
            min = 0;
            max = 1;
            ++index;
        }

        Random rand = new Random();
        int quantifier = rand.nextInt((max - min) + 1) + min;

        return quantifier;
    }

    private String generateCharsFromToken(ArrayList<Character> possible_chars, int quantifier){
        String chars = "";
        Random rand = new Random();
        for (int q = 0; q < quantifier; ++q) {
            int index = rand.nextInt((possible_chars.size() - 0)) + 0;
            char char_to_add = possible_chars.get(index);
            if (char_to_add == '.') {
                char_to_add = (char) (rand.nextInt((255 - 0) + 1) + 0);
            }
            chars = chars + Character.toString(char_to_add);
        }

        return chars;
    }

    private void parseNextToken(String regEx, int numberOfResults, ArrayList<String> result){
        ArrayList<Character> possible_chars = getPossibleChars(regEx);

        int quantifier = getQuantifier(regEx);

        // agrego caracteres del elemento de la regex al string final
        for(int n = 0; n < numberOfResults; ++n) {
            result.set(n, result.get(n) + generateCharsFromToken(possible_chars, quantifier));
        }
    }

    public List<String> generate(String regEx, int numberOfResults) {

        // perparo array de resultados
        ArrayList<String> result = new ArrayList<String>();
        for(int n = 0; n < numberOfResults; ++n){
            result.add("");
        }

        for( index = 0; index < regEx.length(); ++index){ // el for recorre los 'elementos' o 'tokens' de la regex, y tambien se fija su cuantificador
            parseNextToken(regEx, numberOfResults, result);
        }

        return result;
    }
}