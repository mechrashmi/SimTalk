
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Two things I need to change:
 * a) need to do binary search
 * b) extend it to words
 */
public class ModelTrainer {

	public static void main(String[] args) {
		
		SimTalkFileReader fileReader = new SimTalkFileReader("D:/ML/bigtext.txt");
		fileReader.read();
		Model model = fileReader.model;
		
		/*Iterator<Entry<Integer, double[]>> bigramIterator = model.bigram.entrySet().iterator();
		while(bigramIterator.hasNext()){
			Map.Entry<Integer, double[]> bigramPair = bigramIterator.next();
			double[] frequenciesGivenKey = bigramPair.getValue();
			System.out.print(model.indexToStrings.get(bigramPair.getKey()) + " --->");
			for(int i = 0; i < model.totalUniqueStrings; i++){
				if(frequenciesGivenKey[i] > 0){
					System.out.print ( model.indexToStrings.get(i) + "=" + frequenciesGivenKey[i] + " " );
				}
			}
			
			System.out.println("\n");
		}*/
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("D:/ML/output.txt");

			PrintStream ps = new PrintStream(fos);

			model.generateUnigramPrefixArray();
			model.generateBigramPrefixArray();
			String characterToPrint = "";
			double random = Math.random();
			System.out.println("Printing");
			System.setOut(ps);
			for(int i = 0; i < model.totalUniqueCharacters; i ++){
				if(random <= model.unigramPrefixArray[i]){
					characterToPrint = model.indexToCharacters.get(i);
					System.out.print(characterToPrint);
					break;
				}
			}

			for(int i = 1; i < model.totalCharacters; i++){
				random =  Math.random();
				double[] frequenciesGivenKey = model.bigramPrefixArray.get(model.charactersToIndex.get(characterToPrint));
		//		System.out.println(random);
				//System.out.println(model.st	ringToIndex.get(characterToPrint) + " " + characterToPrint);
				for(int j = 0; j < model.totalUniqueCharacters; j++){
					if(random <= frequenciesGivenKey[j]){
						characterToPrint = model.indexToCharacters.get(j);
						System.out.print(characterToPrint + " ");
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("\n" + model.totalUniqueCharacters);

		//System.exit(0);
		
		
		
		
		
		
	}
			
	

}
