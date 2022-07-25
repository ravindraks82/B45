package script;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import generic.BaseTest;

public class TestClass1 extends BaseTest
{

	@Test
	public void testA() {
              		
		extentTest.log(Status.INFO, "This is TestA");
	}
}
