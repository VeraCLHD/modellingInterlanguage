package processing;

import processing.Editor;

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

import jazzy.spellcheck.JazzyCorpusSpellchecker;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.swabunga.spell.engine.EditDistance;
import com.swabunga.spell.engine.Word;


public class AnnotationProcessor {
	
	
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
		
		result.add(originalWord);
		result.add(annotatorSuggestion);

		return result;
		
	}
	
	public static void createInstancesAndOutput(){
		// delete the files if existent
		Editor.deleteFile(Properties.OUTPUT_PATH  + Properties.outputFile);
		Editor.deleteFile(Properties.OUTPUT_PATH  + Properties.CORPUS_STATS);
		
		List<String> filesInFolders = FileFilter_Toefl.findToeflFiles(new File(Properties.ANNOTATED_PATH));
		// id, L1, proficiency,prompt, test/train_dev, word, annotated, first_suggestion
		System.out.println("create instances");
		FrequenciesExtractor.extractAbsoluteFrequenciesGigaword();
		System.out.println(FrequenciesExtractor.getTotalWordCount());
		
		for(String file: filesInFolders){
			ToeflCorpusProcessor toefl = new ToeflCorpusProcessor(file);
			toefl.filterRelevantWords();
			List<String> essayWords = new ArrayList<String>(toefl.getWordsForEssay());
			List<String> annotations = AnnotationProcessor.extractAnnotationsList(essayWords);
			
			
			List<String> relevantStats = new ArrayList<String>();
			
			Integer numFusionErrors = 0;
			for(String annotation: annotations){
				
				List<String> misspellingAndSuggestion = AnnotationProcessor.processAnnotation(annotation);
				
				String misspelling = misspellingAndSuggestion.get(0).toLowerCase();
				String suggestion = misspellingAndSuggestion.get(1);
				Integer wordLength = suggestion.length();
				
				if(misspelling.contains(" ") || suggestion.contains(" ")){
					numFusionErrors +=1;
				}
				
				Integer errorSeverity = EditDistance.getDistance(misspelling,suggestion.toString());
				
				String spellCheckerSuggestion = JazzyCorpusSpellchecker.getSuggestionWithMinDistance(misspelling, 0);
				Double wordFrequency = FrequenciesExtractor.computeRelativeFrequencyForWord(suggestion);
				
				AnnotationProcessor.writeInstancesAnnotationOutput(file, misspellingAndSuggestion,
						spellCheckerSuggestion, wordFrequency, errorSeverity, wordLength);
			}
			
			relevantStats.add(Integer.toString(essayWords.size()));
			relevantStats.add(Integer.toString(annotations.size()));
			relevantStats.add(Integer.toString(numFusionErrors));
			AnnotationProcessor.writeStats(file, relevantStats);
		}
		

	}
	

	private static void writeInstancesAnnotationOutput(String file,
			List<String> misspellingAndSuggestion,
			String spellCheckerSuggestion, Double wordFrequency, Integer errorSeverity, Integer wordLength) {
		
		List<String> lineOutput = extractInformationFromFilename(file);
		
		String misspelling = misspellingAndSuggestion.get(0);
		String suggestion = misspellingAndSuggestion.get(1);
		
		lineOutput.add(misspelling);
		lineOutput.add(suggestion);
		
		lineOutput.add(spellCheckerSuggestion.toString());
		
		lineOutput.add(String.valueOf(wordFrequency));
		
		lineOutput.add(String.valueOf(errorSeverity));
		lineOutput.add(String.valueOf(wordLength));
		if(misspelling.contains(" ") || suggestion.contains(" ")){
			lineOutput.add("fusion");
		} else{
			lineOutput.add("-");
		}
		
		String line = "";
		for(String element: lineOutput){
			line += element + "\t"; 
		}
		Writer.appendLineToFile(line, Properties.OUTPUT_PATH + "/annotation_output.txt");
	
	}
	
	private static void writeStats(String file, List<String> relevantStats) {
		
		List<String> lineOutput = extractInformationFromFilename(file);
		lineOutput.addAll(relevantStats);
		
		String line = "";
		for(String element: lineOutput){
			line += element + "\t"; 
		}
		
		Writer.appendLineToFile(line, Properties.OUTPUT_PATH + Properties.CORPUS_STATS);
	
	}


	private static List<String> extractInformationFromFilename(String file) {
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
		return lineOutput;
	}
	
	
	
	
	public static void main(String[] args) {
		AnnotationProcessor.createInstancesAndOutput();
	}



}
