package utils;

import java.util.HashMap;

public class DateConversion {

	
	public static String dateParse(String raw_date){
	
		HashMap<String,String> months= new HashMap<String,String>();
		months.put("janauary","01");
		months.put("feburary", "02");
		months.put("march", "03");
		months.put("april", "04");
		months.put("may", "05");
		months.put("june", "06");
		months.put("july", "07");
		months.put("august","08");
		months.put("september","09");
		months.put("october", "10");
		months.put("november", "11");
		months.put("december", "12");
		
		String[] raw_date_array = raw_date.split(" ");
		String final_date = raw_date_array[2]+"-"+months.get(raw_date_array[1].toLowerCase())+"-"+raw_date_array[0];
		
		return final_date;
		
		
	}
}
