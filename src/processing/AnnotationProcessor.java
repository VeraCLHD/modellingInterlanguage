package processing;

import google.spellcheck.JazzyCorpusSpellchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.swabunga.spell.engine.Word;

public class AnnotationProcessor {
	
	private static final String ANNOTATED_PATH = "TOEFL/annotated/";
	private static final String OUTPUT_PATH = "TOEFL/OUTPUT";
	
	
	/**
	 * Reads a file line-wise and returns a list of the lines.
	 */
	public static ArrayList<String> extractAnnotationsList(List<String> wordsList) {
		ArrayList<String> annotations = new ArrayList<String>();
				for (String word: wordsList){
					if(word.contains("_")){
						annotations.add(word);
					}
				}
				
			
		 
		return annotations;
	}
	
	
	public static List<String> processAnnotation(String raw_annotation){
		// contains two elements - the misspelling and the annotator's suggestion
		List<String> result = new ArrayList<String>();
		
		if(raw_annotation.contains("[space]")){
			raw_annotation = raw_annotation.replace("[space]", " ");
		}
		
		String[] elementsOfAnnotation = raw_annotation.split("_");
		
		String annotatorSuggestion = elementsOfAnnotation[elementsOfAnnotation.length-1];
		String originalWord = "";
		
		if(elementsOfAnnotation.length > 2){
			for(int i=0;i<elementsOfAnnotation.length-1; i++){
				originalWord += elementsOfAnnotation[i].trim();
			}
		} else{
			originalWord = elementsOfAnnotation[0];
		}
		
		if(annotatorSuggestion.contains("[space]") || originalWord.contains("[space]")){
			//fusion error:was?
			
		}
		
		result.add(originalWord);
		result.add(annotatorSuggestion);

		return result;
		
	}
	
	public static void createInstancesAndOutput(){
		List<String> filesInFolders = FileFilter_Toefl.findToeflFiles(new File(ANNOTATED_PATH));
		// id, L1, proficiency,prompt, test/train_dev, word, annotated, first_suggestion
		System.out.println("create instances");
		FrequenciesExtractor.extractAbsoluteFrequenciesGigaword();
		System.out.println(FrequenciesExtractor.getTotalWordCount());
		
		for(String file: filesInFolders){
			ToeflCorpusProcessor toefl = new ToeflCorpusProcessor(file);
			toefl.filterRelevantWords();
			List<String> essayWords = new ArrayList<String>(toefl.getWordsForEssay());
			List<String> annotations = AnnotationProcessor.extractAnnotationsList(essayWords);
			
			for(String annotation: annotations){
				List<String> misspellingAndSuggestion = AnnotationProcessor.processAnnotation(annotation);
				
				String spellCheckerSuggestion = JazzyCorpusSpellchecker.getSuggestionWithMinDistance(misspellingAndSuggestion.get(0).toLowerCase(), 0);
				Double wordFrequency = FrequenciesExtractor.computeRelativeFrequencyForWord(misspellingAndSuggestion.get(1));
				
				AnnotationProcessor.writeInstancesToFile(file, misspellingAndSuggestion,
						spellCheckerSuggestion, wordFrequency);
			}
		}
		

	}
	

	private static void writeInstancesToFile(String file,
			List<String> misspellingAndSuggestion,
			String spellCheckerSuggestion, Double wordFrequency) {
		
		List<String> lineOutput = new ArrayList<String>();
		String[] filePath = file.split("\\\\");
		file = filePath[filePath.length-1];
		String[] elementsOfFile = file.split("_");
		//System.out.println(Arrays.toString(elementsOfFile));
		lineOutput.add(elementsOfFile[4]);
		lineOutput.add(elementsOfFile[2]);
		lineOutput.add(elementsOfFile[3]);
		lineOutput.add(elementsOfFile[1]);
		lineOutput.add(elementsOfFile[0]);
		
		
		lineOutput.add(misspellingAndSuggestion.get(0));
		lineOutput.add(misspellingAndSuggestion.get(1));
		
		lineOutput.add(spellCheckerSuggestion.toString());
		
		lineOutput.add(String.valueOf(wordFrequency));			
		
		String line = "";
		for(String element: lineOutput){
			line += element + "\t"; 
		}
		Writer.appendLineToFile(line, OUTPUT_PATH + "/annotation_output.txt");
	
	}
	
	
	
	
	public static void main(String[] args) {
		AnnotationProcessor.createInstancesAndOutput();
		//SSystem.out.println(Long.MAX_VALUE);
	}



}
