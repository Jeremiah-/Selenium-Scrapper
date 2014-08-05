package testing;

import java.io.PrintWriter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MatchScrapper {
	public static void main(String[] args) throws Exception{
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.fifa.com/worldcup/teams/index.html"); // countries
        PrintWriter writer = new PrintWriter("/u/jeremiah/MatchStats.txt");
	}
}
