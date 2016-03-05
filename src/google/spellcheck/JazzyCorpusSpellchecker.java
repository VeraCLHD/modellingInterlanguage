package google.spellcheck;

import com.swabunga.spell.engine.EditDistance;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import processing.CorpusReader;

public class JazzyCorpusSpellchecker {

    private static SpellDictionaryHashMap dictionary = null;
    private static SpellChecker spellChecker = null;
    
    private static Map<String, List<String>> suggestions = new HashMap<String, List<String>>();

    static {

        try {

            dictionary =
                new SpellDictionaryHashMap(new
                File("dictionaries/english/eng_com.dic"));
            
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        spellChecker = new SpellChecker(dictionary);
    }

    public static String getSuggestionWithMinDistance(String word,
        int threshold) {
    	
    	int minDistance = Integer.MAX_VALUE;
    	String suggestionWithMinDistance = "no_suggestion";
    	List<Word> suggestions = spellChecker.getSuggestions(word, threshold);
    	
    	if(suggestions.size()>0){
        	for (Word suggestion: suggestions){
        		
        		int distance = EditDistance.getDistance(word,suggestion.toString());
        		/*System.out.println("WORD: " + word + " SUGGESTION: " + suggestion.toString());
    			System.out.println("DISTANCE: " + distance);
    			System.out.println("COST " + suggestion.getCost());
    			System.out.println("MIN_DIST: " + minDistance);*/
        		if(distance < minDistance){
        			minDistance = distance;
        			suggestionWithMinDistance = suggestion.toString();
        			if (minDistance == 0){
        				break;
        			}
        			
        		}
        	
        	}
    	} 
    	

    	//System.out.println("WORD: " + word);
    	//System.out.println(suggestionWithMinDistance.toString());
    	
        return suggestionWithMinDistance;
    }
    


	public static Map<String, List<String>> getSuggestions() {
		return suggestions;
	}


	public static void setSuggestions(Map<String, List<String>> suggestions) {
		JazzyCorpusSpellchecker.suggestions = suggestions;
	}
	
	public static void main(String[] args) {
    	
    	
    	List<String> listForText = CorpusReader.readFileAsWordsList(CorpusReader.getGerPath() + "test_P1_GER_high_10392.txt");
    	
    	List<Word> suggestions = spellChecker.getSuggestions("knowledges", 0);
    	System.out.println(suggestions.toString());
    	/*for(String word: listForText){
    		String firstSuggestion = JazzyCorpusSpellchecker.getSuggestionWithMinDistance(word.toLowerCase(), 0).toString();
    	}*/
    	
	}
}
