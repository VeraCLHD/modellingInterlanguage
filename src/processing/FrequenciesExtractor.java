package processing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FrequenciesExtractor {
	
	
	public static Map<String,Long> gigaWordFrequencies = new HashMap<String,Long>();
	private static Long totalWordCount = 0L; 
	
	
	public static Long getTotalWordCount() {
		return totalWordCount;
	}

	public static void extractAbsoluteFrequenciesGigaword(){
		System.out.println("Extracting word freqs gigaword");
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Properties.GIGAWORD_PATH), "UTF-8"))) {
			String line = new String();
			while (br.ready()) {
				line = br.readLine().trim();
				String[] frequencyLine = line.split(" ");
				String word = frequencyLine[1].trim();
				if(word.matches(".*\\d+.*")){
					word = "NUM";
				} 
				// !word.matches("\\p{Punct}*") && 
				else if (!word.matches("[a-z]+.*")){
					continue;
				}
				
				Long frequency = Long.parseLong(frequencyLine[0].trim());
				if(FrequenciesExtractor.gigaWordFrequencies.containsKey(word)){
					Long originalFrequency = FrequenciesExtractor.gigaWordFrequencies.get(word);
					FrequenciesExtractor.gigaWordFrequencies.put(word, originalFrequency + frequency);
				} else{
					FrequenciesExtractor.gigaWordFrequencies.put(word, frequency);
				}
				
				FrequenciesExtractor.totalWordCount += frequency;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// only looking at the word frequency ffrom the gigaword corpus, not the corpus itself
	public static Double computeRelativeFrequencyForWord(String word){
		Double sfi = 0.0;
		if(gigaWordFrequencies.containsKey(word)){
			Long absoluteFrequency = gigaWordFrequencies.get(word);
			Double realtiveFrequency = (double) absoluteFrequency /totalWordCount;
			 sfi = 10*(Math.log10(realtiveFrequency)+10);
		}
		
		return sfi;
	}
	
	
	

	public Map<String,Long> getGigaWordFrequencies() {
		return gigaWordFrequencies;
	}


	public void setGigaWordFrequencies(Map<String,Long> gigaWordFrequencies) {
		this.gigaWordFrequencies = gigaWordFrequencies;
	}
	public static void main(String[] args) {
		Long a = 644792L;
		
		Long b = 3945554028L;
		Double c =  ((double)a/b);
		System.out.println(c);
	}

}
