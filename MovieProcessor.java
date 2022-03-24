/**
 * This file takes in as input two folders which contain negative and positive movie reviews and processes
 * the files and then builds a Bayesian model based off of the input files found in the folders, it outputs
 * the top 5 results for the negative and positive files
 * @author Kevin
 *
 */

import java.util.*;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The following program will input a set of files containing movie reviews and will create a Bayesian classifier
 * English Stop Words Provided Courtesy of NLTK from GitHub
 * Creates an output file to export to
 * 
 * @author Kevin Lo
 *
 */
public class MovieProcessor {
	StringTokenizer tokens;
	double numReviews = 0;
	ArrayList<String> cleanedTokens = new ArrayList<String>();
	List<String> NLTK_STOP_WORDS = Arrays.asList("0o", "0s", "3a", "3b", "3d", "6b", "6o", "a", "a1", "a2", "a3", "a4", "ab", "able", "about", "above", "abst", "ac", "accordance", "according", "accordingly", "across", "act", "actually", "ad", "added", "adj", "ae", "af", "affected", "affecting", "affects", "after", "afterwards", "ag", "again", "against", "ah", "ain", "ain't", "aj", "al", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "ao", "ap", "apart", "apparently", "appear", "appreciate", "appropriate", "approximately", "ar", "are", "aren", "arent", "aren't", "arise", "around", "as", "a's", "aside", "ask", "asking", "associated", "at", "au", "auth", "av", "available", "aw", "away", "awfully", "ax", "ay", "az", "b", "b1", "b2", "b3", "ba", "back", "bc", "bd", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "bi", "bill", "biol", "bj", "bk", "bl", "bn", "both", "bottom", "bp", "br", "brief", "briefly", "bs", "bt", "bu", "but", "bx", "by", "c", "c1", "c2", "c3", "ca", "call", "came", "can", "cannot", "cant", "can't", "cause", "causes", "cc", "cd", "ce", "certain", "certainly", "cf", "cg", "ch", "changes", "ci", "cit", "cj", "cl", "clearly", "cm", "c'mon", "cn", "co", "com", "come", "comes", "con", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldn", "couldnt", "couldn't", "course", "cp", "cq", "cr", "cry", "cs", "c's", "ct", "cu", "currently", "cv", "cx", "cy", "cz", "d", "d2", "da", "date", "dc", "dd", "de", "definitely", "describe", "described", "despite", "detail", "df", "di", "did", "didn", "didn't", "different", "dj", "dk", "dl", "do", "does", "doesn", "doesn't", "doing", "don", "done", "don't", "down", "downwards", "dp", "dr", "ds", "dt", "du", "due", "during", "dx", "dy", "e", "e2", "e3", "ea", "each", "ec", "ed", "edu", "ee", "ef", "effect", "eg", "ei", "eight", "eighty", "either", "ej", "el", "eleven", "else", "elsewhere", "em", "empty", "en", "end", "ending", "enough", "entirely", "eo", "ep", "eq", "er", "es", "especially", "est", "et", "et-al", "etc", "eu", "ev", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "ey", "f", "f2", "fa", "far", "fc", "few", "ff", "fi", "fifteen", "fifth", "fify", "fill", "find", "fire", "first", "five", "fix", "fj", "fl", "fn", "fo", "followed", "following", "follows", "for", "former", "formerly", "forth", "forty", "found", "four", "fr", "from", "front", "fs", "ft", "fu", "full", "further", "furthermore", "fy", "g", "ga", "gave", "ge", "get", "gets", "getting", "gi", "give", "given", "gives", "giving", "gj", "gl", "go", "goes", "going", "gone", "got", "gotten", "gr", "greetings", "gs", "gy", "h", "h2", "h3", "had", "hadn", "hadn't", "happens", "hardly", "has", "hasn", "hasnt", "hasn't", "have", "haven", "haven't", "having", "he", "hed", "he'd", "he'll", "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "heres", "here's", "hereupon", "hers", "herself", "hes", "he's", "hh", "hi", "hid", "him", "himself", "his", "hither", "hj", "ho", "home", "hopefully", "how", "howbeit", "however", "how's", "hr", "hs", "http", "hu", "hundred", "hy", "i", "i2", "i3", "i4", "i6", "i7", "i8", "ia", "ib", "ibid", "ic", "id", "i'd", "ie", "if", "ig", "ignored", "ih", "ii", "ij", "il", "i'll", "im", "i'm", "immediate", "immediately", "importance", "important", "in", "inasmuch", "inc", "indeed", "index", "indicate", "indicated", "indicates", "information", "inner", "insofar", "instead", "interest", "into", "invention", "inward", "io", "ip", "iq", "ir", "is", "isn", "isn't", "it", "itd", "it'd", "it'll", "its", "it's", "itself", "iv", "i've", "ix", "iy", "iz", "j", "jj", "jr", "js", "jt", "ju", "just", "k", "ke", "keep", "keeps", "kept", "kg", "kj", "km", "know", "known", "knows", "ko", "l", "l2", "la", "largely", "last", "lately", "later", "latter", "latterly", "lb", "lc", "le", "least", "les", "less", "lest", "let", "lets", "let's", "lf", "like", "liked", "likely", "line", "little", "lj", "ll", "ll", "ln", "lo", "look", "looking", "looks", "los", "lr", "ls", "lt", "ltd", "m", "m2", "ma", "made", "mainly", "make", "makes", "many", "may", "maybe", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "mightn", "mightn't", "mill", "million", "mine", "miss", "ml", "mn", "mo", "more", "moreover", "most", "mostly", "move", "mr", "mrs", "ms", "mt", "mu", "much", "mug", "must", "mustn", "mustn't", "my", "myself", "n", "n2", "na", "name", "namely", "nay", "nc", "nd", "ne", "near", "nearly", "necessarily", "necessary", "need", "needn", "needn't", "needs", "neither", "never", "nevertheless", "new", "next", "ng", "ni", "nine", "ninety", "nj", "nl", "nn", "no", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", "noted", "nothing", "novel", "now", "nowhere", "nr", "ns", "nt", "ny", "o", "oa", "ob", "obtain", "obtained", "obviously", "oc", "od", "of", "off", "often", "og", "oh", "oi", "oj", "ok", "okay", "ol", "old", "om", "omitted", "on", "once", "one", "ones", "only", "onto", "oo", "op", "oq", "or", "ord", "os", "ot", "other", "others", "otherwise", "ou", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "ow", "owing", "own", "ox", "oz", "p", "p1", "p2", "p3", "page", "pagecount", "pages", "par", "part", "particular", "particularly", "pas", "past", "pc", "pd", "pe", "per", "perhaps", "pf", "ph", "pi", "pj", "pk", "pl", "placed", "please", "plus", "pm", "pn", "po", "poorly", "possible", "possibly", "potentially", "pp", "pq", "pr", "predominantly", "present", "presumably", "previously", "primarily", "probably", "promptly", "proud", "provides", "ps", "pt", "pu", "put", "py", "q", "qj", "qu", "que", "quickly", "quite", "qv", "r", "r2", "ra", "ran", "rather", "rc", "rd", "re", "readily", "really", "reasonably", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", "relatively", "research", "research-articl", "respectively", "resulted", "resulting", "results", "rf", "rh", "ri", "right", "rj", "rl", "rm", "rn", "ro", "rq", "rr", "rs", "rt", "ru", "run", "rv", "ry", "s", "s2", "sa", "said", "same", "saw", "say", "saying", "says", "sc", "sd", "se", "sec", "second", "secondly", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "sf", "shall", "shan", "shan't", "she", "shed", "she'd", "she'll", "shes", "she's", "should", "shouldn", "shouldn't", "should've", "show", "showed", "shown", "showns", "shows", "si", "side", "significant", "significantly", "similar", "similarly", "since", "sincere", "six", "sixty", "sj", "sl", "slightly", "sm", "sn", "so", "some", "somebody", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "sp", "specifically", "specified", "specify", "specifying", "sq", "sr", "ss", "st", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure", "sy", "system", "sz", "t", "t1", "t2", "t3", "take", "taken", "taking", "tb", "tc", "td", "te", "tell", "ten", "tends", "tf", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that's", "that've", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", "therefore", "therein", "there'll", "thereof", "therere", "theres", "there's", "thereto", "thereupon", "there've", "these", "they", "theyd", "they'd", "they'll", "theyre", "they're", "they've", "thickv", "thin", "think", "third", "this", "thorough", "thoroughly", "those", "thou", "though", "thoughh", "thousand", "three", "throug", "through", "throughout", "thru", "thus", "ti", "til", "tip", "tj", "tl", "tm", "tn", "to", "together", "too", "took", "top", "toward", "towards", "tp", "tq", "tr", "tried", "tries", "truly", "try", "trying", "ts", "t's", "tt", "tv", "twelve", "twenty", "twice", "two", "tx", "u", "u201d", "ue", "ui", "uj", "uk", "um", "un", "under", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "uo", "up", "upon", "ups", "ur", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "ut", "v", "va", "value", "various", "vd", "ve", "ve", "very", "via", "viz", "vj", "vo", "vol", "vols", "volumtype", "vq", "vs", "vt", "vu", "w", "wa", "want", "wants", "was", "wasn", "wasnt", "wasn't", "way", "we", "wed", "we'd", "welcome", "well", "we'll", "well-b", "went", "were", "we're", "weren", "werent", "weren't", "we've", "what", "whatever", "what'll", "whats", "what's", "when", "whence", "whenever", "when's", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "where's", "whereupon", "wherever", "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", "who'll", "whom", "whomever", "whos", "who's", "whose", "why", "why's", "wi", "widely", "will", "willing", "wish", "with", "within", "without", "wo", "won", "wonder", "wont", "won't", "words", "world", "would", "wouldn", "wouldnt", "wouldn't", "www", "x", "x1", "x2", "x3", "xf", "xi", "xj", "xk", "xl", "xn", "xo", "xs", "xt", "xv", "xx", "y", "y2", "yes", "yet", "yj", "yl", "you", "youd", "you'd", "you'll", "your", "youre", "you're", "yours", "yourself", "yourselves", "you've", "yr", "ys", "yt", "z", "zero", "zi", "zz");
	Stream<String> stream;
	Map<String, Long> vocab;
	Map<String, Long> masterVocab =  new HashMap<String, Long>();
	Map<String, Double> sortedBayesianModel;
	
	
	/**
	 * Takes in a review file as input and returns the raw text
	 * @param fileName
	 * @return
	 */
	public String readFile(String fileName) throws IOException {
		String fileText = "";
		try {
			fileText = new String(Files.readAllBytes(Paths.get(fileName)));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return fileText;
		
	}
	
	/**
	 * To string function for directory listing (For testing)
	 * @param inputFolder
	 */
	public void printDirectory(String[] inputFolder) {
		for(int i = 0; i < inputFolder.length; i ++) {
			System.out.println(inputFolder[i]);
		}
		
	}
	
	
	
	/**
	 * Lists the folder's contents
	 * @param inputDirectory
	 * @return String array containing the contents of 
	 */
	public String[] listDirectory(String inputDirectory) {
		File directory = new File(inputDirectory);
		String[] listOfFiles = directory.list();
		return listOfFiles;
	}
	
	/**
	 * Processes the actual files
	 * @param args
	 */
	public void loadFolder(String inputPath) {
		String path;
		String[] listOfFiles = listDirectory(inputPath);
		for(int i = 0; i < listOfFiles.length; i ++) {
			try {
				path = inputPath + '/' + listOfFiles[i];
				readFile(path);
				System.out.println("Loaded " + listOfFiles[i]);
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * Takes in an input file's contents (from readFile) and cleans it of punctuation
	 * @param filePath the file's path
	 */
	public void cleanFile(String fileContent) {
		cleanedTokens.clear(); //Clears the previous cleanedTokens array to make space for new one
		tokens = new StringTokenizer(fileContent);
		//System.out.print("Pre-cleaned token number: ");
		//System.out.println(tokens.countTokens()); 
		while(tokens.hasMoreTokens()) {
			String tempToken = tokens.nextToken();
			//Will remove tokens with length of 1, removes punctuation from words and removes common stop words pulled from database
			if(!tempToken.contains("\\p{Punct}") && tempToken.length() > 1 && !NLTK_STOP_WORDS.contains(tempToken)) {
				tempToken = tempToken.replaceAll("\\p{Punct}", ""); //Removes punctuation
				if(tempToken.length() > 1 && !tempToken.contains("\\d+")) //Further cleaning to remove blank spaces and numbers
					cleanedTokens.add(tempToken);
				}
			}
		//System.out.print("Post-cleaned token number: ");
		//System.out.println(cleanedTokens.size());
	}
	
	/**
	 * Inputs a file, loads it, and cleans it, and adds it to a "Counter" of Vocabulary
	 * @param filePath
	 */
	public void addFileTokenToVocabulary(String filePath) {
		String fileContent;
		try {
			fileContent = readFile(filePath);
			cleanFile(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] tempArr = new String[cleanedTokens.size()];
		for(int i = 0; i < cleanedTokens.size(); i++) {
			tempArr[i] = cleanedTokens.get(i);
		}
		stream = Stream.of(tempArr).parallel();
		vocab = stream.collect(Collectors.groupingBy(String::toString, Collectors.counting()));
		update(vocab); //Updates the masterVocabulary HashMap
	}
	
	/**
	 * Takes a folder path as input and adds all of the folder's files to the vocabulary
	 * @param folderPath
	 */
	public void addFolderTokensToVocabulary(String folderPath) {
		String path;
		String[] listOfFiles = listDirectory(folderPath);
		numReviews = listOfFiles.length;
		for(int i = 0; i < listOfFiles.length; i ++) {
				path = folderPath + '/' + listOfFiles[i];
				addFileTokenToVocabulary(path);
		}
		sortByValue(masterVocab);
		
		
	}
	
	/**
	 * Updates the MasterVocab with individual file vocabs to get a comprehensive vocabulary list
	 */
	public void update(Map<String, Long> inputVocab) {
		for(String word : inputVocab.keySet()) {
			masterVocab.merge(word, inputVocab.get(word), (previousFrequency, newFrequency) -> previousFrequency + newFrequency);
		}
		
	}
	
	/**
	 * Sorts the HashMap of the masterVocab
	 * @param masterVocab a Map of <String, Long> 
	 * @return the sorted HashMap
	 */
	public void sortByValue(Map<String, Long> inputMasterVocab)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Long> > list =
               new LinkedList<Map.Entry<String, Long> >(inputMasterVocab.entrySet());
 
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Long> >() {
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return (o1.getValue()).compareTo(o2.getValue()) * -1; //-1 multiplier sorts it in descending order
            }
        });
         
        // put data from sorted list to hashmap
        Map<String, Long> temp = new LinkedHashMap<String, Long>();
        for (Map.Entry<String, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        this.masterVocab = temp;
    }
	
	/**
	 * Sorts the Bayesian Model in descending order
	 */
	public void sortByProbability() {
		// Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
               new LinkedList<Map.Entry<String, Double> >(sortedBayesianModel.entrySet());
 
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue()) * -1; //-1 multiplier sorts it in descending order
            }
        });
         
        // put data from sorted list to hashmap
        Map<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        this.sortedBayesianModel = temp;
		
		
		
	}
	
	/**
	 * Prints out the top inputed amount of entries in the vocabulary
	 * @param limit the number of entries to print
	 */
	public void printTopNumberVocabulary(int limit) {
		Iterator<Entry<String, Long>> iterator = masterVocab.entrySet().iterator();
		for(int i = 0; i < limit; i ++) {
			Map.Entry<String, Long> masterVocabObj = (Map.Entry<String, Long>) iterator.next();
			System.out.println(masterVocabObj.getKey() + " = " + masterVocabObj.getValue());
		}
	}
	
	/**
	 * Prints out the top inputed amount of entries in the model
	 * @param limit
	 */
	public void printTopNumberModel(int limit) {
		Iterator<Entry<String, Double>> iterator = sortedBayesianModel.entrySet().iterator();
		for(int i = 0; i < limit; i ++) {
			Map.Entry<String, Double> sortedBayesianModelObj = (Map.Entry<String, Double>) iterator.next();
			System.out.println(sortedBayesianModelObj.getKey() + " = " + sortedBayesianModelObj.getValue());
		}
		
	}
	
	
	
	
	/**
	 * Takes in as input a Map representing the total counter for BOTH positive and negative review data sets and returns a Bayesian model
	 * in the format of a Map
	 * @input totalVocab a Map representing the totalVocabulary of both pos and neg review datasets
	 */
	public Map<String, Double> generateModel(Map<String, Long> inputTotalVocab, Map<String, Long> currentVocab) {
		Map<String, Long> totalVocab = inputTotalVocab;
		Map<String, Double> bayesianModel = new HashMap<String, Double>();
		double probability;
		Long wordFrequency;
		Long totalWordFrequency;
		//Calculating the probability of this movieProcessor's masterVocab
		for(String word: currentVocab.keySet()) {
			wordFrequency = currentVocab.get(word);
			totalWordFrequency = totalVocab.get(word);
			
			probability = ((wordFrequency/numReviews) * 0.5) / totalWordFrequency;
			
			bayesianModel.put(word, probability);
		}
		sortedBayesianModel = bayesianModel;
		return bayesianModel;
	}
	
	/**
	 * Simply returns masterVocab
	 * @return masterVocab
	 */
	public Map<String, Long> getMasterVocab() {
		return masterVocab;
	}
	
	
	/**
	 * Exports the BaysianModel in a readable format as a String
	 * @return BaysianModel in a readable format as a String
	 */
	public String exportModel() {
		String bayesianModel = "";
		
		for(String word: sortedBayesianModel.keySet()) {
			bayesianModel += word + " = " + sortedBayesianModel.get(word) + "\n";
			
		}
		return bayesianModel;
		
	}
	
	/**
	 * Exports the top 5 words as a String Array
	 * @return a String array
	 */
	public String[] topFiveWords() {
		String[] topFive = new String[5];
		Iterator<Entry<String, Double>> iterator = sortedBayesianModel.entrySet().iterator();
		for(int i = 0; i < 5; i ++) {
			Map.Entry<String, Double> sortedBayesianModelObj = (Map.Entry<String, Double>) iterator.next();
			topFive[i] = sortedBayesianModelObj.getKey();
		}
		return topFive;
		
	}
	
	
	

	
	
	/**
	 * Takes in user input of directories to positive and negative movie reviews respectively and constructs a Bayesian model out of it
	 * @param args
	 */
	public static void main (String args []) {
		MovieProcessor positiveMovieReviewProcessor = new MovieProcessor(); //object that will handle positive movie review data
		MovieProcessor negativeMovieReviewProcessor = new MovieProcessor(); //object that will handle negative movie review data
		
		Map<String, Long> ultimateMasterVocab = new HashMap<String, Long>();
		Map<String, Long> negativeMasterVocabClone = new HashMap<String, Long>();
		Map<String, Long> positiveMasterVocab = new HashMap<String, Long>();
		Map<String, Long> negativeMasterVocab;
		
		//Getting user input for folder paths of positive and negative reviews
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the path for the positive reviews: ");
		String positivePath = scanner.nextLine();
		System.out.println("Enter the path for the negative reviews: ");
		String negativePath = scanner.nextLine();
		scanner.close();
		
		//Populating masterVocabulary for both positive and negative movie reviews
		positiveMovieReviewProcessor.addFolderTokensToVocabulary(positivePath);
		negativeMovieReviewProcessor.addFolderTokensToVocabulary(negativePath);
		
		//Deep cloning positive to ultimateMasterVocab HashMap
		for(String word: positiveMovieReviewProcessor.getMasterVocab().keySet()) {
			ultimateMasterVocab.put(word, positiveMovieReviewProcessor.getMasterVocab().get(word));
		}
		
		//Deep cloning negative to negativeMasterVocab HashMap
		for(String word: negativeMovieReviewProcessor.getMasterVocab().keySet()) {
			negativeMasterVocabClone.put(word, negativeMovieReviewProcessor.getMasterVocab().get(word));
		}
		
		
		positiveMasterVocab = positiveMovieReviewProcessor.getMasterVocab();
		negativeMasterVocab = negativeMovieReviewProcessor.getMasterVocab();
		
		//Merging both positive and negative vocabularies into one masterVocabulary
		for(String word : negativeMasterVocabClone.keySet()) {
			ultimateMasterVocab.merge(word, negativeMasterVocabClone.get(word), (previousFrequency, newFrequency) -> previousFrequency + newFrequency);
		}
		
		List<Map.Entry<String, Long> > list =
	               new LinkedList<Map.Entry<String, Long> >(ultimateMasterVocab.entrySet());
	 
	        // Sort the list
	        Collections.sort(list, new Comparator<Map.Entry<String, Long> >() {
	            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
	                return (o1.getValue()).compareTo(o2.getValue()) * -1; //-1 multiplier sorts it in descending order
	            }
	        });
	         
	        // put data from sorted list to hashmap
	        Map<String, Long> temp = new LinkedHashMap<String, Long>();
	        for (Map.Entry<String, Long> aa : list) {
	            temp.put(aa.getKey(), aa.getValue());
	        }
	        ultimateMasterVocab = temp;
		
		//Generating Bayesian Models using ultimate master vocabulary
		positiveMovieReviewProcessor.generateModel(ultimateMasterVocab, positiveMasterVocab);
		negativeMovieReviewProcessor.generateModel(ultimateMasterVocab, negativeMasterVocab);
		
		//Sorting respective models in descending order
		positiveMovieReviewProcessor.sortByProbability();
		negativeMovieReviewProcessor.sortByProbability();
		
		//Outputting each respective Bayesian Model to a file
		try {
			File outputFile = new File("BayesianModel.txt");
			if(outputFile.createNewFile()) {
				System.out.println("File created: " + outputFile.getAbsolutePath());
			}
			else
			{
				System.out.println("File already exists.");
			}
			
			
			FileWriter outputWriter = new FileWriter("BayesianModel.txt");
			outputWriter.write("Positive Review Probabilities: \n");
			outputWriter.write(positiveMovieReviewProcessor.exportModel());
			outputWriter.write("Negative Review Probabilities: \n");
			outputWriter.write(negativeMovieReviewProcessor.exportModel());
			outputWriter.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//Then, output the top 5 options from each positive and negative in the format
		//P1: Word:Amazing, P2: Word: Good, .... N1: Word:Disappointing, N2: Word:Forgettable,...
		String [] positiveTop = positiveMovieReviewProcessor.topFiveWords();
		String [] negativeTop = negativeMovieReviewProcessor.topFiveWords();
		
		for(int i = 0; i < 5; i ++) {
			if(i == 4) {
				System.out.print("P" + (i + 1) + ": " + "Word:" + positiveTop[i] + " ");
			}
			else {
				System.out.print("P" + (i + 1) + ": " + "Word:" + positiveTop[i] + ",");
			}
		}
		for(int i = 0; i < 5; i ++) {
			if(i == 4) {
				System.out.print("N" + (i + 1) + ": " + "Word:" + negativeTop[i]);
			}
			else {
				System.out.print("N" + (i + 1) + ": " + "Word:" + negativeTop[i] + ",");
			}
		}
		
		
		
		
			

	
}
	
	

}
