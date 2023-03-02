package com.company.nill.myTool.utill;

import com.fasterxml.uuid.Generators;
import com.nhncorp.lucy.security.xss.XssPreventer;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class StringUtils extends org.springframework.util.StringUtils {
    public static String getRandomStr(int size) {
        Random rnd = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            String randomStr = String.valueOf((char) ((int) (rnd.nextInt(26)) + 65));
            sb.append(randomStr);
        }
        return sb.toString();
    }

    public static String getRandomNum(int size) {
        Random rnd = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            String randomStr = String.valueOf((char) ((int) (rnd.nextInt(10)) + 48));
            sb.append(randomStr);
        }
        return sb.toString();
    }

    public static String numFormatter(int size, int value) {
        String suffix = String.format("%0"+size+"d", value);
        return suffix;
    }

    public static String UUID() {
        UUID uuid2 = Generators.randomBasedGenerator().generate();
        return uuid2.toString();
    }

    public static String numFormatter(int size, String value) {
        return numFormatter(size,Integer.parseInt(value));
    }

    public static String now(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String substring(String src,int start,int end) {
        try {
            return src.substring(start, end);
        } catch (Exception e) {
            return src;
        }
    }
    
    //xss 방어
    public static String cleanXSS(String value) {
		return XssPreventer.escape(value);
    }

    //빅데시멀 가격 비교
    public static int bigdecimal_compare(String source , String target) {
    	
    	String first = source;
    	
    	if(source == null || source.length() == 0) {
    		first = "0";
    	}
    	
    	String second = target;
    	
    	if(target == null || target.length() == 0) {
    		second = "0";
    	}
    	
    	BigDecimal b_first =  new BigDecimal(first);
  		BigDecimal b_second = new BigDecimal(second);
  		
  		return b_first.compareTo(b_second);
    }

    public static String getNation() {
    	
    	String nation[] = {"PHL" , "KOR" , "JPN" , "GBR" , "FRA" , "ESP" , "CHN" , "CAN" , "BRA" , "AUS" , "ARG" , "USA" , "IDN"};
    	String val = nation[0];
    	
    	
    	try {
    		int randomValue = new RandomDataGenerator().nextInt(0,12);
    		
    		val = nation[randomValue];
    		
    	} catch (Exception e) {
    		
    	}
    	
    	return val;
    }
    
    public static String ip_nation_check(String ip) {
    	
    	String rtn_val = "";
    	
    	try {
	    	HttpURLConnection urlcon = (HttpURLConnection)new URL("http://ip2c.org/"+ip).openConnection();
	    	urlcon.setDefaultUseCaches(false);
	    	urlcon.setUseCaches(false);
	    	urlcon.connect();
	    	InputStream is = urlcon.getInputStream();
	    	int c = 0;
	    	String s = "";
	    	while((c = is.read()) != -1) s+= (char)c;
	    	is.close();
	    	switch(s.charAt(0))
	    	{
	    	  case '0':
	    	    System.out.println("Something wrong");
	    	    break;
	    	  case '1':
	    	    String[] reply = s.split(";");
	    	    System.out.println("Two-letter: " + reply[1]);
	    	    System.out.println("Three-letter: " + reply[2]);
	    	    System.out.println("Full name: " + reply[3]);
	    	    
	    	    rtn_val = reply[2];
	    	    
	    	    break;
	    	  case '2':
	    	    System.out.println("Not found in database");
	    	    break;
	    	}
    	}
	    catch(Exception e) {
	    	
	    }
    	
    	if(rtn_val == null || rtn_val.length() == 0 ) {
    		rtn_val = getNation();
    	}
    	
    	
    	return rtn_val;
    }
}
