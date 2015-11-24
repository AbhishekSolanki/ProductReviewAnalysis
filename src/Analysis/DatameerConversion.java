package Analysis;

import java.io.IOException;

import dao.FetchData;

public class DatameerConversion {

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
