import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SimTalkFileReader {
	private String path ;
	int totalCharacters;
	Map<Integer, Character> indexToCharacters = new HashMap<Integer, Character>();
	Map<Character, Integer> charactersToIndex = new HashMap<Character, Integer>();
	Map<Character, HashMap<Character, Double>> bigrams = new HashMap<Character, HashMap<Character, Double>>(); 
	Map<Character, Double> unigram = new HashMap<Character, Double>();
	Map<Character, Double> occurences = new HashMap<Character, Double>();
	Model model;
	public SimTalkFileReader(String path) {
		this.path = path;
	}
	
	public void read(){
		Character firstElement;
		int index = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line;
			while((line = br.readLine()) != null ){
				char[] characters = line.toLowerCase().toCharArray();
				if(characters.length == 0) continue;
				firstElement = characters[0];
				bigrams.put(firstElement, new HashMap<Character, Double>());
				indexToCharacters.put(index, firstElement);
				charactersToIndex.put(firstElement, index);
				occurences.put(firstElement, 1.0);
				totalCharacters += line.length();
				
				for(int i = 1; i < characters.length; i++){
					Character secondElement = characters[i];
					HashMap<Character, Double> bigram = bigrams.get(firstElement);
					
					if(!bigram.containsKey(secondElement)) 
						bigram.put(secondElement, 1.0);
					else
						bigram.put(secondElement, bigram.get(secondElement) + 1);

					bigrams.put(firstElement, bigram);
					if(!bigrams.containsKey(secondElement)) bigrams.put(secondElement, new HashMap<Character,Double>());
					firstElement = secondElement;
					if(!occurences.containsKey(secondElement)){ 
						occurences.put(secondElement, 1.0);
						index++;
						indexToCharacters.put(index, secondElement);
						charactersToIndex.put(secondElement, index);
					}
					else
						occurences.put(secondElement, occurences.get(secondElement) + 1);
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

				
		Iterator<Entry<Character, HashMap<Character, Double>>> it = bigrams.entrySet().iterator();
		
		/*Populate data into Model*/
		
		model = new Model(totalCharacters, indexToCharacters, charactersToIndex);
		
		while(it.hasNext()){
			Map.Entry bigramPair = (Map.Entry) it.next();
			Character f = (Character) bigramPair.getKey();
			Map<Character, Double> frequenciesGivenKey = (Map) bigramPair.getValue();
			Double totalOccurences = 0.0;
			Iterator adjacentElementIterator = frequenciesGivenKey.entrySet().iterator();
			double[] bigramFrequencies = new double[totalCharacters];
			while(adjacentElementIterator.hasNext()){
				Map.Entry newPair = (Map.Entry) adjacentElementIterator.next();
				Double frequency =(Double)newPair.getValue() / occurences.get(f);
				Integer i = charactersToIndex.get(newPair.getKey());
				bigramFrequencies[i] = frequency;
			}
			
			model.bigram.put(charactersToIndex.get(f), bigramFrequencies);
			model.unigram[charactersToIndex.get(f)] = occurences.get(f) / totalCharacters;		
		}
		
		
		
		
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

	
	
}

