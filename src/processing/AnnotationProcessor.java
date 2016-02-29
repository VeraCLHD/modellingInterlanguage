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
	
	public static ArrayList<String> extractAnnotationsList(String filename) {
		ArrayList<String> annotations = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
			String line = new String();
			while (br.ready()) {
				line = br.readLine();
				String[] wordsInLine = line.split(" ");
				for (String word: wordsInLine){
					if(word.contains("_")){
						annotations.add(word);
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return annotations;
	}
	
	
	public static List<String> processAnnotation(String raw_annotation){
		List<String> result = new ArrayList<String>();
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
		
		if(annotatorSuggestion.contains("[")){
			annotatorSuggestion = annotatorSuggestion.replace("[", "");
			annotatorSuggestion = annotatorSuggestion.replace("]", "");
			
		}
		
		result.add(originalWord);
		result.add(annotatorSuggestion);

		return result;
		
	}
	
	public static void writeOutputToFile(){
		List<String> filesInFolders = FileFilter_Toefl.findToeflFiles(new File(ANNOTATED_PATH));
		// id, L1, proficiency,prompt, test/train_dev, word, annotated, first_suggestion
		
		for(String file: filesInFolders){
			List<String> annotations = AnnotationProcessor.extractAnnotationsList(file);
			
			for(String annotation: annotations){
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
				List<String> misspellingAndSuggestion = AnnotationProcessor.processAnnotation(annotation);
				lineOutput.add(misspellingAndSuggestion.get(0));
				lineOutput.add(misspellingAndSuggestion.get(1));
				
				String word = JazzyCorpusSpellchecker.getSuggestionWithMinDistance(misspellingAndSuggestion.get(0).toLowerCase(), 0);
				
				lineOutput.add(word.toString());
								
				
				String line = "";
				for(String element: lineOutput){
					line += element + "\t"; 
				}
				Writer.appendLineToFile(line, OUTPUT_PATH + "/annotation_output.txt");
			}
		}
		

	}
	
	
	
	
	public static void main(String[] args) {
		AnnotationProcessor.writeOutputToFile();
	}

}
