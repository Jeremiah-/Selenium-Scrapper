package SeleniumScrapper;

import java.io.PrintWriter;
import java.util.ArrayList;
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
        
//        WebElement page;
        for (int i = 0; i < containerOfAllMatches.size(); i++) {
        	driver.get(containerOfAllMatches.get(i));
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	
        	WebElement page = driver.findElement(// By.id("mitem-statistics"));
        			By.xpath("//div[@class='nav-wrap']//li[@id='mitem-statistics']//a[@data-ref='#statistics']")); // had //a at the end

        	page.click();
        }
//        driver.close();
	}
}
