package Scrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import conf.Config;
import dao.MongoDao;
import dao.Store;

public class FlipkartReviewScrapper {
	public static void FlipkartReviewScrapper() {
		Document doc;
		
		String url ="http://www.flipkart.com/asus-fe380cg-1g046a-1g052a-fonepad-8-tablet/p/itme3mvqqh2tufdh?pid=TABE3MVHHQJURZNF&al=pK62JZqIxbLfg9HJtYKWZsldugMWZuE7wkNiXfq8GiQKQ6VhnzMNOHQaK9jP1LJO%2BW7a%2F%2BTTydw%3D&ref=L%3A5354562021126326687&srno=b_1";
		int totalNoOfComments=0;
		try {
			System.out.println("fkrs");
			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));
			System.setProperty("http.proxyPort", Config.config().getProperty("proxy_port"));
			/*
			Socket socket = new Socket(Config.config().getProperty("socket_host"),
					Integer.parseInt(Config.config().getProperty("socket_port")));
			 socket.setSoTimeout(7000);
socket.setKeepAlive(true);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);*/
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
			
			//product name
			String productName = doc.getElementsByTag("h1").html().toLowerCase();
			String category = doc.getElementsByClass("clp-breadcrumb").select("li").select("a").eq(2).html().toLowerCase();
			int price=Integer.parseInt(doc.getElementsByClass("selling-price").html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9.?!\\.]","").replace(".",""));
			String specification = Jsoup.parse((doc.getElementsByClass("specSection").html())).text();
			System.out.println(specification);
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
			url = doc.getElementsByClass("reviewListBottom").select("a").first().attr("abs:href"); 
			int count=0;
			
			//Iterating over all the pages
			for(int i=0;i<=totalNoOfComments/10;i++){
				doc = Jsoup.connect(url)
						.data("query", "Java")
						.userAgent("Mozilla")
						.cookie("auth", "token")
						.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
						.post();
				Elements element = doc.getElementsByClass("fk-review");
				for(Element temp: element){
					//System.out.println(temp.text());
					//out.println(temp.text());
					int stars = Integer.parseInt(temp.getElementsByClass("fk-stars").attr("title").substring(0, 1));
					String username=null;
					String user_profile_url = "NA";
					if((username = temp.getElementsByClass("load-user-widget").html().replace(".", "")).isEmpty()){
						username=temp.getElementsByClass("review-username").text();//.replace(".", "");
					}else{
						user_profile_url = temp.getElementsByClass("load-user-widget").attr("href");
					}
					System.out.println("USERNAME"+ username +" "+user_profile_url);
					String review = temp.getElementsByClass("review-text").text();
					store.DataStreamReceiver(username,user_profile_url,stars,review);
					count++;
				}
				
				//next page link
				url = doc.getElementsByClass("nav_bar_next_prev").select("a").last().attr("abs:href"); 
				
			}
			store.closeConnection();
			/*out.println("EOF");
			out.close();
			socket.close();
			System.out.println(count);*/
			
			//System.out.println(count);	 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
