package processing;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorpusReader {
	
	private static final String FRE_PATH = "TOEFL/FRE/";
	private static final String GER_PATH = "TOEFL/GER/";
	


	/**
	 * Returns complete content of a file as a string.
	 */
	
	public static String readContentOfFile(String filename){
		String content = new String();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
			
			while (br.ready()) {
				String line = br.readLine();
				content += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return content;	
	}
	
	public static List<String> readFileAsWordsList(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
			String line = new String();
			while (br.ready()) {
				line = br.readLine();
				String[] lineAsList = line.split(" ");
				lines.addAll(Arrays.asList(lineAsList));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	/**
	 * Reads a file line-wise and returns a list of the lines.
	 */
	
	public static ArrayList<String> readLinesList(String filename) {
		ArrayList<String> lines = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
			String line = new String();
			while (br.ready()) {
				line = br.readLine();
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static void main(String[] args) {
		System.out.println(CorpusReader.readFileAsWordsList(GER_PATH + "test_P1_GER_high_10392.txt"));

	}

	public static String getFrePath() {
		return FRE_PATH;
	}
	
	public static String getGerPath() {
		return GER_PATH;
	}

}
