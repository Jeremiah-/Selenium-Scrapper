package SeleniumScrapper;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

public class playground {
	public static void main(String[] args) throws Exception {
		File jsonPlayers = new File("/u/jeremiah/playerInstagramWidgetURLs.txt"); // has 257
        Scanner lookAtFile = new Scanner(jsonPlayers);
        JSONObject playerNames = new JSONObject(lookAtFile.nextLine());
        Iterator players = playerNames.keys();
       while (players.hasNext()) {
    	   String key = (String) players.next();
//    	   System.out.println(key);
    	   System.out.println(playerNames.getString(key));
    	   if (playerNames.getString(key).isEmpty() || playerNames.getString(key).length() < 1) {
    		   System.out.println("EMPTY ENTRY");
    		   return;
    	   }
       }
       System.out.println("Good!");
//        PrintWriter writer = new PrintWriter("/u/jeremiah/playerInstagramWidgetURLs.json");
	}
}
