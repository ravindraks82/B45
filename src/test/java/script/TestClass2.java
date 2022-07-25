package script;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import generic.BaseTest;

public class TestClass2 extends BaseTest
{

	@Test
	public void testLogin() {
		
		extentTest.log(Status.INFO, "This is TestB");
		Assert.fail();
	}
}
