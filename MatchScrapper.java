package SeleniumScrapper;

import java.io.PrintWriter;
import java.util.List;

import org.openqa.selenium.By;
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
      
        int startDate = 20140612;
        List<WebElement> containerOfAllMatches = driver.findElements(By.xpath("//div[@class='matches']//a[@class='mu-m-ink']"));
        for (int i = startDate; i < 20140713; i++) {
        	
        }
        // get link to the statistics
        
        // arrive to statistics page.
	}
}
