package Scrapper;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import conf.Config;



public class FlipkartReviewScrapper {
	public static void main(String[] args) {
		Document doc;
		
		String url ="http://www.flipkart.com/ambrane-p-1310-13000-mah/p/itme8kj5caedgg4h?pid=PWBE3GDFASZPWEGQ&ref=L%3A-8652693929287987991&srno=p_3&query=power+bank&otracker=from-search";
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

			//total no. of comments, helpful for controlling loops eg. Read more top reviews(68)
			String s = doc.getElementsByClass("lnkViewMore").last().text();
			Pattern pattern = Pattern.compile("\\((.*?)\\)");
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) {
				totalNoOfComments=Integer.parseInt(matcher.group(1));
			}

			//Getting link to open all review 
			url = doc.getElementsByClass("reviewListBottom").select("a").first().attr("abs:href"); 
			int count=0;
			
			//Iterating over all the pages
			for(int i=0;i<totalNoOfComments/10;i++){
				doc = Jsoup.connect(url)
						.data("query", "Java")
						.userAgent("Mozilla")
						.cookie("auth", "token")
						.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
						.post();
				Elements element = doc.getElementsByClass("review-text");
				for(Element temp: element){
					System.out.println(temp.text());
					count++;
				}
				
				//next page link
				url = doc.getElementsByClass("nav_bar_next_prev").select("a").last().attr("abs:href"); 
				
			}
			System.out.println(count);
			//System.out.println(count);	 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
