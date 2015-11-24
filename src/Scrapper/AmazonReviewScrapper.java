/*
This class takes url as input of a product and scraps all the details from the page and
stores in mongodb
*/
package Scrapper;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import conf.Config;
import dao.Store;
import utils.DateConversion;

public class AmazonReviewScrapper {

	public  void amazonReviewScrapper(String url) {
		Document doc;
		System.out.println("inside amazon review scrapper"+url);
		int totalNoOfComments=0;
		Store store = null;
		try {
			//Setting Proxy
			System.setProperty("http.proxyHost", Config.config().getProperty("proxy_url"));

			doc = Jsoup.connect(url)
					.data("query", "Java")
					.userAgent("Mozilla")
					.cookie("auth", "token")
					.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
					.post();

			try{
				totalNoOfComments = Integer.parseInt(doc.getElementsByClass("a-link-emphasis").last().html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9?!\\.]",""));
			}catch(Exception ex){
				totalNoOfComments=10;
			}
			String productName = doc.getElementById("productTitle").html().toLowerCase();
			String category;

			category=doc.getElementById("nav-subnav").select("a").first().select("span").html().replace("&amp;", "&");

			String price = null;
			try{
				price=doc.getElementById("priceblock_ourprice").html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9.?!\\.]","");
			}catch(Exception ex){
				try{
					price=doc.getElementById("priceblock_saleprice").html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9.?!\\.]","");
				}catch(NullPointerException nu){
					try{
						price=doc.getElementById("olp_feature_div").select("span").last().html().replaceAll("(?<=\\d),(?=\\d)", "").replaceAll("[^0-9.?!\\.]","");
					}catch(Exception e){
						price="0";
					}
				}
			}

			String specification =null;
			try{
				specification = Jsoup.parse((doc.getElementById("prodDetails").html())).text();
			}catch(Exception ex){
				try{
					specification = Jsoup.parse((doc.getElementById("techSpecSoftlinesWrap").html())).text();
				}catch(Exception e){
					try{
						specification = Jsoup.parse((doc.getElementById("productDescription").html())).text();
					}catch(Exception e1){
						specification="NA";
					}
				}
			}
			store = new Store();
			org.bson.Document document = new org.bson.Document();
			document.append("productName",productName);
			document.append("totalComments", totalNoOfComments);
			document.append("url", url);
			document.append("category", category);
			document.append("price", price);
			document.append("specification",specification);
			String refId = store.MongoDocumentCreate(document);

			//Getting link to open all review 
			url = doc.getElementsByClass("a-link-emphasis").attr("abs:href"); 
			

				int count=0;
				Pattern pattern = Pattern.compile("profile/(.*?)/");


				//Iterating over all the pages
				for(int i=0;i<=totalNoOfComments/10;i++){
					if(!url.equals(null)){
					System.out.println("URL"+url);
					doc = Jsoup.connect(url)
							.data("query", "Java")
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2")
							.cookie("auth", "token")
							.timeout(Integer.parseInt(Config.config().getProperty("timeout")))
							.post();

					Elements element = doc.getElementsByClass("review");
					for(Element temp: element){
						int stars = Integer.parseInt(temp.getElementsByClass("a-icon-alt").html().substring(0, 1));
						String username = temp.getElementsByClass("author").html();
						Matcher matcher = pattern.matcher(temp.getElementsByClass("author").attr("href"));
						String user_profile_url = "NA";
						if (matcher.find()) {
							user_profile_url = matcher.group(1);
						}
						String post_date_raw=temp.getElementsByClass("review-date").html().replace("on ","");
						String post_date = DateConversion.dateParse(post_date_raw);
						java.util.Date date= new java.util.Date();
						int random = 0 +(int)(Math.random()*1000);
						String review_no = new Timestamp(date.getTime()).toString()+random;
						System.out.println(user_profile_url);
						String review = temp.getElementsByClass("review-text").text();
						store.DataStreamReceiver(username,user_profile_url,stars,review,review_no,refId,post_date);
						count++;
					}

					try{
						url = doc.getElementsByClass("a-last").select("a").last().attr("abs:href");
						System.out.println("TEST"+url);
					}catch(NullPointerException nu){
						try{
							url = "http://www.amazon.in"+doc.getElementsByClass("a-last").select("a").last().attr("href");
						}catch(Exception e){
							System.out.println("NUNUNUNUNUNU @"+url);
							
						}
						
					}
					//next page link

					System.out.println("Current url"+ url);

				}
				
				
			}
				System.out.println(count);
				store.closeConnection();
			
		} catch (IOException e) {
			if(store!=null){
				store.closeConnection();
			}
			//e.printStackTrace();

		}
	}
}
