package Scrapper;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import conf.Config;

public class AmazonSearchLinkScrapper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Document doc;

		String url ="http://www.amazon.in/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=bags";

		try {
			System.out.println("fkrs");
			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));
			System.setProperty("http.proxyPort", Config.config().getProperty("proxy_port"));

			doc = Jsoup.connect(url)
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.maxBodySize(0)
					.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
					.post();		    
			ArrayList<String> url_list = new ArrayList<>();
			String url1=null;
			Integer totalPage = 0;

			try{

				totalPage = Integer.parseInt(doc.getElementsByClass("pagnDisabled").html());
				System.out.println(totalPage);
			}catch(Exception ex){

			}
			for(int i=0;i<=totalPage;i++){
				doc = Jsoup.connect(url)
						.data("query", "Java")
						.userAgent("Mozilla")
						.cookie("auth", "token")
						.maxBodySize(0)
						.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
						.post();		    
				Elements elements = doc.getElementsByClass("s-item-container");
				for(Element element:elements){
					if(!(url1=element.getElementsByClass("a-link-normal").attr("href").toString()).isEmpty()){
						if(url1.substring(0, 1).equals("/")){
							url1="http://www.amazon.in"+url1;
						}
						url_list.add(url1);
					}
				}

				try{
					url = doc.getElementById("pagnNextLink").attr("href");
					if(url.substring(0, 1).equals("/")){
						url="http://www.amazon.in"+url;
					}
				}catch(Exception ex){

				}
			}
			for(String uri:url_list){
				System.out.println(uri);
				//FlipkartReviewScrapper.FlipkartReviewScrapper(uri);
			}
			System.out.println(url_list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}
