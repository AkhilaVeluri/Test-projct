package testPackage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;

public class CameraOrder {
	AndroidDriver<MobileElement> driver;



	@BeforeClass
	public void test() throws MalformedURLException {
	DesiredCapabilities cap = new DesiredCapabilities();
	cap.setCapability(MobileCapabilityType.PLATFORM_NAME,MobilePlatform.ANDROID);
	cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Andorid device");
	cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "25");
	
	cap.setCapability(
	"app",
	"C:\\Users\\xhdeployweb\\eclipse-workspace\\com.test\\xfinityhome-beta-release-9.40.1.9040001.apk");

	driver=new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"),cap);	
	driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

	}
	@Test
	public void AppLogin() throws InterruptedException, IOException {



	Thread.sleep(5000);
	       driver.findElement(By.id("user")).sendKeys("regression7");
	       Thread.sleep(3000);
	      
	       driver.findElement(By.id("passwd")).sendKeys("Applepie7");
	       Thread.sleep(3000);
	       driver.findElement(By.id("sign_in")).click();
	      
	       
	       


	}

}
