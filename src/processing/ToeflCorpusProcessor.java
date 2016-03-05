package processing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ToeflCorpusProcessor {
	
	private  Set<String> wordsForEssay = new HashSet<String>();
	private String file;
	
	public ToeflCorpusProcessor(String file){
		this.setFile(file);
	}
	
	/**
	 * Reads a file line-wise and returns a list of the lines.
	 */
	public void filterRelevantWords() {
		System.out.println("Reading each file " + file);
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFile()), "UTF-8"))) {
			String line = new String();
			while (br.ready()) {
				line = br.readLine();
				String[] wordsInLine = line.split(" ");
				for (String word: wordsInLine){
					if(word.matches("\\p{Punct}*")){
						continue;
					} else if(word.matches(".*\\d+.*")){
						word = "NUM";
					}
					
					this.wordsForEssay.add(word.toLowerCase());
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("Guests".matches(".*\\d+.*"));

	}

	public  Set<String> getWordsForEssay() {
		return wordsForEssay;
	}

	public void setWordsForEssay(Set<String> wordsForEssay) {
		this.wordsForEssay = wordsForEssay;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
