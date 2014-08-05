package SeleniumScrapper;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TwitterScrapper {
    public static void main(String[] args) throws Exception {
    	
        // The Firefox driver supports javascript 
        WebDriver driver = new FirefoxDriver();
        

        File jsonPlayers = new File("/u/jeremiah/players.txt");
        Scanner lookAtFile = new Scanner(jsonPlayers);
        JSONObject playerNames = new JSONObject(lookAtFile.nextLine());
        Iterator players = playerNames.keys();
        PrintWriter writer = new PrintWriter("/u/jeremiah/playerTwitterNames.json");
        
        // start dictionary
        String twitterName = "";
        int i = 0;
        System.out.print("{");
        writer.print("{");
        while (players.hasNext()) { // goes through 736 times
        	if (i == 30) {
        		driver.close();
        		driver = new FirefoxDriver();
        		i = 0;
        	}
        	String key = (String) players.next();
//        	System.out.println(key); // keys are good

        	// this google link doesn't have suggestions, which messed with getting links
        	driver.get("https://www.google.com/webhp?complete=0&hl=en&gws_rd=ssl");
        	
        	// Find the search box, search for "<name> Twitter" and submit
        	WebElement query = driver.findElement(By.name("q"));
        	query.sendKeys(key + " Twitter");
	        query.submit();
	        
	        // this is to let the page load and get the first link from a google search
	        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	        List<WebElement> findElements = driver.findElements(By.xpath("//*[@id='rso']//h3/a"));
	        
	        twitterName = "";
	        if (!findElements.isEmpty()) {
	        	twitterName = findElements.get(0).getAttribute("href");
	        }
	   
	        
	        // Only want links that are from twitter.
	        // If the first link is not a twitter link,
	        // I assume the player does not have a Twitter account
	        if (twitterName.startsWith("https://twitter.com/")) {
	        	twitterName = twitterName.substring(20);
	        	
	        	// if this character is contained, that means it is not a profile
	        	// only want links that go to profiles since we want the username
	        	if (!twitterName.contains("/")) {
	        	System.out.print("\"" + key + "\":\"" + twitterName + "\",");
	        	writer.print("\"" + key + "\":\"" + twitterName + "\",");
	        	}
	        }
	        i++;
        }
        System.out.print("}"); // end the dictionary
        writer.print("}");
        lookAtFile.close();
        writer.close();
        driver.quit();
    }
}