package com.test1.tests;

import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.test1.pages.DefaultLandingPage;
import com.test1.util.DataDrivenHelper;
import com.test1.util.WebDriverHelper;

public class TestBase 
{
	
	//Change Private WebDriver driver to protected so other test objects can inherit it
	//private WebDriver driver;
	protected WebDriver driver;
	//need to make the entry point a member variable below like this
	protected DefaultLandingPage defaultlandingpage;
	//need to make Properties testConfig member variable below like this
	protected Properties testConfig;
	public String baseUrl;
		
	
	
	@BeforeSuite()
	public void beforSuite() throws FileNotFoundException, IOException
	{
		testConfig = new Properties();
		testConfig.load(new FileInputStream("TestConfig.properties"));
	}
	
	@BeforeMethod
	  public void beforeMethod() 
	  {
		
		//the below 3 are handled by the WebDriverHelper
		
//		//To launch on google Chrome
//		System.setProperty("webdriver.chrome.driver",
//		"C:\\seleniumJar\\chromedriver_win32\\chromedriver.exe");
		
		//	change the below since we are getting web choice from WebDriverHelper		
		//driver = new ChromeDriver();  //testConfig.getProperty("browser")
		
		driver = WebDriverHelper.createDriver(testConfig.getProperty("browser"));  
		
		// the below baseUrl value is coming from the Java PropertiesFile
		baseUrl = testConfig.getProperty("baseUrl");
				
//    the implicit wait and screen enlargement are taken care of in Java PropertiesFile		
//		//Set implicityWait time to 45 seconds
//		driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
//		
//		//Enlarge the application screen
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		int Width = (int) toolkit.getScreenSize().getWidth();
//		int Height = (int)toolkit.getScreenSize().getHeight();
//		driver.manage().window().setSize(new Dimension(1800,1600));
		
		driver.get(baseUrl);
		
		//adding the entry point for all test DefaultLandingPage defaultlandingpage = new DefaultLandingPage(driver) so this can be inherited
		//1 DefaultLandingPage defaultlandingpage = new DefaultLandingPage(driver);
		//2 now DefaultLandingPage can be removed below because this has been added as protected above
		defaultlandingpage = new DefaultLandingPage(driver);
				
	  }
	
	 @DataProvider()
	  public Object[][] dataProvider(Method method)
	  {

		 DataDrivenHelper ddh = new DataDrivenHelper(testConfig.getProperty("mastertestdatafile"));
			
		Object[][] testData = ddh.getTestCaseDataSets(testConfig.getProperty("testdatasheet"), method.getName());
		
		return testData;
		
	  }
	  
	
	 @AfterMethod
	  public void afterMethod() 
	  {
		 //since we are inheriting from WebDriverHelper to close or tear down we need the below
		 WebDriverHelper.quitDriver(driver);
		 // driver.close();
	  }
	
}
