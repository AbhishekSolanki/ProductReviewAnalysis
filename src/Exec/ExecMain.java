package Exec;

import Scrapper.AmazonReviewScrapper;
import Scrapper.FlipkartReviewScrapper;
import dao.Store;

public class ExecMain {



	public static void main(String[] args) {


		try {
			Thread data_store_thread = new Thread(){
					public void run(){
						//Store.DataStreamReceiver();
						}
					};
					
			Thread flipkart_scrapper_thread = new Thread(){
						public void run(){
							//FlipkartReviewScrapper.FlipkartReviewScrapper();
							//AmazonReviewScrapper.AmazonReviewScrapper();
						} 
					};
					
					//data_store_thread.start();
					flipkart_scrapper_thread.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
