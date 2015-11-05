package Scrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import conf.Config;
import dao.Store;



public class AmazonReviewScrapper {
	public static void AmazonReviewScrapper() {
		Document doc;

		String url ="http://www.amazon.in/iPro-iP40-Powerbank-Smartphones-Tablets13000/dp/B015H3VXI2/ref=sr_1_4?s=electronics&ie=UTF8&qid=1446720866&sr=1-4&keywords=powerbank";
		int totalNoOfComments=0;
		try {
			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));
			System.setProperty("http.proxyPort", Config.config().getProperty("proxy_port"));
			Socket socket = new Socket(Config.config().getProperty("socket_host"),
					Integer.parseInt(Config.config().getProperty("socket_port")));
			socket.setSoTimeout(7000);
			socket.setKeepAlive(true);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			doc = Jsoup.connect(url)
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
					.post();
			//doc  = Jsoup.parse(doc.toString());
			//total no. of comments, helpful for controlling loops eg. Read more top reviews(68)
			totalNoOfComments = Integer.parseInt(doc.getElementsByClass("a-link-emphasis").last().html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9?!\\.]",""));
			String productName = doc.getElementById("productTitle").html().toLowerCase();
			String category = doc.getElementsByClass("content").last().select("li").select("a").eq(1).html().toLowerCase();
			String price=doc.getElementById("priceblock_ourprice").html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9.?!\\.]","");
			String specification = Jsoup.parse((doc.getElementById("prodDetails").html())).text();
			Store store = new Store();
			org.bson.Document document = new org.bson.Document();
			document.append("productName",productName);
			document.append("totalComments", totalNoOfComments);
			document.append("url", url);
			document.append("category", category);
			document.append("price", price);
			document.append("specification",specification);
			store.MongoDocumentCreate(document);

			//Getting link to open all review 
			url = doc.getElementsByClass("a-link-emphasis").attr("abs:href"); 
			System.out.println(url);
			int count=0;

			//Iterating over all the pages
			for(int i=0;i<=totalNoOfComments/10;i++){
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
					out.println(temp.text());
					count++;
				}
				
				//next page link



				//url = doc.getElementsByClass("a-pagination").select("li").last().select("a").attr("abs:href");
				System.out.println("Current url"+ url);

			}
			System.out.println(count);
			out.println("EOF");
			out.close();
			socket.close();
			System.out.println(count);
			
			//System.out.println(count);	 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
