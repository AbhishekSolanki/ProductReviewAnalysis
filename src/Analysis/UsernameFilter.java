package Analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import dao.FetchData;
import utils.CosineSimilarity;
import utils.LevenshteinDistanceAlgorithm;




public class UsernameFilter {

	
	public static ArrayList<String> filteredUserCommentCheck(HashMap<String, String> map){
		 System.out.println("No. of Comments for Filtered Users "+map.size());
		    Iterator<Entry<String, String>> i = map.entrySet().iterator();
		    int count=0;
		    int countt=0;
		    while (i.hasNext()) {
		        Entry next = i.next();
		        i.remove();
		        for (Entry e : map.entrySet()) {
		            e.equals(next);
		            String va1 = e.getValue().toString();
		            String va2 = next.getValue().toString();
		           CosineSimilarity c = new CosineSimilarity();
		           if(c.Cosine_Similarity_Score(va2,va1)>=0.8){
		        	  System.out.println(va1+"->"+va2);
		        	   count++;
		           }
		           countt++;
		           
		        }
		    }
		    System.out.println("total review combination count "+countt+"\ncomment count which are 55% or more similar "+ count);
		return null;
		
	}
	
	
	public static void main(String args[]) {
		FetchData data = new FetchData();
		ArrayList<String> al =data.usernames();
		//System.out.println(al.toString());
		
		ArrayList<String> filtedList = new ArrayList<String>(); //List of users whose are very much similar
		
		int count=0;
		for(int i=0;i<al.size()-1;i++){
			String one = al.get(i);
			for(int j=i+1;j<al.size();j++){
				String two = al.get(j);
				
				if(LevenshteinDistanceAlgorithm.distance(one, two)<3){
					//System.out.println(one+" "+two);
					if(!filtedList.contains(one)){
						filtedList.add(one);
					}if(!filtedList.contains(two)){
						filtedList.add(two);
					}
					count++;
				}
			}
		}
		
		//System.out.println(count+"+"+filtedList.size());
		System.out.println("Filtered User Count "+filtedList.size());
		filteredUserCommentCheck(data.filteredUsersComment(filtedList));
	}
	

	
}
