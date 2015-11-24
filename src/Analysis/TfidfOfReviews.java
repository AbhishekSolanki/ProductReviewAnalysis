/*
THIS CLASS CONTAINS THE LOGIC OF
TFIDF ON COMMENTS, METHODS ARE THE
STAGES OF TFIDF CONVERSION

#allCommentsFrequencyCount
This method calculate the frequency of 
all the words of the comments and TF. returns
object array containing hashmap of term frequency and
count which is passed as argument to tfidfcalulate

#tfidfcalulate
Calculate the IDF and then used the hash map which is 
supplied in parameter containing TF to calulate final
TF*IDF and stored in temporary file TFIDF.tmp with following
structure for 
review_no%%REVIEW%%comment_word%%TERM%%tfidf_val<space>comment_word%%TERM%%tfidf_val....<NewLine>

#cosineSimilarity
Calulates the cosine similarity between the comments using their TF-IDF values

*/

package Analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import dao.FetchData;

public class TfidfOfReviews {

	public static Object[] allCommentsFrequencyCount(){
		//get all comments
		FetchData d = new  FetchData();
		long count = 0;
		HashMap<String, Integer>IDFfreqHashMap = new HashMap<String,Integer>();
		try {
			if(d.getOnlyReviewComment()==true){ //true when temp file containing comments is created
				BufferedReader br = null;
				String sCurrentLine;
				File file = new File("getOnlyReviewComment.tmp");
				FileWriter fileWriter = new FileWriter("TF.tmp");
				br = new BufferedReader(new FileReader(file));
				while ((sCurrentLine = br.readLine()) != null) {
					count++;
					String rawLine[] = sCurrentLine.split("%%COMMENT%%");
					String reviewNo = rawLine[0];
					String lineWords[] = null;
					try{
						lineWords = rawLine[1].split("\\s");
					}catch(Exception e){
						lineWords = new String[]{""};
					}
					//calculateTermFrequency(lineWords);
					ArrayList<String> uniqueWords = new ArrayList<String>();
					HashMap<String, Integer>linefreqHashMap = new HashMap<String,Integer>();
					for(String word:lineWords){
						if(!IDFfreqHashMap.containsKey(word)){ //when new word found
							IDFfreqHashMap.put(word, 1);
							linefreqHashMap.put(word, 1);
							uniqueWords.add(word);
						}else{ // words contains in how many docs. used for calulation of IDF
							if(!uniqueWords.contains(word)){
								IDFfreqHashMap.put(word,IDFfreqHashMap.get(word)+1);
								uniqueWords.add(word);
								linefreqHashMap.put(word, 1); 
							}else{ //counts frequency of words in a sentence used for calulating TF
								linefreqHashMap.put(word, linefreqHashMap.get(word)+1); 
							}
						}
					}

					//System.out.println(linefreqHashMap);
					fileWriter.write(reviewNo+"%%COMMENT%%");
					Iterator<Entry<String, Integer>> it = linefreqHashMap.entrySet().iterator();
					while(it.hasNext()){
						Map.Entry<String , Integer> pair =  it.next();
						int pair_val = pair.getValue();
						int sentence_words_length = lineWords.length;
						double TF= (double) ((double)pair_val/sentence_words_length);
						//						System.out.println(TF);
						fileWriter.write(pair.getKey()+","+TF+"\t");
					}
					fileWriter.write("\n");
				}
				fileWriter.close();
				br.close();
				file.delete(); //delete the temp file as its of no use
			}
			//System.out.println("Line count in TfidfOfReview: "+count+" HashMap size: "+frequencyHashMap.size());
		}catch (Exception e) {
			e.printStackTrace();
		}
		Object obj[] = new Object[]{IDFfreqHashMap,count};
		return obj;

	}

	private static void tfidfcalulate(Object[] obj){
		@SuppressWarnings("unchecked")
		HashMap<String, Integer>IDFfreqHashMap =(HashMap<String, Integer>) obj[0];
		long count = (long) obj[1];
		BufferedReader br;
		String sCurrentLine;
		try{
			File file = new File("TF.tmp");
			FileWriter fileWriter = new FileWriter("TFIDF.tmp");
			br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				String rawLine[] = sCurrentLine.split("%%COMMENT%%");
				String reviewNo = rawLine[0];
				String lineWords[] = null;
				try{
					lineWords = rawLine[1].split("\\s");
				}catch(Exception e){
					lineWords = new String[]{""};
				}
				fileWriter.write(reviewNo+"%%COMMENT%%");
				for(String words:lineWords){
					String[] word_tf = words.split(",");
					if(word_tf[0]!=""){
						double idf = 1 + Math.log(count/IDFfreqHashMap.get(word_tf[0]));
						double tfidf = Double.parseDouble(word_tf[1])*idf;
						fileWriter.write(word_tf[0]+"%%TERM%%"+tfidf+" ");
					}
				}
				fileWriter.write("\n");
			}
			fileWriter.close();
			br.close();
			file.delete();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private static void cosineSimilarity(){

		BufferedReader br;
		String sCurrentLine;
		try{
			HashMap<String, Double> commentONE = new HashMap<String,Double>();
			HashMap<String, Double> commentTWO = new HashMap<String,Double>();

			boolean line = true;
			int loop=2;
			/*try (Stream<String> lines = Files.lines(Paths.get("TFIDF.tmp"))) {
			    String line32 = lines.skip(32).findFirst().get();
			    System.out.println(line32);
			}*/
			
			//FileWriter fileWriter = new FileWriter("TFIDF.tmp");
			while(loop>1){
				
					File file = new File("TFIDF.tmp");
					int lineCount=0;
					br = new BufferedReader(new FileReader(file));
					File tempFile = new File("myTempFile.tmp");
					FileWriter writer = new  FileWriter(tempFile);	
					boolean i=false;
					int count = 0;
					while ((sCurrentLine = br.readLine()) != null) {
						count++;
						String rawLine[] = sCurrentLine.split("%%COMMENT%%");
						String reviewNo = rawLine[0];
						String lineWords[] = null;
						try{
							lineWords = rawLine[1].split("\\s");
						}catch(Exception e){
							lineWords = new String[]{""};
						}

						if(i){
							writer.write(sCurrentLine+"\n");
							lineCount++;
							
						}else{
							i=true;
						}		

						for(String words:lineWords){
							String[] rawLineData = words.split("\\s");
							for(String temp:rawLineData){
								String lineDat[] = temp.split("%%TERM%%");
								if(i){
									//commentONE.put(lineDat[0],Double.parseDouble(lineDat[1]));
								}else{
									//commentTWO.put(lineDat[0],Double.parseDouble(lineDat[1]));
								}

							}

						}

					}
					System.out.println(count);
					writer.close();
					br.close();
					file.delete();
					tempFile.renameTo(new File("TFIDF.tmp"));
					//tempFile.delete();
					System.out.println("LineCount"+lineCount);
					loop=lineCount/2;
					System.out.println(loop);
					
					
			
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void cosineRelative(HashMap<String, Double> CommentOne,HashMap<String, Double> CommentTwo){

		ArrayList<String> keywords = new  ArrayList<>();
		keywords.addAll(CommentOne.keySet());
		keywords.addAll(CommentTwo.keySet());

	}

	public void ReviewTFIDF(){

		tfidfcalulate(allCommentsFrequencyCount());
		cosineSimilarity();
	}
}
