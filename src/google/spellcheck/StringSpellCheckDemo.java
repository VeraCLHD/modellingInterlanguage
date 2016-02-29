package google.spellcheck;

import com.jspell.domain.*;

/**
 * An example of using JSpell to spellcheck a string of text.
 */
public class StringSpellCheckDemo {

	JSpellDictionaryLocal jdLocal;
	JSpellParser parser;
	JSpellDictionaryManager manager;

	public StringSpellCheckDemo(String directory, String textToCheck) {
		// initialize the dictionary and parser
		manager = JSpellDictionaryManager.getJSpellDictionaryManager();
		manager.setDictionaryDirectory(directory);
		jdLocal = manager.getJSpellDictionaryLocal("enUS"); // specify the language here
		jdLocal.setForceUpperCase(false); // set options on the dictionary here
		jdLocal.setIgnoreUpper(true);
		jdLocal.setIgnoreIrregularCaps(false);
		jdLocal.setIgnoreFirstCaps(false);
		jdLocal.setIgnoreDoubleWords(false);
		jdLocal.setLearnWords(false);

		parser = new JSpellParser(jdLocal, textToCheck); // this is where you specify your text

		// iterate through errors by calling parser.getError() until null. If parser returns non-null value
		// then you will get a JSpellErrorInfo object containing the position, original word and suggestions
		try {
			JSpellErrorInfo error = parser.getError();
			while (error != null) {
				System.out.println("Error at position: " + error.getPosition() + " in word: " + error.getWord());
				System.out.print("      Suggestions: ");
				for (int i = 0; i < error.getSuggestions().length && error.getSuggestions()[i] != null; i++) {
					System.out.print(error.getSuggestions()[i] + " ");
				}
				System.out.println();
				error = parser.getError();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// if no arguments are received on the command line then provide built-in defaults
		if (args.length == 0) {
			new StringSpellCheckDemo("./lexicons", "thsi isa sample");
		} else {
			new StringSpellCheckDemo(args[0], args[1]);
		}
	}
}