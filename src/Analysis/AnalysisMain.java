/*
 This is the main class which executes all the analysis portion of the project:
 
 1. tfidf of project reviews and finding similarity by calling "TfidfOfReviews" class.
 
 */
package Analysis;

import java.io.IOException;

import dao.FetchData;

public class AnalysisMain {

	public static void main(String[] args) {

		TfidfOfReviews tfidfOfReviews = new TfidfOfReviews();
		tfidfOfReviews.ReviewTFIDF();
		FetchData d = new  FetchData();
		try {
		//	d.ReviewDocToJSON();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
