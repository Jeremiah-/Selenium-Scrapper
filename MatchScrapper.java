package SeleniumScrapper;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MatchScrapper {
	public static void main(String[] args) throws Exception{
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.fifa.com/worldcup/matches/index.html"); // matches
        PrintWriter writer = new PrintWriter("/u/jeremiah/MatchStats.txt");

        
        // gotta get a list of the countries, but like with CountryStatScrapper,
        // have to copy the urls and names before we advance with the driver.
        Integer date;
        int startDate = 20140612;
        String countryURL;
        List<WebElement> countryLinks;
        ArrayList<String> containerOfAllMatches = new ArrayList<String>();
        for (int i = startDate; i < 20140714; i++) {
        	
        	try {
	        	date = new Integer(i); // need this to be able to cast the int to a string
	        	countryLinks = driver.findElements(By.xpath(
	        			"//div[@class='matches']//div[@id='" + date.toString() + "']//a"));
	        	
	        	//countryLinks can contain multiple matches
	        	for (int k = 0; k < countryLinks.size(); k++) {
	        		countryURL = countryLinks.get(k).getAttribute("href"); // href attribute contains the link
	            	containerOfAllMatches.add(countryURL);
	        	}
        	} catch (NoSuchElementException e) {
        		// this is here for the bad links that will be created.
        		// the loop goes through invalid dates which will throw this exception
        	}
        }
        System.out.printf("%d\n", containerOfAllMatches.size());
        assert (containerOfAllMatches.size() == 64);
        
        String countryA;
        String countryB;
        System.out.print("{");
        writer.print("{");
        for (int i = 0; i < containerOfAllMatches.size(); i++) {
        	String url = containerOfAllMatches.get(i);
        	
        	// the link stays the same for the match statistics except for the end (very handy)
        	url = url.replaceFirst("index.html#nosticky", "statistics.html#nosticky");
        	
        	// don't know why, but using the same driver isn't working. Have to create a new one.
        	// it would navigate the pages but the information printed was the same.
        	// maybe it was a property of those certain pages? try commenting out the close and new to see what I mean
        	driver.close();
        	driver = new FirefoxDriver();
        	driver.get(url);
        	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
 
        	List<WebElement> countryNames = driver.findElements(By.xpath(
        			"//div[@class='container']//div[@class='mh-m']//div[@class='t-n']"));

        	assert (countryNames.size() == 2);
        	
        	countryA = countryNames.get(0).findElement(By.cssSelector("span")).getText().toLowerCase();
        	countryB = countryNames.get(1).findElement(By.cssSelector("span")).getText().toLowerCase();
        	
        	System.out.print("\"" + countryA + "-" + countryB + "\" : [");
        	writer.print("\"" + countryA + "-" + countryB + "\":[");
        	
        	getStatsFromTable(driver, "//div[@id='attacking' and @class='anchor']", writer);
        	getStatsFromTable(driver, "//div[@id='defending' and @class='anchor']", writer);
        	writer.print("],");
        }
        System.out.print("}");
        writer.print("}");
        driver.close();
        writer.close();
	}

	private static void getStatsFromTable(WebDriver driver, String xpath, PrintWriter writer) {
		List<WebElement> rows = driver.findElements(By.xpath(
				xpath + "//table//tr[@data-codeid]"));
		while (rows.size() < 1) {
			rows = driver.findElements(By.xpath(
					xpath + "//table//tr[@data-codeid]"));
		}
		
		assert (rows.size() == 5 || rows.size() == 6);
		
		System.out.printf("how many rows: %d \n", rows.size());
		for (WebElement row : rows) {

			List<WebElement> rowStats = row.findElements(By.cssSelector("td"));
			
			// index 0 and 4 are useless
			System.out.print(rowStats.get(2).getText() + " "); // name of stat
			System.out.print(rowStats.get(1).getText() + " "); // home stat
			System.out.print(rowStats.get(3).getText() + ", "); // away stat
			writer.print(rowStats.get(1).getText() + ",");
			writer.print(rowStats.get(3).getText() + ",");
		}
		System.out.println();
	}
	
}
