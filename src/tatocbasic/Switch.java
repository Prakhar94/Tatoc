package tatocbasic;

import java.awt.List;

import org.apache.jasper.tagplugins.jstl.core.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class Switch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String str1, str2, str3, str4;
		
		WebDriver driver= new FirefoxDriver();
		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
		driver.findElement(By.cssSelector("div.greenbox")).click();
		
//		if(driver.findElement(By.cssSelector("div.redbox")) != null)
//			driver.get("http://10.0.1.86/tatoc/error");
		
		
		// switching between frames
		driver.switchTo().frame(driver.findElement(By.id("main")));
		
		
		str1= driver.findElement(By.cssSelector("div#answer")).getAttribute("class");
		
		driver.switchTo().frame(driver.findElement(By.id("child")));
		
		str2= driver.findElement(By.cssSelector("div#answer")).getAttribute("class");
		driver.switchTo().defaultContent();
		
		
		
		

		
		while(!str1.equals(str2)){
			driver.switchTo().frame(driver.findElement(By.id("main")));
			
			driver.findElement(By.linkText("Repaint Box 2")).click();
			
			
			driver.switchTo().frame(driver.findElement(By.id("child")));
			
			str2= driver.findElement(By.cssSelector("div#answer")).getAttribute("class");
			driver.switchTo().defaultContent();
			
			
		}
		driver.switchTo().frame(driver.findElement(By.id("main")));
		driver.findElement(By.linkText("Proceed")).click();
		driver.switchTo().defaultContent();
		
		
		
		//dragbox module
		
		WebElement Sourcelocator = driver.findElement(By.cssSelector(".ui-draggable"));
		WebElement Destinationlocator = driver.findElement(By.cssSelector("div #dropbox"));
		dragAndDrop(Sourcelocator,Destinationlocator, driver);
		
		
		driver.findElement(By.linkText("Proceed")).click();
		
		//popup
		
		
		
		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();
		// Perform the click operation that opens new window
		driver.findElement(By.linkText("Launch Popup Window")).click();
		
		// Switch to new window opened
		for(String winHandle : driver.getWindowHandles()){
		    driver.switchTo().window(winHandle);
		  
		}

		// Perform the actions on new window
		driver.findElement(By.cssSelector("input#name")).sendKeys("hey!!");
 		 driver.findElement(By.cssSelector("input#submit")).click();

		

 		 // Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);

		// Continue with original browser (first window)
		driver.findElement(By.linkText("Proceed")).click();
		
		
		
		
		
		// generating cookies
		
		driver.findElement(By.linkText("Generate Token")).click();
		
		str3=driver.findElement(By.cssSelector("span#token")).getText();
		str4= str3.substring(7);
		
		Cookie n=new Cookie("Token", str4);
		driver.manage().addCookie(n);
		
		driver.findElement(By.linkText("Proceed")).click();
		
		
		
			

	}

	private static void dragAndDrop(WebElement sourceElement, WebElement destinationElement, WebDriver driver) {
		// TODO Auto-generated method stub
		if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
			Actions action = new Actions(driver);
			action.dragAndDrop(sourceElement, destinationElement).build().perform();
		}
		
	}
	
}


