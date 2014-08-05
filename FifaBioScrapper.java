package testing;

import java.io.PrintWriter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/*
 * THIS IS UNFINISHED
 */


public class FifaBioScrapper {
	public static void main(String[] args) throws Exception{
		
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.fifa.com/worldcup/groups/index.html"); // countries
        PrintWriter writer = new PrintWriter("/u/jeremiah/playerBios.txt");
        
		// need to find letters A-Z on base page
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < abc.length(); i++) {
			String xpath = "//li[@id='letter-" + abc.charAt(i) + "']/div[1]"; // works
			// tried finding by xpath but I think the whitespace is screwing it up. Works for "count", which is in the same area
//			String xpath = "//li[@id='letter-" + abc.charAt(i) + "']/div[@class='letter${nbsp}selected']"; 
			
			// go back to base page with letter selection
			driver.get("http://www.fifa.com/worldcup/players/browser/index.html");
			
			// get the letter page we want to go to and click.
			WebElement letter = driver.findElement(By.xpath(xpath));
			letter.click();
//			System.out.println(letter.getText());
			
//			List<WebElement> players = driver.findElements(By.xpath("//span[@class='p-n-webname']")); //gets the span
			List<WebElement> players = driver.findElements(By.xpath("//li//div[@class='p-n']/a")); // GETS PLAYER LINKS
			System.out.printf("%d \n", players.size());
			for (int index = 0; index < players.size(); index++) {
				players.get(index).click();
				// CURRENTLY BREAKS HERE. CAN CLICK ON PLAYER NAME THOUGH :)
				
				System.out.println(players.get(index).toString());
			}
		} 
		driver.close();
        
        
			// need to go through each player (Eduardo is going to fuck things up)
				// inside player, need to know what name to use in dictionary
				// (use the name on the webpage) (manually enter the eduargo missing)
				// grab bio info
		
	}
}
