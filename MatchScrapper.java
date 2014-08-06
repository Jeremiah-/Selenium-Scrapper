package SeleniumScrapper;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MatchScrapper {
	public static void main(String[] args) throws Exception{
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.fifa.com/worldcup/matches/index.html"); // countries
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
	        	date = new Integer(i);
	        	countryLinks = driver.findElements(By.xpath(
	        			"//div[@class='matches']//div[@id='" + date.toString() + "']//a"));
	        	for (int k = 0; k < countryLinks.size(); k++) {
	        		countryURL = countryLinks.get(k).getAttribute("href");
	            	containerOfAllMatches.add(countryURL);
	        	}
        	} catch (NoSuchElementException e) {
        		
        	}
        	
        }
        System.out.printf("%d\n", containerOfAllMatches.size());
        
        // can change this to a for each loop :D
        for (String url : containerOfAllMatches) {
        	driver.get(url);
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	
        	// I think it's freezing right here. It takes too long to find this link.
        	WebElement page = driver.findElement(// By.id("mitem-statistics"));
        			By.xpath("//div[@class='nav-wrap']//li[@id='mitem-statistics']" +
        					"//a[@data-ref='#statistics']"));

        	page.click();
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	// good above this point.
        	
        	getStatsFromTable(driver, "//div[@id='attacking' and @class='anchor']", writer);
        	getStatsFromTable(driver, "//div[@id='defending' and @class='anchor']", writer);
        }
//        driver.close();
	}

	private static void getStatsFromTable(WebDriver driver, String xpath, PrintWriter writer) {
		// TODO Auto-generated method stub
		List<WebElement> rows = driver.findElements(By.xpath(
				xpath + "//table//tr[@data-codeid]"));
//		System.out.printf("how many rows: %d \n", rows.size());
		for (WebElement row : rows) {

			List<WebElement> rowStats = row.findElements(By.cssSelector("td"));
//			System.out.printf("elements in row: %d \n", rowStats.size());
			
			System.out.print(rowStats.get(2).getText() + " ");
			System.out.print(rowStats.get(1).getText() + " ");
			System.out.println(rowStats.get(3).getText());
		}
		
	}
	
	private static void getStatsFromDisciplinary(WebElement container, PrintWriter writer) {
		// TODO Auto-generated method stub
		
	}
	
}
