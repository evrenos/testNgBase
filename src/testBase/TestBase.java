/**
 * 
 */
/**
 * @author evrenos
 *
 */
package testBase;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestBase {
	
	String filename;
    protected WebDriver user = null;
    protected WebDriverWait wait = null;
    	
    @BeforeMethod
    public void setUp() throws MalformedURLException, FileNotFoundException {
    /*
     * web driver executable path is set.
	 */
    	String path = System.getProperty("user.dir");
    	System.setProperty("webdriver.chrome.driver",path+"/chromedriver.exe");
    	//System.setProperty("webdriver.gecko.driver",path+"/geckodriver.exe");		
    	//System.setProperty("webdriver.ie.driver",path+"/MicrosoftWebDriver.exe");
    	user = new ChromeDriver();
    	user.manage().window().maximize();
    	wait = new WebDriverWait(user, 30);
    }
    @AfterMethod
    public void closeBrowser() throws IOException {
    	takeScreenshots();
        getDriver().quit();
    }
    @Test (enabled = false)
    public void testTrial() throws InterruptedException {
    	getDriver().navigate().to("https://www.google.com");
    }
    /**
     * getter for driver to be accessible from specific test classes.
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return user;
    }
    /*
     * customized TestBase WebDriver
     */
    public WebDriver opens(String url , By identifier) {
    	getDriver().get(url);
		waitFor(identifier);
		return getDriver();
	}
    public WebDriver clickById(String id , By identifier) {
		WebElement button = (new WebDriverWait(getDriver(), 30)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(id)));
		button.click();
		Assert.assertTrue(waitFor(identifier));
		return getDriver();
	}
	/**
	 * clicks on a linked text.
	 *
	 * @param linkedText text of clickable element
	 * @param identifier element to check if driver landed on correct web page after click
	 * @return the case WebDriver
	 */
	public WebDriver clickByLnkText(String linkedText , By identifier) {
		WebElement button = (new WebDriverWait(getDriver(), 30)).until(ExpectedConditions.presenceOfElementLocated(By.linkText(linkedText)));
		button.click();
		Assert.assertTrue(waitFor(identifier));
		return getDriver();
	}
	
	/**
	 * clicks on an element via Xpath.
	 *
	 * @param xpath xpath of the element
	 * @param identifier element to validate page after click.
	 * @return the case WebDriver
	 */
	public WebDriver clickByXpath(String xpath , By identifier){
		WebElement button = (new WebDriverWait(getDriver(), 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		button.click();
		Assert.assertTrue(waitFor(identifier));
		return getDriver();
	}
	/**
	 * clicks on an element via clickIdentifier and waits for validation identifier.
	 * 
	 * @param cIdentifier
	 * @param identifier
	 * @return
	 */
	public WebDriver clickByBy(By cIdentifier , By identifier){
		WebElement button = (new WebDriverWait(getDriver(), 30)).until(ExpectedConditions.presenceOfElementLocated(cIdentifier));
		button.click();
		Assert.assertTrue(waitFor(identifier));
		return getDriver();
	}
	/**
	 * 
	 * @param xpath
	 * @return
	 */
	public WebDriver clickXpath(String xpath){
		WebElement button = (new WebDriverWait(getDriver(), 30)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		button.click();
		//Assert.assertTrue(wait.waitFor(identifier));
		return getDriver();
	}
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public WebDriver clickByIdentifier(By identifier){
		WebElement button = (new WebDriverWait(getDriver(), 30)).until(ExpectedConditions.presenceOfElementLocated(identifier));
		button.click();
		//Assert.assertTrue(wait.waitFor(identifier));
		return getDriver();
	}
	/**
	 * 
	 * @param xpath
	 * @param valueIdentifier
	 * @return
	 * @throws InterruptedException
	 */
	public WebDriver dropdownClickSelect(String xpath, By valueIdentifier ) throws InterruptedException {
		clickXpath(xpath);
		Thread.sleep(2000);
		clickByIdentifier(valueIdentifier);
		Thread.sleep(2000);
		return getDriver();
	}
	public WebDriver switchToWindow(String expectedTitle , By identifier) {
	    Set<String> handles = getDriver().getWindowHandles();
	    System.out.println("Number of windows found: " + handles.size());
	    for (String handle : handles) {
	        System.out.println("Working on window with handle: " + handle);
	        getDriver().switchTo().window(handle);
	        System.out.println("Switched to window with handle: " + handle);
	        String currentWindowTitle = getDriver().getTitle().trim();
	        System.out.println("Current window page title: " + currentWindowTitle);
	        if(currentWindowTitle.toLowerCase().contains(expectedTitle.toLowerCase())){
	            System.out.println("Window found (" + expectedTitle + ")");
	            waitFor(identifier);
	            break;
	        }
	        else{
	            System.out.println("-- This is not the window with title we are looking for");
	        }
	    }
	    return getDriver();
	}
	/**
	 * 
	 * @param identifier
	 * @param keys
	 * @return current driver
	 * @throws InterruptedException
	 */
	public WebDriver sendKeysByBy(By identifier , String keys) throws InterruptedException {
		WebElement element = getDriver().findElement(identifier);
		element.clear();
		Thread.sleep(100);
		element.sendKeys(keys);
		return getDriver();
	}
	/**
	 * send keys to an element via elementID.
	 *
	 * @param id Id of the element
	 * @param keys Input to send
	 * @return the case WebDriver
	 * @throws InterruptedException 
	 */
	public WebDriver sendKeysById(String id , String keys) throws InterruptedException {
		WebElement element = getDriver().findElement(By.id(id));
		element.clear();
		Thread.sleep(100);
		element.sendKeys(keys);
		return getDriver();
	}
	
	/**
	 * send keys to an element via elementName.
	 * @param name
	 * @param keys
	 * @return
	 * @throws InterruptedException 
	 */
	public WebDriver sendKeysByName(String name , String keys) throws InterruptedException {
		WebElement element = getDriver().findElement(By.name(name));
		element.clear();
		Thread.sleep(100);
		element.sendKeys(keys);
		return getDriver();
	}
	
	/**
	 * send keys to element via linkedText.
	 *
	 * @param lnktext linked Text of element
	 * @param keys Input to send
	 * @return the case WebDriver
	 * @throws InterruptedException 
	 */
	public WebDriver sendKeysByLnkText(String lnktext , String keys) throws InterruptedException {
		WebElement element = getDriver().findElement(By.linkText(lnktext));
		element.clear();
		Thread.sleep(100);
		element.sendKeys(keys);
		return getDriver();
	}
	
	/**
	 * send keys to element via element Xpath.
	 *
	 * @param xpath Xpath of the element
	 * @param keys Input to send
	 * @return the case WebDriver
	 * @throws InterruptedException 
	 */
	public WebDriver sendKeysByXpath(String xpath , String keys) throws InterruptedException {
		WebElement element = getDriver().findElement(By.xpath(xpath));
		element.clear();
		Thread.sleep(100);
		element.sendKeys(keys);
		return getDriver();
	}
	public WebDriver dropdownSelect(By byX , String value) {
		Select elm = new Select(getDriver().findElement(byX));
		//Thread.sleep(100);
		elm.selectByValue(value);
		return getDriver();
		}
		//string generate WebDriver
		/**
		 * function to Generate Random unique Emails.
		 *
		 * @param length number of characters for Email name
		 * @return the string
		 */
		public String generateTestEmail(int length) {
		    String allowedChars="abcdefghijklmnopqrstuvwxyz" +   
		            "1234567890";   
		    String email="";
		    String temp=RandomStringUtils.random(length,allowedChars);
		    email=temp.substring(0,temp.length()-9)+"@ovidos.com";
		    return email;   
		}
		
		public double generateRandomDoubleNumber( double min, double max )
		{
		  double diff = max - min;
		  return min + Math.random( ) * diff;
		}
		/**
		 * function to generate random strings.
		 *
		 * @param length number of characters for string
		 * @return the string
		 */
		public String generateTestAlphaNumericStrings(int length){
		    return RandomStringUtils.randomAlphanumeric(length);
		}
		
		/**
		 * 
		 * function to scroll page by pixel
		 * @param pxl
		 * @return
		 */
		public WebDriver scrollDown(String pxl){
			JavascriptExecutor jse2 = (JavascriptExecutor)getDriver();  
			jse2.executeScript("window.scrollBy(0,"+pxl+")", "");
			return getDriver();
		}
		
		/**
		 * generates random Phone No
		 * @return
		 */
		public String generateRandomPhoneNo() {
			    String allowedChars="1234567890";   
			    String phoneNo="";
			    String temp=RandomStringUtils.random(18,allowedChars);
			    phoneNo="05"+temp.substring(0,temp.length()-9);
			    return phoneNo;   
		}
		/**
		 * document upload windowsOS handler
		 * @param file_name
		 * @return
		 * @throws InterruptedException
		 * @throws AWTException
		 */
		public WebDriver robotCall(String file_name) throws InterruptedException, AWTException {
			 StringSelection stringSelection = new StringSelection(file_name);
			 Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null); 
			 //Robot Created for Keyboard inputs in order to handle FileUpload Window
			 Robot robot = new Robot();
			 robot.keyPress(KeyEvent.VK_CONTROL);
			 robot.keyPress(KeyEvent.VK_V);
			 robot.keyRelease(KeyEvent.VK_V);
			 robot.keyRelease(KeyEvent.VK_CONTROL);
			 Thread.sleep(1000);
			 robot.keyPress(KeyEvent.VK_ENTER);
			 robot.keyRelease(KeyEvent.VK_ENTER);
			 //System.out.println("Enter Pressed In order to Upload "+file_name); //file selected to upload
			return getDriver();
		}
		/**
		 *  press enter for windowsOS windows
		 * @return
		 * @throws Exception
		 */
		public WebDriver robotPressEnter () throws Exception {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			return getDriver();
		}
		/**
		 * wait for a specific element manually after a dynamic change on the browser page
		 * @param locator
		 * @return
		 */
    public boolean waitFor(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
	}
    /**
     * Takes screenshot of currently displayed page
     * @throws IOException
     */
    public void takeScreenshots() throws IOException {
    	 DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy h-m-s");
	     Date date = new Date();
	     File scrFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
	     filename="case_result";
	     FileUtils.copyFile(scrFile, new File("C:\\AutomationScreenshots\\"+filename+"-"+dateFormat.format(date)+".png"));
    }
}