package processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Contains methods that write (in) files.
 * 
 * @author Vera Boteva, Demian Gholipour
 *
 */

public class Writer {
	
	/**
	 * Appends a line to a file.
	 */
	
	public static void appendLineToFile(String text, String filename) {
		File f = new File(filename);
		String s = "";
		if (f.exists()) {
			s += "\r\n";
		}
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), "UTF-8"))) {	
		    bw.write(s + text);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Writes an empty file.
	 */
	
	public static void writeEmptyFile(String filename) {
		File f = new File(filename);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write string in file, overwrite if file exists. 
	 */
	
	public static void overwriteFile(String content, String outputfile){
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile), "UTF-8"))){
			bw.write(content);
			bw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		
		
	}
}
