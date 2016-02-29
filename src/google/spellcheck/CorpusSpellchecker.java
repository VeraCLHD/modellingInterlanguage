package google.spellcheck;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;

public class CorpusSpellchecker {
	private SpellChecker checker = new SpellChecker();
	
	
	public void testMethod(){
		System.out.println(this.getChecker().check("helloo world"));
	}
	
	
	public static void main(String[] args) {
		CorpusSpellchecker  corpusCheck = new CorpusSpellchecker();
		corpusCheck.testMethod();

	}


	public SpellChecker getChecker() {
		return checker;
	}


	public void setChecker(SpellChecker checker) {
		this.checker = checker;
	}

}
