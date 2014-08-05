package SeleniumScrapper;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

// makes Instagram widgets for soccer players, (usernames gotten
// from modified TwitterScrapper). Parses the unique URLs in the widgets, 
// stores them in a dictionary.


public class InstagramWidgetURL {
    public static void main(String[] args) throws Exception {
    	
        // The Firefox driver supports javascript 
        WebDriver driver = new FirefoxDriver();
        

        File jsonPlayers = new File("/u/jeremiah/playerInstagramNames.json"); // has 257
        Scanner lookAtFile = new Scanner(jsonPlayers);
        JSONObject playerNames = new JSONObject(lookAtFile.nextLine());
        Iterator players = playerNames.keys();
        PrintWriter writer = new PrintWriter("/u/jeremiah/playerInstagramWidgetURLs.json");
        
//    	driver.get("http://snapwidget.com/");
//        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);


        // start dictionary
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
//        	System.out.println(); // keys are good

        	// refresh the page to start a new widget
//        	WebDriver driver = new FirefoxDriver();
        	driver.get("http://snapwidget.com/");
	        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        	// Find the username box and submit the username
        	WebElement query = driver.findElement(By.name("username"));
        	query.sendKeys(key);
//	        query.submit();
	        
	        // this is to let the page load and click the "Submit" botton for the widget code
	        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	        WebElement findElement = driver.findElement(By.xpath("//*[@id='btnGetCode']"));
	        findElement.click();
	        
	        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//	        driver.manage().timeouts().wait(5000);
	     
//	        
//	        findElement = driver.findElement(By.xpath("//span[@class='atv']"));
//	        System.out.println(findElement.getAttribute("span")); // null
//	        System.out.println(findElement.getTagName()); // span
//	        System.out.println(findElement.getCssValue("span")); // empty
//	        System.out.println(findElement.getText()); // empty
	        
	        
//	        List<WebElement> findElements = driver.findElements(By.className("atv"));
//	        findElement = findElements.get(0);
//	        System.out.printf("%d\n", findElements.size()); // 7
//	        System.out.println(findElement.getAttribute("span")); // null
//	        System.out.println(findElement.getTagName()); // span
//	        System.out.println(findElement.getCssValue("span")); // empty
//	        System.out.println(findElement.getText()); // WHAT WE WANT
	        
	        findElement = driver.findElement(By.className("atv"));
//	        System.out.println(findElement.getAttribute("span")); // null
//	        System.out.println(findElement.getTagName()); // span
//	        System.out.println(findElement.getCssValue("span")); // empty
//	        System.out.println(findElement.getText()); // WHAT WE WANT
	   	    String url = findElement.getText();
	   	    while (url.equals("")) {
	   	    	url = findElement.getText();
	   	    }
	        
//        	System.out.print("\"" + key + "\":" + url + ",");
	   	    System.out.println(key + " : " + url);
        	writer.print("\"" + key + "\":" + url + ",");
	        i++;
//	        driver.close();
        }
        System.out.print("}"); // end the dictionary
        writer.print("}");
        lookAtFile.close();
        writer.close();
//        driver.quit();
    }
}
