package processing;

import java.io.File;


public class Editor {
	
	/**
	 * Creates the folders needed to create and structure the OUTPUT
	 */
	public static void createOutputFolder(){
		
		File output = new File(Properties.OUTPUT_PATH);
		File annotation_output = new File(Properties.OUTPUT_PATH + Properties.outputFile);
		output.mkdirs();
		
	}
	
	public static void deleteFile(String filename) {
		File f = new File(filename);
		f.delete();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
