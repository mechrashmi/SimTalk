import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Model {
	Map<Integer, double[]> bigram = new HashMap<Integer, double[]>();
	double[] unigram;
	int totalUniqueCharacters;
	int totalCharacters;
	Map<Integer, Character> indexToCharacters;
	Map<Character, Integer> charactersToIndex;
	double[] unigramPrefixArray;
	Map<Integer, double[]> bigramPrefixArray = new HashMap<Integer, double[]>();
	
	Model(int totalUniqueCharacters, int totalCharacters, Map<Integer, Character> indexToCharacters, Map<Character, Integer> charactersToIndex){
		this.totalUniqueCharacters = totalUniqueCharacters;
		this.totalCharacters = totalCharacters;
		this.unigram = new double[totalUniqueCharacters];
		this.indexToCharacters = indexToCharacters;
		this.charactersToIndex = charactersToIndex;
		this.unigramPrefixArray = new double[totalUniqueCharacters];
	}
	
	Model(){
		
	}
	
	public void generateBigramPrefixArray(){
		Iterator<Entry<Integer, double[]>> bigramIterator = bigram.entrySet().iterator();
		while(bigramIterator.hasNext()){
			Map.Entry<Integer, double[]> bigramPair = bigramIterator.next();
			double[] frequenciesGivenKey = bigramPair.getValue();
			double[] prefixArrayGivenKey = new double[totalUniqueCharacters];
			prefixArrayGivenKey[0] = frequenciesGivenKey[0];
			for(int i = 1; i < totalUniqueCharacters; i++){
				prefixArrayGivenKey[i] = prefixArrayGivenKey[i - 1] + frequenciesGivenKey[i];
			}
			bigramPrefixArray.put(bigramPair.getKey(), prefixArrayGivenKey);
			
			
		}
		
		//System.out.println(indexToCharacters);
		
		/*bigramIterator = bigramPrefixArray.entrySet().iterator();
		while(bigramIterator.hasNext()){
			Map.Entry<Integer, double[]> bigramPair = bigramIterator.next();
			double[] frequenciesGivenKey = bigramPair.getValue();
			System.out.println (indexToCharacters.get(bigramPair.getKey()) + " -->");
			for(int i = 0; i < totalCharacters; i++){
				System.out.print( " " + frequenciesGivenKey[i]);
			}
			System.out.println("\n");
		}*/
	}
	
	public void generateUnigramPrefixArray(){
		unigramPrefixArray[0] = unigram[0];
		for(int i = 1; i < totalUniqueCharacters; i++){
			unigramPrefixArray[i] = unigramPrefixArray[i - 1] + unigram[i];
		}
	}
	
}
