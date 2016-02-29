package processing;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A file filter for TOEFL11 Data. Implementation of FileFilter.
 * 
 * @author Vera Boteva
 */
public class FileFilter_Toefl implements FileFilter {


	@Override
	// Filter accepts only auto_conll
	/**
     * Accepts text files and returns false if the file is not in this format
     * 
     * @param inputFile file the file that is being checked/filtered
     */
	public boolean accept(File inputFile) {
		if (inputFile.getName().endsWith("_done.txt")){
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Recursively searches through a directory to find annotated essay-files. If the file is a directory, the method is invoked again.
	 * If it is a file, the absolute file is stored in a List.
	 * 
	 * @param path file as the path where the essay-files are supposed to be found
	 * @return a list of all annotated essay files in a directory
	 */
	public static List<String> findToeflFiles(File path){
		List<String> filesInFolders = new ArrayList<String>();
		File folder = new File(path.toString());
		File[] listOfFiles = folder.listFiles();
		FileFilter_Toefl ffe = new FileFilter_Toefl();

		for (File file: listOfFiles) {
			
			if (file.isFile() && ffe.accept(file)) {
				filesInFolders.add(file.getAbsolutePath());
			}
			
			else if(file.isDirectory()){
				filesInFolders.addAll(findToeflFiles(file));
			}
		}
		
		return filesInFolders;
	}

}
