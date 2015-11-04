package Scrapper;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import conf.Config;



public class AmazonReviewScrapper {
	public static void main(String[] args) throws InterruptedException {
		Document doc;

		String url ="http://www.amazon.com/KMASHI-10000mAh-External-Portable-Powerful/dp/B00JM59JPG/ref=sr_1_6?s=electronics&ie=UTF8&qid=1446532304&sr=1-6&keywords=power+bank";
		int totalNoOfComments=0;
		try {
			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));
			System.setProperty("http.proxyPort", Config.config().getProperty("proxy_port"));

			doc = Jsoup.connect(url)
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
					.post();
			//doc  = Jsoup.parse(doc.toString());
			//total no. of comments, helpful for controlling loops eg. Read more top reviews(68)
			totalNoOfComments = Integer.parseInt(doc.getElementsByClass("a-link-emphasis").last().html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9?!\\.]",""));
	
			
			//Getting link to open all review 
			url = doc.getElementsByClass("a-link-emphasis").attr("abs:href"); 
			System.out.println(url);
			int count=0;
			
			//Iterating over all the pages
			for(int i=0;i<totalNoOfComments/10;i++){
				//Thread.sleep(100);
				doc = Jsoup.connect(url)
						.data("query", "Java")
						.userAgent("Mozilla")
						.cookie("auth", "token")
						.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
						.post();
				url = doc.getElementsByClass("a-last").select("a").last().attr("abs:href");
				Elements element = doc.getElementsByClass("review-text");
				for(Element temp: element){
					System.out.println(temp.text());
					count++;
				}
				
				//next page link
				
			
					
				//url = doc.getElementsByClass("a-pagination").select("li").last().select("a").attr("abs:href");
				System.out.println("Current url"+ url);
				
			}
			System.out.println(count);
			//System.out.println(count);	 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}