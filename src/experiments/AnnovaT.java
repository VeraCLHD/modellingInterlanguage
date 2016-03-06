package experiments;

import org.apache.commons.math3.stat.inference.OneWayAnova;

import processing.Properties;
import flanagan.analysis.ANOVA;

public class AnnovaT {
	
	public static void main(String[] args) {
		OneWayAnova annova = new OneWayAnova();
		//annova.readResponseData();//.readResponseData(Properties.OUTPUT_PATH + Properties.outputFile);
	}

}
