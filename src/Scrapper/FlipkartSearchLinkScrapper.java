package Scrapper;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import conf.Config;

public class FlipkartSearchLinkScrapper {

<<<<<<< HEAD
	public void flipkartquery(String keyword) {
		Document doc;

		String url ="http://www.flipkart.com/search?q="+keyword;

		try {
			System.out.println("Flipkart search Link Scrapper......BEGIN");

			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));
			System.setProperty("http.proxyPort", Config.config().getProperty("proxy_port"));

=======
	public static void main(String[] args) {
		// TODO Auto-generated method stub
Document doc;
		
		String url ="http://www.flipkart.com/search?q=wallet";
	
		try {
			System.out.println("fkrs");
			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));
			System.setProperty("http.proxyPort", Config.config().getProperty("proxy_port"));
		
>>>>>>> origin/master
			doc = Jsoup.connect(url)
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.maxBodySize(0)
					.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
					.post();		    
<<<<<<< HEAD
			ArrayList<String> url_list = new ArrayList<>();
			String url1=null;
			Elements elements = doc.getElementsByClass("product-unit");
			for(Element element:elements){
				if(!(url1=element.getElementsByClass("squarebox").select("a").attr("href").toString()).isEmpty()){
					url_list.add("http://www.flipkart.com"+url1);
				}
			}
			System.out.println("Flipkart Link Scrapping finished found "+url_list.size()+" links");
			for(String uri:url_list){
				System.out.println(uri);
				FlipkartReviewScrapper flipkartReviewScrapper = new FlipkartReviewScrapper();
				flipkartReviewScrapper.flipkartReviewScrapper(uri);
			}
			System.out.println(url_list);
		} catch (IOException e) {
			e.printStackTrace();
		}


=======
		ArrayList<String> url_list = new ArrayList<>();
		String url1=null;
		Elements elements = doc.getElementsByClass("product-unit");
		for(Element element:elements){
			if(!(url1=element.getElementsByClass("squarebox").select("a").attr("href").toString()).isEmpty()){
				url_list.add("http://www.flipkart.com"+url1);
			}
		}
	
		for(String uri:url_list){
			System.out.println(uri);
			FlipkartReviewScrapper.FlipkartReviewScrapper(uri);
		}
		System.out.println(url_list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
>>>>>>> origin/master

	}

}
