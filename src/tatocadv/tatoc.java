package tatocadv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.net.HttpURLConnection;
import java.net.URL;



import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class tatoc {

	private static final String USER_AGENT = null;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, JSONException, IOException {
		// TODO Auto-generated method stub
		
		
		
		String downloadDir = System.getProperty("user.home") + "//Downloads";
	   	System.setProperty("webdriver.chrome.driver", downloadDir+"//chromedriver");

		WebDriver driver=new ChromeDriver();

		
		
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://10.0.1.86/tatoc/advanced/hover/menu");
//		driver.findElement(By.cssSelector("span.menuitem[onclick='gonext();']")).click();
		
		//locate the menu to hover over using its xpath
		WebElement menu = driver.findElement(By.cssSelector(".menutitle"));

		//Initiate mouse action using Actions class
		Actions builder = new Actions(driver);   

		// move the mouse to the earlier identified menu option
		builder.moveToElement(menu).build().perform();

		// wait for max of 5 seconds before proceeding. This allows sub menu appears properly before trying to click on it
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".menutop")));  // until this submenu is found

		//identify menu option from the resulting menu display and click
		WebElement menuOption = driver.findElement(By.cssSelector("span.menuitem[onclick='gonext();']"));
		menuOption.click();
		
		
		
		//Query to mysql database
		Thread.sleep(2000);
		
		 String dbUrl = "jdbc:mysql://10.0.1.86:3306/tatoc";
		 String name=null;
		 String passkey=null;
		 
		 try{
			 String symbol= driver.findElement(By.cssSelector("#symboldisplay")).getText();
			 ResultSet rs1=null;
		//Database Username
		 String username = "tatocuser";	
		 //Database Password
		 String password = "tatoc01";	
		  
		 //Query to Execute
		 String query1 = "select name, passkey from credentials c, identity i where i.symbol='"+symbol+"' and i.id=c.id;";	
		 	
		 
		 
		 
		 //Load mysql jdbc driver
		 Class.forName("com.mysql.jdbc.Driver");
		
		 //Create Connection to DB
		 Connection con = DriverManager.getConnection(dbUrl,username,password);
		 //Create Statement Object
		 Statement stmt = con.createStatement();
		 
		 // Execute the SQL Query. Store results in ResultSet
		 rs1= stmt.executeQuery(query1);
		 System.out.println("hello");
		 
		 
		 // While Loop to iterate through all data and print results
		 while (rs1.next()){
		 name = rs1.getString("name");	
		 passkey = rs1.getString("passkey");	
		 System.out.println(name+" "+passkey);	
		 }	
		 rs1.close();
		 stmt.close();
		
		 // closing DB Connection
		 con.close();	
		 }
		 catch (SQLException e) {
		        System.out.println("Connection Failed! Check output console");
		        e.printStackTrace();
		    	}
		 catch(Exception e)
			{
				e.printStackTrace();
			}
		driver.findElement(By.cssSelector("#name")).sendKeys(name);
		driver.findElement(By.cssSelector("#passkey")).sendKeys(passkey);
		driver.findElement(By.cssSelector("#submit")).click();
		
		
		//automate ooyala driver
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//play video
		
//		 Thread.sleep(1000);
//			//js.executeScript("document.getElementsByClassName('innerWrapper')[0].getElementsByTagName('object')[0].playMovie();");
//			Thread.sleep(35000);
//			driver.findElement(By.linkText("Proceed")).click();
			
			
			

			
		
		//REST application
		driver.get("http://10.0.1.86/tatoc/advanced/rest");
		
		String session=driver.findElement(By.cssSelector("#session_id")).getText();
		String sessionid=session.substring(12);
		System.out.println(sessionid);
		
		String link="http://10.0.1.86/tatoc/advanced/rest/service/token/";
		String url=link.concat(sessionid);
		driver.get(url);
		URL link2 = new URL(url);
		
		
		//json parsing
		
		//JSONObject obj = new JSONObject(" ");
		Scanner scan=new Scanner(link2.openStream());
		String str = new String();
	    while (scan.hasNext())
	        str += scan.nextLine();
	    scan.close();
		JSONObject obj = new JSONObject(str);
		String token = obj.getString("token");
		System.out.println(token);
		
		
		//POST 
		
		  URL url2 = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
			HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
		
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			String string= "id="+sessionid+"& signature="+token+"&allow_access=1";
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(string);
			wr.flush();
			wr.close();

			int responseCode = conn.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + string);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) 
			{
				response.append(inputLine);
			}
			in.close();
			
			//print result
			System.out.println(response.toString());
		
			conn.disconnect();
			driver.navigate().back();
			
			driver.findElement(By.cssSelector(".page a")).click();

			
			
			//download the file
			driver.findElement(By.linkText("Download File")).click();
			
			
			FirefoxProfile profile = new FirefoxProfile();
			WebDriver driver2 = new FirefoxDriver(profile);
		
			
			profile.setPreference("browser.helperapps.neverAsk.saveToDisk" ,"text/dat");
			
			String sign;
			BufferedReader br = new BufferedReader(new FileReader(downloadDir+"//file_handle_test.dat"));

			ArrayList<String> data= new ArrayList<String>();
			String sig  = "";
			while ((sign = br.readLine()) != null) 
			{
				sig = sign;
			}
			System.out.println(sig);
			System.out.println(sig.substring(11));
			 driver.findElement(By.cssSelector("#signature")).sendKeys(sig.substring(11));
		    driver.findElement(By.cssSelector(".submit")).click();
		    
}

}
