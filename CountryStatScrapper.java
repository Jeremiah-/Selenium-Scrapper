package testing;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


// TODO: prints "km", need to fix that for the json.
//TODO: for stats next to pie graph in all tabs, an extra space is added 
public class CountryStatScrapper {
	public static void main(String[] args) throws Exception{
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.fifa.com/worldcup/teams/index.html"); // countries
        PrintWriter writer = new PrintWriter("/u/jeremiah/CountryStatsUpdated.txt");
        
        // gets clickable links to countries on the bottom of the page
        List<WebElement> countries = driver.findElements(
        		By.xpath("//div[@class='team-qualifiedteams']//ul/li/a"));
        
        // the elements in countries expire, so get the information wanted before going to another page
        ArrayList<String> countryNames = new ArrayList<String>();
        for (int i = 0; i < countries.size(); i++) {
        	countryNames.add(countries.get(i).findElement
        			(By.cssSelector("span[class='team-name']")).getText());
        }
        assert(countryNames.size() == 32);
        
        ArrayList<String> countryURLs = new ArrayList<String>();
        for (int i = 0; i < countries.size(); i++) {
        	countryURLs.add(countries.get(i).getAttribute("href").toString());
        }
        assert(countryURLs.size() == 32);

        // this will print out a dictionary with country names as keys,
        // list of stats as values. The order of stats is:
        //  Attempts Off-Target, Shots saved, Shots blocked, Goals scored, Tournament average 42.9
        //  Tackles, Saves, Blocks
        //  Short Passes complete, Medium Passes complete, Long Passes complete, Tournament average
        System.out.print("{");
        writer.print("{");
        for (int i = 0; i < countries.size(); i++) {
        	
        	// make the country name as key.
        	System.out.print("\"" + countryNames.get(i) + "\" : [");
        	writer.print("\"" + countryNames.get(i) + "\":[");
        	
        	driver.get(countryURLs.get(i));
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	
        	// get the general stats under "statistics"
        	getGeneralStats(driver, writer);
        	
        	// have to gather the tab data (attack, defense, and passing are in different tabs)
        	// then click each one before we can see the elements
        	List<WebElement> tabs = driver.findElements(By.xpath("//div[@data-require='tabbedcontent']//li"));
        	assert(tabs.size() == 4);
        	
        	
        	// needs its own method, it's a special case
        	tabs.get(0).click();
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	getGoalFreq(driver, writer, "//div[@class='stats-scoring-freq']");
        	getStats(driver, writer, "//div[@class='stats-team-goalsscoring']");
        	
        	tabs.get(1).click();
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	List<WebElement> tables = driver.findElements(By.xpath("//div[@class='col-xs-6']//table[@class='table']"));
        	while (tables.size() < 1) {
        		tables = driver.findElements(By.xpath("//div[@class='col-xs-6']//table[@class='table']"));
        	}
        	getStats(driver, writer, "//div[@class='stats-team-attackingattempts']"); 
        	getTableStats(driver, writer, tables.get(0));
        	////////////////////////////////////////////////////////
        	
        	tabs.get(2).click();
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	tables = driver.findElements(By.xpath("//div[@class='col-xs-6']//table[@class='table']"));
        	while (tables.size() < 2) {
        		tables = driver.findElements(By.xpath("//div[@class='col-xs-6']//table[@class='table']"));
        	}
        	getStats(driver, writer, "//div[@class='stats-team-defendingsaves']"); 
        	getTableStats(driver, writer, tables.get(1));
        	///////////////////////////////////////////////////////////
        	
        	tabs.get(3).click();
        	driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        	tables = driver.findElements(By.xpath("//div[@class='col-xs-6']//table[@class='table']"));
        	while (tables.size() < 3) {
        		tables = driver.findElements(By.xpath("//div[@class='col-xs-6']//table[@class='table']"));
        	}
        	getStats(driver, writer, "//div[@class='stats-team-passingpasses']"); 
        	getTableStats(driver, writer, tables.get(2));
        	
        	System.out.println("], ");
        	writer.print("],");
        }
        System.out.println("}");
        writer.print("}");
        driver.close();
        writer.close();        
	}

	private static void getTableStats(WebDriver driver, PrintWriter writer, WebElement table) {
		List<WebElement> tableData = table.findElements(By.cssSelector("td"));
		assert (tableData.size() > 3);
		
		// want to skip first three: a space, "<country name>", "tournament average"
		for (int i = 3; i < tableData.size(); i++) {
			System.out.print(tableData.get(i).getText() + " "); // data name
			i++;
			System.out.print(tableData.get(i).getText() + ", "); // country data
			writer.print(tableData.get(i).getText() + ",");
			i++;
			System.out.println(tableData.get(i).getText() + ", "); // average data
			writer.print(tableData.get(i).getText() + ",");
		}
		
	}

	private static void getGoalFreq(WebDriver driver, PrintWriter writer, String xpath) {
		List<WebElement> data = driver.findElement(By.xpath(xpath)).findElements(By.cssSelector("span[class='label-num']"));
		assert(data.size() > 0);
		for (int i = 0; i < data.size(); i++) {
			System.out.print(data.get(i).getText() + ", ");
			writer.print(data.get(i).getText() + ",");
		}
	}

	private static void getGeneralStats(WebDriver driver, PrintWriter writer) {
		String xpath = "//div[@id='statistics']//table";
		WebElement generalStatsTable = driver.findElement(By.xpath(xpath));
		List<WebElement> dataNums = generalStatsTable.findElements(By.cssSelector("td"));
		List<WebElement> dataNames = generalStatsTable.findElements(By.cssSelector("th"));

		assert (dataNums.size() > 0 && dataNames.size() > 0);
		assert (dataNums.size() == dataNames.size());
		for (int i = 0; i < dataNames.size(); i++) {
			System.out.print(dataNames.get(i).getText() + " ");
			
			System.out.println(dataNums.get(i).getText() + ", ");
			writer.print(dataNums.get(i).getText() + ",");
		}
	}


	// method that gets a specific set of data from specific tabs
	private static void getStats(WebDriver driver, PrintWriter writer, String xpath) {
		
		// to get the data from the pie chart
		WebElement pieData = driver.findElement(By.xpath(xpath));
				
		System.out.print(pieData.findElement(By.cssSelector("span[class='chart-text']")).getText() + " ");
		System.out.print(pieData.findElement(By.cssSelector("span[class='chart-num']")).getText() + ", ");
		writer.print(pieData.findElement(By.cssSelector("span[class='chart-num']")).getText() + ", ");
		
		// to get data next to the pie chart
		List<WebElement> data = driver.findElements(By.xpath(xpath + "//li"));
		assert(data.size() > 0);
		for (int i = 0; i < data.size(); i++) {
			// easier to get span text with css selector
			String n = data.get(i).findElement(
					By.cssSelector("span[class='description']")).getText();
			
			String d = data.get(i).findElement(
					By.cssSelector("span[class='data']")).getText();
			System.out.print(n + " ");
			System.out.print(d + ", "); 
			writer.print(d + ",");
		
		}
	}
	
	
}
