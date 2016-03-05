package processing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FrequenciesExtractor {
	
	private static final String GIGAWORD_PATH = "frequencies/gigaword_frequencies.txt";
	private static Map<String,Long> gigaWordFrequencies = new HashMap<String,Long>();
	private static Long totalWordCount = 0L; 
	
	
	public static Long getTotalWordCount() {
		return totalWordCount;
	}

	public static void extractAbsoluteFrequenciesGigaword(){
		System.out.println("Extracting word freqs gigaword");
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(GIGAWORD_PATH), "UTF-8"))) {
			String line = new String();
			while (br.ready()) {
				line = br.readLine();
				String[] frequencyLine = line.trim().split(" ");
				String word = frequencyLine[1];
				if(word.matches(".*\\d+.*")){
					word = "NUM";
				} if (!word.matches("\\p{Punct}*")){
					Long frequency = Long.parseLong(frequencyLine[0]);
					FrequenciesExtractor.gigaWordFrequencies.putIfAbsent(word, frequency);
					FrequenciesExtractor.totalWordCount += frequency;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// only looking at the word frequency ffrom the gigaword corpus, not the corpus itself
	public static Double computeRelativeFrequencyForWord(String word){
		Double sfi = 0.0;
		if(gigaWordFrequencies.containsKey(word)){
			Long realtiveFrequency = gigaWordFrequencies.get(word)/totalWordCount;
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
		// TODO Auto-generated method stub

	}

}
