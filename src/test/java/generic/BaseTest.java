package generic;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest implements IAutoConst{
	
	public WebDriver driver;
	public WebDriverWait wait;
	public ExtentTest extentTest;
	
		
	@BeforeSuite
	public void createReport() {
		ExtentSparkReporter spark=new ExtentSparkReporter(REPORT_PATH);
		EXTENTREPORTS.attachReporter(spark); 
	}
	
	@AfterSuite
	public void publishReport() {
		EXTENTREPORTS.flush();	
	}
	
	@Parameters({"property"})
	@BeforeMethod
	public void openApp(Method testMethod,@Optional("config.properties") String property) {
		String configPath=ENV_FOLDER+property;
		
		String testName=testMethod.getName();
		extentTest = EXTENTREPORTS.createTest(testName);
		
		String gird = Utility.getProperty(configPath,"GRID");
		extentTest.log(Status.INFO, "Use Grid to Execute?"+gird);
		
		
		String browser = Utility.getProperty(configPath,"BROWSER");
		extentTest.log(Status.INFO, "Browser is:"+browser);
		
		
		if(gird.equalsIgnoreCase("YES"))
		{
			URL url=null;
			String gridURL=Utility.getProperty(configPath, "GRIDURL");
			try 
			{
			 url=new URL(gridURL);
			}
			catch (Exception e) {
				extentTest.log(Status.FAIL, e.getMessage());
			}
			
			DesiredCapabilities dc=new DesiredCapabilities();
			dc.setBrowserName(browser);
			
			driver = new RemoteWebDriver(url,dc);
		}
		else
		{
			if(browser.equals("chrome"))
			{
				WebDriverManager.chromedriver().setup();
				String path=System.getProperty(CHROME_KEY);
				extentTest.log(Status.INFO, "Set the path of driver exe:"+path);
				extentTest.log(Status.INFO, "Open the browser");
				driver=new ChromeDriver();
			}
			else
			{
				WebDriverManager.firefoxdriver().setup();
				String path=System.getProperty(GECKO_KEY);
				extentTest.log(Status.INFO, "Set the path of driver exe:"+path);
				extentTest.log(Status.INFO, "Open the browser");
				driver=new FirefoxDriver();
			}
		}
		
		
		
		String strITO = Utility.getProperty(configPath,"ITO");
		int iITO=Integer.parseInt(strITO);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(iITO));
		extentTest.log(Status.INFO, "Set the ITO:"+iITO);
		
		String strETO = Utility.getProperty(configPath,"ETO");
		int iETO=Integer.parseInt(strETO);
		wait=new WebDriverWait(driver, Duration.ofSeconds(iETO));
		extentTest.log(Status.INFO, "Set the ETO:"+iETO);
				
		String appURL = Utility.getProperty(configPath,"APPURL");
		driver.get(appURL);
		extentTest.log(Status.INFO, "Enter the URL:"+appURL);
	}
	
	@AfterMethod
	public void closeApp(ITestResult result)  {
		String testName=result.getName();
		int testStatus= result.getStatus();
		
		if(testStatus==1)
		{
			extentTest.log(Status.PASS, testName+" is pass");
		}
		else
		{
			TakesScreenshot t=(TakesScreenshot)driver;
			File srcFile = t.getScreenshotAs(OutputType.FILE);
			File dstFile = new File(SCREENSHOT_FOLDER+testName+IMAGE_FORMAT);
			try
			{
					FileUtils.copyFile(srcFile, dstFile);
			}
			catch (Exception e) {
				
					extentTest.log(Status.FAIL, e.getMessage());
			}
			extentTest.addScreenCaptureFromPath(SCREENSHOT_FOLDER_FOR_REPORT+testName+IMAGE_FORMAT);
			extentTest.log(Status.FAIL, testName+" test is Failed");
		}
		
		extentTest.log(Status.INFO, "Close the browser");
		driver.quit();
	}
}
