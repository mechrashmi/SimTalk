import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class SimTalkFileReader {
	private String path ;
	int totalCharacters;
	int totalUniqueStrings;
	Map<Integer, String> indexToStrings = new HashMap<Integer, String>();
	Map<String, Integer> stringToIndex = new HashMap<String, Integer>();
	Map<String, HashMap<String, Double>> bigrams = new HashMap<String, HashMap<String, Double>>();
	Map<Character, Double> unigram = new HashMap<Character, Double>();
	Map<String, Double> occurences = new HashMap<String, Double>();
	Model model;
	public SimTalkFileReader(String path) {
		this.path = path;
	}
	
	public void read(){
		String firstElement;
		String prevLineElement = null;
		int index = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line;
			while((line = br.readLine()) != null ){
				//char[] stringArray = line.toLowerCase().toCharArray();
				//String[] stringArray = line.toLowerCase().toString().split(" ");
				ArrayList<String> stringArray = new ArrayList<String>(Arrays.asList(line.toLowerCase().toString().split(" ")));
				stringArray.add("\n");
				//System.out.println(stringArray);
				if(stringArray.size() == 0) continue;
				if(null == prevLineElement)
					firstElement = stringArray.get(0);
				else
					firstElement = prevLineElement;
				if(!bigrams.containsKey(firstElement)){
					bigrams.put(firstElement, new HashMap<String, Double>());
					indexToStrings.put(index, firstElement);
					stringToIndex.put(firstElement, index);
					index++;
					occurences.put(firstElement, 1.0);
					totalUniqueStrings++;
				}
				else{
					occurences.put(firstElement, occurences.get(firstElement) + 1);
				}

				totalCharacters += stringArray.size();
				
				for(int i = 1; i < stringArray.size(); i++){
					String secondElement = stringArray.get(i);
					HashMap<String, Double> bigram = bigrams.get(firstElement);
					
					if(!bigram.containsKey(secondElement)) 
						bigram.put(secondElement, 1.0);
					else
						bigram.put(secondElement, bigram.get(secondElement) + 1);

					bigrams.put(firstElement, bigram);
					if(!bigrams.containsKey(secondElement)){
						bigrams.put(secondElement, new HashMap<String, Double>());
						occurences.put(secondElement, 1.0);
						indexToStrings.put(index, secondElement);
						stringToIndex.put(secondElement, index);
						index++;
						totalUniqueStrings++;
					}
					else{
						occurences.put(secondElement, occurences.get(secondElement) + 1);
					}
					firstElement = secondElement;
					prevLineElement = secondElement;
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		System.out.println("totalUniqueStrings: " + totalUniqueStrings);
		Iterator<Entry<String, HashMap<String, Double>>> it = bigrams.entrySet().iterator();
		
		/*Populate data into Model*/
		
		model = new Model(totalUniqueStrings,totalCharacters, indexToStrings, stringToIndex);
		
		while(it.hasNext()){
			Map.Entry bigramPair = (Map.Entry) it.next();
			String f = (String) bigramPair.getKey();
			Map<String, Double> frequenciesGivenKey = (Map) bigramPair.getValue();
			Double totalOccurences = 0.0;
			Iterator adjacentElementIterator = frequenciesGivenKey.entrySet().iterator();
			double[] bigramFrequencies = new double[totalUniqueStrings];
			while(adjacentElementIterator.hasNext()){
				Map.Entry newPair = (Map.Entry) adjacentElementIterator.next();
				Double frequency =round((Double)newPair.getValue() / occurences.get(f), 2);
				Integer i = stringToIndex.get(newPair.getKey());
				bigramFrequencies[i] = frequency;
			}
			
			model.bigram.put(stringToIndex.get(f), bigramFrequencies);
			model.unigram[stringToIndex.get(f)] = round(occurences.get(f) / totalCharacters, 2);
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

