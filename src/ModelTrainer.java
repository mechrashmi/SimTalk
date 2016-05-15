import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class ModelTrainer {

	public static void main(String[] args) {
		
		SimTalkFileReader fileReader = new SimTalkFileReader("D:/ML/temp1.txt");
		fileReader.read();
		Model model = fileReader.model;
		
		/*Iterator<Entry<Integer, double[]>> bigramIterator = model.bigram.entrySet().iterator();
		while(bigramIterator.hasNext()){
			Map.Entry<Integer, double[]> bigramPair = bigramIterator.next();
			double[] frequenciesGivenKey = bigramPair.getValue();
			System.out.print(model.indexToCharacters.get(bigramPair.getKey()) + " --->");
			for(int i = 0; i < model.totalCharacters; i++){
				if(frequenciesGivenKey[i] > 0){
					System.out.print ( model.indexToCharacters.get(i) + "=" + frequenciesGivenKey[i] + " " );
				}
			}
			
			System.out.println("\n");
		}*/
		model.generateUnigramPrefixArray();
		model.generateBigramPrefixArray();
		char characterToPrint = ' ';
		double random = Math.random();
		for(int i = 0; i < model.totalCharacters; i ++){
			if(random <= model.unigramPrefixArray[i]){
				characterToPrint = model.indexToCharacters.get(i);
				System.out.print(characterToPrint);
				break;
			}
		}
		
		for(int i = 1; i < model.totalCharacters; i++){
			random =  Math.random();
			double[] frequenciesGivenKey = model.bigramPrefixArray.get(model.charactersToIndex.get(characterToPrint));
			//System.out.println(random);
			//System.out.println(model.charactersToIndex.get(characterToPrint) + " " + characterToPrint);
			for(int j = 0; j < model.totalCharacters; j++){
				if(random <= frequenciesGivenKey[j]){
					characterToPrint = model.indexToCharacters.get(j);
					System.out.print(characterToPrint);
					break;
				}
			}
			
			
			
		}
		
		
		
		
		
		
	}
			
	

}
