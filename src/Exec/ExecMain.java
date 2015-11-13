package Exec;

import Scrapper.AmazonReviewScrapper;
import Scrapper.AmazonSearchLinkScrapper;
import Scrapper.FlipkartSearchLinkScrapper;
import dao.Store;

public class ExecMain {

	public static void main(String[] args) {

		final String keyword = "wallet";

		try {

			Thread amazon_scrapper_thread = new Thread(){
				public void run(){
					AmazonSearchLinkScrapper amazonSearchLinkScrapper = new AmazonSearchLinkScrapper();
					amazonSearchLinkScrapper.amazonQuery(keyword);
				} 
			};

			Thread flipkart_scrapper_thread = new Thread(){
				public void run(){
					FlipkartSearchLinkScrapper flipkartSearchLinkScrapper = new FlipkartSearchLinkScrapper();
					flipkartSearchLinkScrapper.flipkartquery(keyword);
				} 
			};

			//flipkart_scrapper_thread.start();
			amazon_scrapper_thread.start();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
