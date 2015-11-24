/*
This is the main file the "keyword" value is the string which is fired on the e-commerce website search box.

There are two thread one for flipkart.com and another for amazon.in, currently only amazon.in is working.

When the amazon_scrapper_thread is executed it first get all the links for the products in search result by executing
"AmazonSearchLinkScrapper" class which takes keyword as the parameter
*/
package Exec;

import Scrapper.AmazonReviewScrapper;
import Scrapper.AmazonSearchLinkScrapper;
import Scrapper.FlipkartSearchLinkScrapper;
import dao.Store;

public class ExecMain {

	public static void main(String[] args) {

		final String keyword = "headphones";

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
