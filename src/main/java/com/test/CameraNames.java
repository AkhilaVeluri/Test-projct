package com.test;

public class CameraNames {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
package Pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.DocFlavor.STRING;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AndroidFindBys;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.qameta.allure.Step;
import utility.Hook;
import utility.XhomeUtils;

public class OverviewPage extends Hook {

OverView_PageObjects overviewPage;
Camera_PageObjects cameraPage;
BasePage basePage;
WebDriver driver;
//SettingsPage settingsPage = new SettingsPage(driver);
public OverviewPage(WebDriver driver) {
	this.driver = driver;
	overviewPage = new OverView_PageObjects();
	// PageFactory.initElements(new AppiumFieldDecorator(getDriver()),overviewPage);
	PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS), overviewPage);
}

public void handlePopups() {
	waitTime(15);
	dismissCimaPswdRecoveryScreen();
	waitTime(25);
	if (XhomeUtils.platform.equals("iOS")) {
		changeContext("Native");
		try {
			if(overviewPage.close_button.isDisplayed()) {
				overviewPage.close_button.click();
			} else {
				System.out.println("no close button");
			}
		} catch(Exception e) {
			System.out.println("no close button");
		}
	}
	
	dismiss_fingerprint_popup();
	dismiss_update_alert();
	dismiss_24_7_cvr_onboarding_screen();
	if (XhomeUtils.platform.equals("iOS")) {
		changeContext("Native");
		System.out.println("Handle pop-ups");
		dismiss_push_notification();
		dismiss_add_to_contacts();
	}
}

@Step("Step to dismiss CIMA password recovery screen")
public void dismissCimaPswdRecoveryScreen() {
	try {
		if(overviewPage.cimaSupportScreen.isDisplayed()) {
			System.out.println("CIMA Password Recovery Screen Displayed");
			scrollToElement(overviewPage.askMeLaterButton);
			overviewPage.askMeLaterButton.click();
		} else {
			System.out.println("CIMA Password recovery screen not displayed");
		}
		
	} catch(Exception e) {
		System.out.println("CIMA Password recovery screen not displayed");
	}
}

//1. update -> push_notifications->finger print primer->add contacts
//2. 24/7 CVR  onboarding screen can come anytime after fresh Installation
public void validate_popups(String popup) {
	waitTime(20);
	if (XhomeUtils.platform.equals("iOS")) {
		changeContext("Native");}
	int i=0;
	switch(popup)
	{
		case "close":
			while(i<5) {
			if(isElementPresent(overviewPage.close_button))
			{
				System.out.println("close button available for 24/7 popup");	
				break;
			}
			dismiss_push_notification();
			dismiss_add_to_contacts();
			i++;
			if(i==5 && !isElementPresent(overviewPage.close_button)) {
				Assert.fail("unable to find close button");
			}
			}
			break;
			
			
		case "push_notification":
			while(i<5) {
			if(isElementPresent(overviewPage.push_notification_screen))
			{
				System.out.println("push notification popup avaialble");
				break;
			}
			try {
				if(overviewPage.close_button.isDisplayed())
					overviewPage.close_button.click();}
				catch(Exception e) {
					System.out.println("no close button");
				}
			dismiss_add_to_contacts();
			i++;
			if(i==5 && !isElementPresent(overviewPage.push_notification_screen))
			{
				Assert.fail("No push_notification screen is available");
			}}
			break;
			
		default:
				System.out.println("enter a valid screen to be verified");
				
		}
	
		}

		

@Step("Validate appearance of Overview screen")
public boolean validateOverviewScreen() {
	boolean element = false;

	handlePopups();
	
	try {
		waitTime(15);
		if (overviewPage.overViewScreen.isDisplayed()) {
			element = true;
		} else {
			element = false;
			Assert.fail("Inside false block, overview screen not dispalyed");
		}
	} catch (Exception e) {
		Assert.fail("Overview screen not displayed");
	}
	waitTime(2);
	return element;
}

@Step("Validate account is BB Offline")
public boolean validateBBOfflineScreen() {
	boolean element = false;
	String bbOfflineText = "Your Touchscreen is not connected to WiFi.";
	try {
		waitTime(5);
		if (overviewPage.bbOfflineTitle.isDisplayed()) {
			if(overviewPage.bbOfflineTitle.getText().equalsIgnoreCase(bbOfflineText)) {
				element = true;
			} else {
				Assert.fail("Account is not BB Offline");
			}
		}
	} catch (Exception e) {
		Assert.fail("Account is not BB Offline");
	}
	return element;
}

@Step("Validate appearance of Internet Overview screen")
public boolean validateInternetOverviewScreen() {
	boolean element = false;

	// Handle Pop-ups
	handlePopups();

	// With the changed UI we can directly use this as Internet user Overview screen validation
	try {
		waitTime(5);
		if (overviewPage.internet_validation.isDisplayed()) {
			element = true;
		}
		else
		{
			Assert.fail("Inside false block, not an internet account");	
		}
	}
	catch(Exception e) {
		Assert.fail("not an internet account");
	}
	
	return element;
}

@Step("Validate Overview screen for a control user - Mode, activity, Alert and Automation Icon presence")
public boolean validateControlOverviewScreen() {
	boolean element = false;
	handlePopups();
	if (XhomeUtils.platform.equals("iOS")) {
		try {
			waitTime(5);
			if(overviewPage.control_validation.isDisplayed()) {
				System.out.println("Its a Contrl user");
				element = true;
			} else {
				Assert.fail("not a control user ");
			}
		} catch(Exception e)
		{
			Assert.fail("not a control user ");
		}
			
	}
	else {
	if (overviewPage.controlModeStatus.isDisplayed()) {
		if (overviewPage.activity_icon.isDisplayed()) {
			if (overviewPage.alert_icon.isDisplayed()) {
				if (overviewPage.automation_icon.isDisplayed()) {
					element = true;
				}
			}
		}
	} 
	}
	
	return element;
}
public void dismiss_fingerprint_popup() {
	try {
		
			waitTime(5);
			if (overviewPage.fingerprint_tagline.isDisplayed()) {
				overviewPage.ignore_fingerprint.click();
			}
			else
			{
				System.out.println("fingerprint popuup not available to handle");
			}
	} catch (Exception e) {
		System.out.println("fingerprint popuup not available to handle");
	}
}

public void dismiss_update_alert() {
	try {
		//if (overviewPage.updateAlert.isDisplayed()) {
			waitTime(15);
			overviewPage.ignoreUpdate.click();
		//}
	} catch (Exception e) {

	}
}

public void dismiss_24_7_cvr_onboarding_screen() {
	try {
		if (overviewPage.cvr_onboarding_24_7.isDisplayed()) {
			overviewPage.cvr_onboarding_back_button.click();
		}
		else
		{
			System.out.println("cvr on boarding primer not available to handle");
		}
	} catch (Exception e) {
		System.out.println("cvr on boarding primernot available to handle");
	}
}

public void dismiss_auth4all_popup() {
	try {
		if (overviewPage.auth4allPopup.isDisplayed()) {
			overviewPage.auth4allPopup.click();
		}
		else
		{
			System.out.println("auth4all popuup not available to handle");
		}
	} catch (Exception e) {
		System.out.println("auth4all popuup not available to handle");
	}
}

public void dismiss_push_notification() {
	try {
		if (overviewPage.push_notification_screen.isDisplayed()) {
			overviewPage.continue_button.click();
			waitTime(25);
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.alertIsPresent());
			getDriver().switchTo().alert().accept();
		}
		else
		{
			System.out.println("push notification popuup not available to handle");
		}
	} catch (Exception e) {
		System.out.println("push notification popuup not available to handle");
	}
}

public void dismiss_add_to_contacts() {
	try {
		if (overviewPage.add_to_contacts_screen.isDisplayed()) {
			overviewPage.continue_button.click();
			waitTime(25);
			WebDriverWait wait = new WebDriverWait(getDriver(), 30);
			wait.until(ExpectedConditions.alertIsPresent());
			getDriver().switchTo().alert().accept();
		}
		else
		{
			System.out.println("add contact primer not available to handle");
		}
	} catch (Exception e) {
		System.out.println("add contact primer not available to handle");
	}
}

public void handle_crash() {
	try {
		waitTime(20);
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);
		wait.until(ExpectedConditions.alertIsPresent());
		getDriver().switchTo().alert();
		if (overviewPage.crash_header.isDisplayed()) {
			overviewPage.send_report_button.click();
			System.out.println("crash occured and report sent");
		}
		else
		{
			System.out.println("Crash header not available,not sure of what alert it was");
		}
	} catch (Exception e) {
		System.out.println("Crash header not available, not sure of what alrt it was");
	}
}

@Step("Click on screen icons on Overview Screen")
public void click_screen_icon(String screen) {
	basePage = new BasePage();
	try {
		switch (screen) {
		case "Activity":
			if (overviewPage.activity_icon.isDisplayed())
					overviewPage.activity_icon.click();
			else
				Assert.fail("Inside false block"+screen+" element not avaialble");
			break;
		case "Automation":
			if (overviewPage.automation_icon.isDisplayed()) 
				overviewPage.automation_icon.click();
			else
				Assert.fail("Inside false block"+screen+" element not avaialble");
			waitTime(10);
			break;
		case "Alert":
			try {
				if (overviewPage.alert_icon.isDisplayed())
						overviewPage.alert_icon.click();
				else 
					Assert.fail("Alert Icon not present");
			} catch(Exception e) {
				Assert.fail("Alert Icon not present");
			}
			break;
		case "More":
			if (overviewPage.more_icon.isDisplayed()) {
					overviewPage.more_icon.click();
			}else {
				Assert.fail("Inside false block, more Icon not present");
			}
			break;
		case "Zone and Sensors status":
			if (overviewPage.sensors_status.isDisplayed())
				overviewPage.sensors_status.click();
			else
				Assert.fail("Inside false block"+screen+" element not avaialble");
			break;
		case "Feedback Card":
			 if (scrollToElement(overviewPage.feedback_card))
					overviewPage.feedback_card.click();
			 else
				 Assert.fail("Inside false block"+screen+" element not avaialble");
			break;
		case "Camera":
			if (scrollToElement(overviewPage.camera_thumbnail))
				overviewPage.camera_thumbnail.click();
			break;
		case "internet_Camera":
			if (scrollToElement(overviewPage.internet_camera_thumbnail))
				overviewPage.internet_camera_thumbnail.click();	
			break;
		case "Add a Device":
			if (scrollToElement(overviewPage.add_a_device))
				overviewPage.add_a_device.click();
			break;
		case "Security Sensors":
			if (scrollToElement(overviewPage.security_sensors))
					overviewPage.security_sensors.click();
			break;
		case "Hazard Detectors":
			if (scrollToElement(overviewPage.hazard_detectors))
					overviewPage.hazard_detectors.click();
			break;
		case "Latest Activity":
			if (scrollToElement(overviewPage.latest_activity_on_overview))
				overviewPage.latest_activity_on_overview.click();
			break;

		case "Lights":
			if (XhomeUtils.platform.equals("iOS")){
				if (scrollToElement(overviewPage.lights_section))
					overviewPage.lights_section.click();
				else if (scrollToElement(overviewPage.lightTile))
					overviewPage.lightTile.click();
			} else {
				 if (scrollToElement(overviewPage.lightTile)) {
					 overviewPage.lightTile.click();
				 }
			}
			break;
		case "Thermostat":
		getDriver().getPageSource();
			scrollDown();
		 if (scrollToElement(overviewPage.thermostatTile))
				overviewPage.thermostatTile.click();
			break;
		case "iCam":
		 if (scrollToElement(overviewPage.recCameraThumbNail))
			 	overviewPage.recCameraThumbNail.click();
		 break;
		default:
			System.out.println("enter a valid screen to navigate");
		}
	} catch (Exception e) {
		Assert.fail(screen+" element not avaialble");
	}

}

@Step("Verify I am on {0} screen")
public void verify_on_screen(String screen) {
	basePage = new BasePage();
	try {
		switch (screen) {
		case "Activity":
			if (overviewPage.activity_title.isDisplayed())
				System.out.println("Activity Screen is Displayed");
			else
				Assert.fail("Inside false block,not on"+screen+"screen");
			break;
		case "Automation":
			waitTime(30);
			//if (XhomeUtils.platform.equals("iOS"))
			//	changeContext("WebView");
			if (overviewPage.automation_title.isDisplayed())
				System.out.println("Automation Screen is Displayed");
			else
				Assert.fail("Inside false block,not on"+screen+"screen");
			//				if (XhomeUtils.platform.equals("iOS"))
			//					changeContext("Native");
			break;
		case "Alert":
			if (overviewPage.alert_title.isDisplayed())
				System.out.println("Alert Screen is Displayed");
			if(XhomeUtils.platform.equalsIgnoreCase("iOS")) {
				exit_alert_screen();
			}
			break;
		case "More":
			waitTime(15);
			if (overviewPage.more_title.isDisplayed())
				System.out.println("More Screen is Displayed");
			else{
				Assert.fail("More Screen is not Displayed");
			}
			break;
		case "Zone and Sensors status":
			if (overviewPage.sensors_title.isDisplayed())
				System.out.println("Zones & Sensors Screen is Displayed");
			else
				Assert.fail("Inside false block,not on"+screen+"screen");
			break;
		case "Feedback Card":
			waitTime(6);
			if (overviewPage.feedback_title.isDisplayed())
				System.out.println("Feedback Screen is Displayed");
			else
				Assert.fail("Inside false block,not on"+screen+"screen");
			break;
		case "Camera":
			if (XhomeUtils.platform.equals("iOS"))
				if(cameraPage.Take_Picture_Button.isDisplayed() || cameraPage.LiveRecording.isDisplayed() || cameraPage.MotionEvent1.isDisplayed() || cameraPage.NoEvents.isDisplayed())
					System.out.println("Camera Screen is Displayed");
				else if (overviewPage.camera_video_details.isDisplayed())
					System.out.println("Camera Screen is Displayed");
				else
					Assert.fail("Inside false block,not on"+screen+"screen");
			break;
		case "Add a Device":
			if (overviewPage.add_a_device_title.isDisplayed())
				System.out.println("Add a Device Screen is Displayed");
			else
			{
				Assert.fail("Inside false block,not on"+screen+"screen");
			}
			break;
		case "Security Sensors":
			try {
				if (overviewPage.security_sensors_title.isDisplayed())
					System.out.println("Security Snesors Screen is Displayed");
				else
					Assert.fail("Security Sensors not displayed");
			} catch(Exception e) {
				Assert.fail("Security Sensors not displayed");
			}
			break;
		case "Hazard Detectors":
			if (overviewPage.hazard_detectors_title.isDisplayed())
				System.out.println("Hazard Detectors Screen is Displayed");
			else
			{
				Assert.fail("Inside false block,not on"+screen+"screen");
			}
			break;
		case "Lights":
			waitTime(5);
			if (overviewPage.LightsTitle.isDisplayed()) {
				System.out.println("Lights Screen is Displayed");
			}
			else
			{
				Assert.fail("Inside false block,not on"+screen+"screen");
			}
			break;

		default:
			System.out.println("enter a valid screen to validate");
		}
	} catch (Exception e) {
		//e.printStackTrace();
		Assert.fail("not on "+screen+" screen");
	}

}

@Step(" Step tp exit alert scren ")
public void exit_alert_screen()
{
	try {
		if(overviewPage.alertExitButton.isDisplayed()) {
			// Cancel exit button
			overviewPage.alertExitButton.click();
		}else {
			Assert.fail("Inside false block, alert screen is not displayed");
		}
	} catch(Exception e) {
		Assert.fail("Unable to exit Alert screen");
	}
}


@Step("Step to scroll to Upsell Tile on Overview Screen")
public void scrollToUpsellTile() {
	if(scrollToElement(overviewPage.upsellTile)) {
		System.out.println("Scrolled to upsell screen");
	}	
}

@Step("validate the existence of carousal tile")
public void verify_carousal()
{
	scrollToUpsellTile();
	if(isElementPresent(overviewPage.upsellTile))
	{
		System.out.println("carousal tile available");
	}
	else
	{
		Assert.fail("carousal not availble");
	}
}

@Step("Step to validate if specific Upsell Screen is displayed")
public void findUpsellScreen(String upsellScreen) {
	switch(upsellScreen) {
		case "xCam":
			for (int i=0; i<8; i++) {
				System.out.println("Inside value of i:"+i);
				try {
					if (overviewPage.xcamUpsell.isDisplayed()) {
						System.out.println("Found xCam Upsell.. clicking on the same");
						overviewPage.xcamUpsell.click();
						break;
					} else {
						System.out.println("Move to next upsell tab");
						waitTime(10);
						horizontalScroll(overviewPage.upsellTile);
					}
				} catch(Exception e) {
					System.out.println("Move to next upsell tab");
					waitTime(10);
					horizontalScroll(overviewPage.upsellTile);
				}
				System.out.println("Outside value of i:"+i);
				if (i==7) {
					Assert.fail(upsellScreen + " Upsell Screen not present");
				}
			}
			break;
		case "zen":
            for (int i=0; i<8; i++) {
                try {
                    if (overviewPage.zen_carousel.isDisplayed()) {
                        System.out.println("Found zen Upsell.. clicking on the same");
                        overviewPage.zen_carousel.click();
                        break;   
                    } else {
                    	System.out.println("Move to next upsell tab");
                        waitTime(10);
                        horizontalScroll(overviewPage.upsellTile);
                    }
                } catch(Exception e) {
                    System.out.println("Move to next upsell tab");
                    waitTime(10);
                    horizontalScroll(overviewPage.upsellTile);
                }
                System.out.println("Outside value of i:"+i);
				if (i==8) {
					Assert.fail(upsellScreen + " Screen not present");
				}
            }
            break;
            
		case "trouble":
			for (int i=0; i<8; i++) {
                try {
                    if (overviewPage.troubleCarousel.isDisplayed()) {
                        System.out.println("Found Having Trouble Upsell.. clicking on the same");
                        overviewPage.troubleCarousel.click();
                        break;
                    } else {
                    	 System.out.println("Move to next upsell tab");
                         waitTime(10);
                         horizontalScroll(overviewPage.upsellTile);
                    }
                } catch(Exception e) {
                    System.out.println("Move to next upsell tab");
                    waitTime(10);
                    horizontalScroll(overviewPage.upsellTile);
                }
                System.out.println("Outside value of i:"+i);
				if (i==8) {
					Assert.fail(upsellScreen + " Screen not present");
				}
            }
			break;
		case "default":
			Assert.fail("Unidentified tile defined in the method");
			break;
	}
}

public void handleOpenInHomeAlert() {
	waitTime(5);
	WebDriverWait wait=null;
	try {
	wait = new WebDriverWait(getDriver(), 5);
	Alert alert = wait.until(ExpectedConditions.alertIsPresent());
	getDriver().switchTo().alert();
	alert.accept();
	}catch(Exception e) {
		System.out.println("No server timeout alert found");
	}
	waitTime(5);
}

@Step("Step to validate if specific Upsell Screen is displayed is as expected")
public void validateUpsellScreen(String upsellScreen) {
	switch(upsellScreen) {
	case "xCam":
		List<WebElement> ids = new ArrayList<WebElement>(Arrays.asList(overviewPage.xCamupsellTitle, overviewPage.buyNowLink, overviewPage.learnMoreLink));
		for(i=0; i<ids.size(); i++) {
			if(scrollToElement(ids.get(i))) {
				System.out.println(ids.get(i)+" is displayed");
			}
		}

		break;
	case "zen":
		List<WebElement> zen_ids = new ArrayList<WebElement>(Arrays.asList(overviewPage.zenHeader, overviewPage.zenbuyNowLink, overviewPage.learnMoreLink, overviewPage.zen_icon, overviewPage.zen_close_button));
		for(i=0; i<zen_ids.size(); i++) {
			if(scrollToElement(zen_ids.get(i))) {
				System.out.println(zen_ids.get(i)+" is displayed");
			}
		}

		break;
//		
//	case "cvr":
//		settingsPage.validate_cvr_upsell_page();
//		break;
	}
}

@Step("Step to validate Upsell Screen not present for users with camera's")
public void validateUpsellScreenNotPresent(String upsellScreen) {
	switch(upsellScreen) {
		case "xCam":
			for (int i=0; i<8; i++) {
				try {
					if (overviewPage.xcamUpsell.isDisplayed()) {
						Assert.fail("Xcam Upsell present in an account with camera's");
					}
					else
					{
						System.out.println("Inside false block");
						System.out.println("XCam upsell not present");
						waitTime(10);
						horizontalScroll(overviewPage.upsellTile);	
					}
				} catch(Exception e) {
					System.out.println("XCam upsell not present");
					waitTime(10);
					horizontalScroll(overviewPage.upsellTile);
				}
			}
			break;
		case "zen":
            for (int i=0; i<8; i++) {
                try {
                    if (overviewPage.zen_carousel.isDisplayed()) {
                        Assert.fail("Zen Upsell present in an account ");
                    }
                    else
                    {
                    	System.out.println("Inside false block");
                        System.out.println("Zen upsell not present");
                        waitTime(10);
                        horizontalScroll(overviewPage.upsellTile);
                    }
                } catch(Exception e) {
                    System.out.println("Zen upsell not present");
                    waitTime(10);
                    horizontalScroll(overviewPage.upsellTile);
                }
            }
            break;
	}
}

@Step("Step to Validate Learn More screen on clicking Learn More icon")
public void validateLearnMoreScreen() {
	try {
		if(overviewPage.learnMoreLink.isDisplayed()) {
			overviewPage.learnMoreLink.click();
			if(scrollToElement(overviewPage.learnScreenCameraiCon)) {
				System.out.println("Camera Icon is displayed");
			}
		}
		else
		{
			Assert.fail("Learn More Link not present");
		}
	} catch(Exception e) {
		Assert.fail("Learn More Link not present");
	}
	
	// Close the webView
	closeWebView();
	
}

@Step("Step to Validate Buy Now screen on clicking Buy Now icon")
public void validateBuyNowScreen() {
	if(scrollToElement(overviewPage.buyNowLink)) {	
		
		overviewPage.buyNowLink.click();
		waitTime(20);
		if(scrollToElement(overviewPage.xfinityIcon)) {
			System.out.println("Buy Now screen is displayed");
		}
	}
	
	// Close the webView
	closeWebView();
}


@Step("Step to Validate zen Buy Now screen on clicking Get thermostat icon")
public void validateZenBuyNowEnabled() {
	if(scrollToElement(overviewPage.zenbuyNowLink)) {	
		
		if(overviewPage.zenbuyNowLink.isEnabled())
			System.out.println("zen buy now is enabled");
		else
			Assert.fail("zen buy now is not enabled");
		
	}
}

@Step("Step to Validate Buy Now screen not present for Internet User")
public void validateBuyNowScreenNotPresent() {
	try {
		if(overviewPage.buyNowLink.isDisplayed()) {
			Assert.fail("Buy Now Button is present for Internet User. Internet User should not see Buy Now");
		}
	} catch(Exception e) {
		System.out.println("Buy Now is not present for Internet User as expected");
	}
}

@Step("Step to close the web view screen")
public void closeWebView() {
	// Close screen
	try {
		if(overviewPage.closeButton.isDisplayed()) {
			System.out.println("Close Icon is displayed.. Closing the screen");
			overviewPage.closeButton.click();
		}
	} catch(Exception e) {
		Assert.fail("Close Button not present on Learn More Screen");
	}
}

@Step("Step to go back")
public void clickBack() {
	try {
		if(overviewPage.back_button.isDisplayed()) {
			overviewPage.back_button.click();
		}
	} catch(Exception e) {
		Assert.fail("Back button not present");
	}
}

@Step("step to close zen upsell for iOS")
public void clickClose_Zen() {
	try {
		if(overviewPage.zen_close_button.isDisplayed()) {
			overviewPage.zen_close_button.click();
		}
	}catch(Exception e) {
		Assert.fail("close button not present");
	}
}

@Step(" Step to verify Latest Activity on Overview Screen")
public void verify_latest_activity_on_Overview() {
	if (scrollToElement(overviewPage.latest_activity_on_overview)) {
		overviewPage.latest_activity_on_overview.click();
		verify_on_screen("Activity");
		back_to_overview();
	}
}

@Step(" Step to validate I am on Secure Account Overview screen")
public boolean validateIamOnSecureOverviewScreen() {
	scrollToTopOfOverviewScreen();
	boolean flag = false;
	try {
		if (overviewPage.overViewScreen.isDisplayed()) {
			flag = true;
		} else {
			flag = false;
		}
	} catch (Exception e) {
		flag = false;
	}
	return flag;
}

@Step("Step to navigate to the top of the overvew screen")
public void scrollToTopOfOverviewScreen()
{
	scrollToTopElement(overviewPage.overViewScreen);
}

@Step("Step to validate I am on Control Account Overview screen")
public boolean validateIamOnControlOverviewScreen() {
	scrollToTopElement(overviewPage.controlModeStatus);
	boolean flag = false;
	try {
		if (overviewPage.controlModeStatus.isDisplayed()) {
			flag = true;
		} else {
			flag = false;
		}
	} catch (Exception e) {
		flag = false;
	}
	return flag;
}

// Function to scroll to Camera Thumbnail
@Step("Step to scroll to Camera Thumbnail")
public void scrollToCameraThumbnail() {
	scrollToElement(overviewPage.camera_thumbnail);
}

// Function to scroll to Add a Devvice
@Step("Step to scroll to Add a Device")
public void scrollToAddADevice() {
	scrollToElement(overviewPage.add_a_device);
}

// Function to verify Camera Thumbnail appearance
@Step("Validate Camera thumbnail appearence")
public boolean verifyCameraThumbnailAppearance() {
	boolean flag = true;
	String thumbnailText;
	List<String> camerasList = getCamerasList();
	for(String cameraName : camerasList) {
		if (scrollToCamera(cameraName)) {
			if (XhomeUtils.platform.equals("iOS")) {
				thumbnailText = getDriver().findElement(By.xpath("//XCUIElementTypeButton[contains(@label,\""+cameraName+"\")]")).getText();						
			}else {
				thumbnailText = getDriver().findElement(By.xpath("//android.widget.TextView[@text='"+cameraName+"']/following-sibling::android.widget.TextView")).getText();	
			}
			//Validate camera thumbnail text
			if(thumbnailText.contains("Preview unavailable")) {
				System.out.println("TEST DATA ISSUE, Found offline camera -"+ thumbnailText);
				flag =false;
			}else {
				  String pattern = "(\\d+):(\\d+):(\\d+)";
				  // Create a Pattern object
			      Pattern r = Pattern.compile(pattern);

			      // Now create matcher object.
			      Matcher m = r.matcher(thumbnailText);
			      if (m.find( )){
			    	  System.out.println("Camera -"+thumbnailText+" thumbnail is displayed as expected");
			    	  flag = true || flag;
			      }else {
			    	  System.out.println("Camera -"+thumbnailText+" thumbnail is not displayed as expected");
			    	  flag = false;
			      }
			    	  
			}
			
		}
			
	}
	if(!flag) {
		Assert.fail("Camera thumbnail is not working as expected");
	}
	
	return flag;
}


// Function to move back from any Screen to Overview Screen except thirdparty as it requires cancel for iOS
@Step(" Step to move back from any Screen to Overview Screen (except third party for iOS)")
public void back_to_overview() {
	try {
		int i = 0;
		// while loop will check for the back button in any screen and travese back to
		// overview screen until the overview page is found.
		while (i < 5) {
			if (overviewPage.back_button.isDisplayed()) {
				System.out.println("Back Button is Displayed");
				overviewPage.back_button.click();
			}
			i++;
		}
		if (overviewPage.overview_icon.isDisplayed()) {
			System.out.println("ovrview icon displayed");
			overviewPage.overview_icon.click();
		}
	} catch (Exception e) {
		try {
			if (overviewPage.overview_icon.isDisplayed()) {
				System.out.println("ovrview icon displayed");
				overviewPage.overview_icon.click();
			}
		} catch (Exception e1) {
			Assert.fail("no back button and overview icon");
		}

	}
}

@Step("Navigate to ecobee thermostat")
public void navigate_ecobee_thermostat(String name)
{
	scrollToTopElement(overviewPage.overViewScreen);
	String ecobee_deviceName = "//*[@text='" + name + "']";
	boolean status = true;
	int j =0;

	do {
		try {
			if (j <= 6) {
				getDriver().findElement(By.xpath(ecobee_deviceName));
				status = false;
				j++;
				break;
			} else {
				Assert.fail("Connected ecobee thermostat is not found even after scrolling");
			}
		} catch (Exception e) {
			j++;
			scrollDown();

		}
	} while (status);
	waitTime(4);
	try {
	getDriver().findElement(By.xpath(ecobee_deviceName)).click();
	waitTime(5);
	}
	catch(Exception e) {
		Assert.fail("The thermostat "+name+" is not found");
	}
}

// Function to move back from Automation Screen
@Step(" Step to move back from Automation Screen")
public void back_from_automation() {
	try {

		if (XhomeUtils.platform.equals("iOS")) {
			if (overviewPage.overview_icon.isDisplayed()) {
				System.out.println("Overview icon is Displayed");
				overviewPage.overview_icon.click();
			}
		} else {
			if (overviewPage.automation_back_button.isDisplayed()) {
				System.out.println("Automation Screen Back Button is Displayed");
				overviewPage.automation_back_button.click();
			}
		}

	} catch (Exception e) {

	}
}

// Function to signout from app

@Step("Function to sign out from the app")
public boolean signOut() {
	boolean flag = false;
		click_screen_icon("More");
		verify_on_screen("More");
		scrollDown();
		scrollDown();
		/*if(!scrollToElement(overviewPage.signOutId)){
			Assert.fail("Sign Out not found even after scrolling");
		}*/
		try {
			if(overviewPage.signOutId.isDisplayed()) {
				overviewPage.signOutId.click();
				flag = true;
			} else {
				Assert.fail("Unable to sign out from app");
			}
		} catch(Exception e) {
			Assert.fail("Unable to sign out from app");
		}

	return flag;

}

//commenting and placing this earlier snippet for a referencce because in case if a logic is required to signout from more options dirctly in android instead of from more screen it could be retained/reused instead of writing code from first 	

/* // Function to signout from app
public boolean signOut() {
	boolean flag = false;
	if(XhomeUtils.platform.equals("iOS")) 
	{
		click_screen_icon("More");
		verify_on_screen("More");
		scrollDown();
		    try {
		if(overviewPage.signOutId.isDisplayed()) {
			overviewPage.signOutId.click();
			flag = true;}}
		    catch(Exception e) {System.out.println("no signout");}
	}
	else {
		if (overviewPage.more_option_icon.isDisplayed()) {
			System.out.println("More options Displayed");
			overviewPage.more_option_icon.click();
			if (overviewPage.signOutId.isDisplayed()) {
				overviewPage.signOutId.click();
				flag = true;
			}
		}}
	return flag;

} */

@Step("Step to Validate a verify feedback tile")
public void verify_feedback_tile() {

	try {

		if (scrollToElement(overviewPage.feedback_card)) {
			System.out.println("feedback tile is displayed");
			try {
				if (overviewPage.tell_us.isDisplayed()) {
					System.out.println("Tell Us is displayed");
					try {
						if (overviewPage.feedback_tile_close.isDisplayed()) {
							System.out.println("feedback tile close button is displayed");
							String ExpText = "How are we doing? Tell us";
							String ActText = overviewPage.feedback_card.getText();

							if (ExpText.equals(ActText)) {
								// Assert.assertEquals(ExpText, ActText);
								System.out.println("Text verified Successfully");
							} else {
								Assert.fail("Text Verification Failed");
							}

						}

					} catch (Exception e3) {
						Assert.fail("feedback tile close button is NOT displayed");
					}

				}
			}

			catch (Exception e2) {
				Assert.fail("Tell Us is NOT displayed ");
			}

		}

	} catch (Exception e) {
		Assert.fail("feedback tile is not displayed ");
	}

}

// Verify Buy and clear link in Zen Thermostat screen
@Step(" Step to Verify Buy and clear link in Zen Thermostat screen")
public void verify_learnmore() {
	waitTime(9);
	if (!(overviewPage.learnMoreLink.isEnabled())) {
		Assert.fail("Learn more btn is not enabled");
	}
}

@Step(" Step to validate Lock Unlock")
public void validateLockUnlock() {
	scrollToElement(overviewPage.LockTile);
	String before_doorLockStatus = null;
	boolean doorlock_status=false;
	boolean doorlock_icon_status=false;
	try {
		if(XhomeUtils.platform.equals("Android")) {
			doorlock_status=overviewPage.DoorLockStatus.isDisplayed();
		}else {
			doorlock_status=overviewPage.DoorLockStatus.isEnabled();
		}
		if (doorlock_status) {
			before_doorLockStatus = overviewPage.DoorLockStatus.getText();
			System.out.println("Door lock status"+before_doorLockStatus);
		} else {
			Assert.fail("Door Lock status tile not present");
		}
	} catch (Exception e) {
		Assert.fail("Door Lock status tile not present");
	}

	try {
		if(XhomeUtils.platform.equals("Android")) {
			doorlock_icon_status=overviewPage.DoorLockIcon.isDisplayed();
		}else {
			doorlock_icon_status=overviewPage.DoorLockIcon.isEnabled();
		}
		if (doorlock_icon_status) {
			overviewPage.DoorLockIcon.click();
		} else {
			Assert.fail("Inside false block, Door Lock icon not displayed");
		}
	} catch (Exception e) {
		Assert.fail("Door Lock icon not displayed");
	}
	int locking_status=0;
	for(locking_status=0;locking_status<7;locking_status++) {
		String locking_state=null;
		waitTime(10);
		try {
		if(overviewPage.DoorLockingStatus.isDisplayed()) {
		   locking_state= overviewPage.DoorLockingStatus.getText();
		   if(locking_state.contains("Locking")||locking_state.contains("Unlocking"))
			   System.out.println("Door lock is in locking/unlocking state");
		   else
			   System.out.println("Door lock is not in locking/unlocking state");
		}else {
			System.out.println("Door lock is not in locking state checking for alert");
			handleServerTimeoutAlert();
			break;
		}
		}catch(Exception e) {
			System.out.println("Door lock is not in locking/unlocking state checking for alert");
			handleServerTimeoutAlert();
			break;
		}
		locking_status++;
	}
	verifylockstatuschange(before_doorLockStatus);
}
@Step("Verifying lock status changed or not")
public void verifylockstatuschange(String beforedoorLockStatus) {
	String after_doorLockStatus=null;
	waitTime(15);
	try {
	after_doorLockStatus = overviewPage.DoorLockStatus.getText();
	}catch(Exception e) {
		Assert.fail("Door lock status element not found");
	}
	for(int i=0;i<7;i++) {
	if (beforedoorLockStatus.equals(after_doorLockStatus)) {
		System.out.println("Door lock status not yet changed");
	} else {
		System.out.println(beforedoorLockStatus+ " Door lock status changed to " + after_doorLockStatus);
		break;
	}
	if(i==6)
		Assert.fail("Waited for 30sec but lock status is not changed");
	waitTime(5);
	}
}
@Step("To verify for servertimeout alert window")
public void handleServerTimeoutAlert() {
	boolean alert_found=false;
	if(XhomeUtils.platform.equalsIgnoreCase("iOS"))
	{
		WebDriverWait wait = new WebDriverWait(getDriver(), 5);
		try {
		wait.until(ExpectedConditions.alertIsPresent());
		getDriver().switchTo().alert();
		alert_found=true;
		}catch(Exception e) {
			System.out.println("No server timeout alert found");
			alert_found=false;
		}
	}
	try {
		if(overviewPage.alertHeader.isDisplayed()) {
			System.out.println("Alert header found");
			alert_found=true;
		}else {
			System.out.println("Inside false block, No alert header found");
		}
		
	}catch(Exception e) {
		System.out.println("No alert header found");
	}
	if(alert_found) {
		try {
			if(overviewPage.alert_ok.isDisplayed()) {
				overviewPage.alert_ok.click();
				System.out.println("Clicked on ok");
			}else {
				Assert.fail("Inside false block, No alert ok button found");
			}
			
		}catch(Exception e) {
			Assert.fail("No alert ok button found");
		}
	}
}

public List<String> getCamerasList() {
	List<String> camerasList = new ArrayList<String>();
	String removeStatus1 = "Preview unavailable";
	String removeStatus2 = "Connecting to camera...";
	System.out.println("Camera Name List");
	int run = 0;
	while (run < 5) {

		int count = overviewPage.camerasList.size();
		for (int index = 0; index < count; index++) {
			String cameraName = overviewPage.camerasList.get(index).getText();
			if (XhomeUtils.platform.equals("iOS")) {
				if (cameraName.contains(removeStatus1)) {
					cameraName.replaceAll(removeStatus1, "");
				} else if (cameraName.contains(removeStatus2)) {
					cameraName.replaceAll(removeStatus2, "");
				} else {
					int len = cameraName.split(" ").length;
					cameraName = cameraName.replaceAll(cameraName.split(" ")[len - 1], "").trim();
				}
			}
			boolean elementExist = false;
			for (String name : camerasList) {
				if (cameraName.contentEquals(name)) {
					elementExist = true;
				}
			}
			if (!elementExist) {
				System.out.println(cameraName);
				camerasList.add(cameraName);
			}
		}
		if (XhomeUtils.platform.equals("iOS")) {
			run = 100;
		} else {
			scrollDownToNextCamera();
			run++;
		}
	}
	scrollToTopElement(overviewPage.overViewScreen);
	return camerasList;
}

@Step(" Step to click on given camera")
public void clickGivenCamera(String cameraName) {
	WebElement element;
	if (XhomeUtils.platform.equals("iOS")) {
		scrollToElementByXpath("//XCUIElementTypeButton[contains(@label,\"" + cameraName + "\")]");
		getDriver()
		.findElement(By.xpath("//XCUIElementTypeButton[contains(@label,\"" + cameraName + "\")]")).click();
	} else {
		scrollToElementByXpath("//android.widget.TextView[@text='" + cameraName + "']");
		getDriver()
				.findElement(By.xpath("//android.widget.TextView[@text='" + cameraName + "']")).click();
	}

	/*while (run < 3) {
		try {
			WebElement element;
			if (XhomeUtils.platform.equals("iOS")) {
				element = getDriver()
						.findElement(By.xpath("//XCUIElementTypeButton[contains(@label,\"" + cameraName + "\")]"));
				scrollToElement(element);
				if (element.isDisplayed()) {
					element.click();
					break;
				} else {
					System.out.println("Element not found, scrolling down");
					scrollDown();
				}
			} else {
				element = getDriver()
						.findElement(By.xpath("//android.widget.TextView[@text='" + cameraName + "']"));
				scrollToElement(element);
				element.click();
				break;
			}

		} catch (NoSuchElementException e) {
			System.out.println("Element not found, scrolling down");
			scrollDown();
		}

		run++;}*/
	
	}

@Step(" Step to Scroll to Camera")
public boolean scrollToCamera(String cameraName) {
	int run = 1;
	boolean elementExist = false;
	while (run < 3) {
		try {
			WebElement element;
			if (XhomeUtils.platform.equals("iOS")) {
				element = getDriver()
						.findElement(By.xpath("//XCUIElementTypeButton[contains(@label,\"" + cameraName + "\")]"));
				if (element.isDisplayed()) {
					elementExist = true;
					break;
				} else {
					System.out.println("Element not found, scrolling down");
					scrollDown();
				}
			} else {
				element = getDriver()
						.findElement(By.xpath("//android.widget.TextView[@text='" + cameraName + "']"));
				elementExist = true;
				break;
			}

		} catch (NoSuchElementException e) {
			System.out.println("Element not found, scrolling down");
			scrollDown();
		}
		run++;
	}
	return elementExist;
}

@Step("Step to Validate Security Sensors on Overview screen")
public void verifySensor(String sensor) {

	if (scrollToElement(overviewPage.security_sensors)) {
		waitTime(10);
	} else {
		Assert.fail("Unable to scroll to element ");
	}
	switch (sensor) {

	case "Door":
		if (overviewPage.DoorSensor.size() > 0) {
			System.out.println(" Door Sensor is Displayed ");
		} else {
			Assert.fail("Door Sensor is NOT Displayed ");
		}
		break;
	case "Window":
		if (overviewPage.WindowSensor.size() > 0) {
			System.out.println(" Window Sensor is Displayed ");
		} else {
			Assert.fail("Window Sensor is NOT Displayed ");
		}
		break;

	case "Motion":
		if (overviewPage.MotionSensor.size() > 0) {
			System.out.println(" Motion Sensor is Displayed ");
		} else {
			Assert.fail("Motion Sensor is NOT Displayed ");
		}
		break;
	case "Glass Break":
		if (overviewPage.GlassBreakSensor.size() > 0) {
			System.out.println(" Glass Break Sensor is Displayed ");
		} else {
			Assert.fail("Glass Break Sensor is NOT Displayed ");
		}
		break;
	case "Smoke":
		if (scrollToElement(overviewPage.hazard_detectors)) {
		}
			if(overviewPage.SmokeSensor.size() > 0) {
				System.out.println(" Smoke Sensor is Displayed");	
			}

			else {
				System.out.println("Smoke Sensot is NOT Displayed");
		}
		break;
		
	case "Water":
		if (scrollToElement(overviewPage.hazard_detectors)) {
		}
			if(overviewPage.WaterSensor.size() > 0) {
				System.out.println(" Water Sensor is Displayed");	
			}
			else {
				System.out.println("Water Sensor is NOT Displayed");
		}
		break;
	default:
		Assert.fail("Enter a Valid Sensor to Validate" + sensor + " is NOT Displayed");

	}
}

		
	

// Function to validate overview screen Appearance
@Step("Step to validate Alert Icon")
public void validateAlerticon() {
	try {
		if (overviewPage.alert_icon.isDisplayed()) {
			System.out.println(" Alert icon is Displayed ");
		}else {
			Assert.fail("Inside false block, alert icon is not Displayed ");
		}
	} catch (Exception e1) {
		System.out.println("Alert badge is NOT Displayed for this account ");

	}}

@Step(" Step to validate Activity icon")
public void validateActivityIcon() {
	try {
		if (overviewPage.activity_icon.isDisplayed()) {
			System.out.println(" Activity icon is Displayed ");
		}else {
			Assert.fail("Inside false block, Activity icon is not displayed");
		}
	} catch (Exception e1) {
		Assert.fail("Activity icon is NOT Displayed ");

	}
}

@Step(" Step to validate Automation icon")
public void validateAutomationIcon() {
	try {
		if (overviewPage.automation_icon.isDisplayed()) {
			System.out.println(" Automation icon is Displayed ");
		}else {
			Assert.fail("Inside false block, Automation icon is not displayed");
		}
	} catch (Exception e1) {
		Assert.fail("Automation icon is NOT Displayed ");

	}
}

@Step(" Step to validate More icon")
public void validateMoreIcon() {
	try {
		if (overviewPage.more_icon.isDisplayed()) {
			System.out.println(" More icon is Displayed ");
		}else {
			Assert.fail("Inside false block, More icon is not displayed");
		}
	} catch (Exception e1) {
		Assert.fail("More icon is NOT Displayed ");

	}
}

@Step(" Step to validate Feedback Tile")
public void validateFeedbacktile() {
	try {
		if (overviewPage.feedback_card.isDisplayed()) {
			System.out.println(" Feedback tile is Displayed ");
		} else if (!overviewPage.feedback_card.isDisplayed()) {
			scrollToElement(overviewPage.feedback_card);
			waitTime(5);
			if (overviewPage.feedback_card.isDisplayed()) {
				System.out.println("Feedback card displayed");
			}

		}
	} catch (Exception e1) {
		System.out.println("Feedback card is NOT Displayed ");

	}
}

@Step(" Step to validate Camera Tile")
public void validateCameraTile() {

	try {
		waitTime(5);
		if (scrollToElement(overviewPage.cameraTimestamp)) {
			if (overviewPage.cameraTimestamp.isDisplayed())
				;

			System.out.println(" camera Tile is Displayed ");
		}
	} catch (Exception e6) {
		System.out.println("Camera Tile is NOT Displayed for this account");
	}
}

@Step(" Step to validate carousel Tile")
public void validateCarouselTile() {
	try {
		waitTime(5);
		if (scrollToElement(overviewPage.upsellTile)) {
			overviewPage.upsellTile.isDisplayed();

			System.out.println(" Carousel Tile is Displayed ");
		}else {
			Assert.fail("Not scrolled to Carousel element");
		}

	} catch (Exception e) {
		System.out.println("Carousel is NOT Displayed for this account");
	}
}

@Step(" Step to validate Add a Device Tile")
public void validateAddaDeviceTile() {
	try {
		if (scrollToElement(overviewPage.add_a_device)) {
			if (overviewPage.add_a_device.isDisplayed()) {
				System.out.println(" Add a Device is Displayed");
			}else {
				Assert.fail("Inside false block, Add a Device is not Displayed");
			}
		}else {
			Assert.fail("Not scrolled to add a device");
		}
	} catch (Exception e) {
		Assert.fail("Add a Device is NOT Displayed");
	}

}

		
	@Step("Validate Arm/Disarm Shield")
	public void validateshield() {
		try {
			if (overviewPage.Status.isDisplayed()) {
			
				System.out.println(overviewPage.Status.getAttribute("name")+" Status is Displayed");
				
			}else {
				Assert.fail("Inside false block "+overviewPage.Status.getAttribute("name")+"status is not displayed");
			}
		} catch (Exception e) {
			Assert.fail(" Arm Disarm Status is NOT Displayed ");
		}
			
		}
	
	@Step("Step to get cameras list from overview page")
	public  List<String> getIotList() {
		back_to_overview();
		List<String> deviceList = new ArrayList<String>();
		System.out.println("Number of devices found");
		int run = 0;
		String deviceName=null;
		waitTime(20);
		try {
		while(run<5) {
			int count = overviewPage.IotList.size();
			System.out.println(count);
			for(int index = 0; index<count; index++) {
				try {
				deviceName = overviewPage.IotList.get(index).getText();
				}catch(Exception e1) {
					System.out.println("No devices found");
					break;
				}
				System.out.println(deviceName);
				boolean elementExist = false;
				for (String name : deviceList) {
					if(deviceName.contentEquals(name)) {
						elementExist = true;
					}
				}if(!elementExist) {
					deviceList.add(deviceName);
					System.out.println(deviceName);
				}
			}
			scrollDown();
			run++;
		}
		}
		catch(Exception e) {
			Assert.fail("No more devices available");
		}
		scrollToTopElement(overviewPage.camera_thumbnail);
		return deviceList;
	
	}

	public void printing_all_cameranames() {
		List<WebElement> allcameranames=overviewPage.cameranames;
					System.out.println(allcameranames.size());

					/**for (int i = 1; i<=allcameranames.size(); i=i+1)

					{

					System.out.println(allcameranames.get(i).getText());

					}**/
					
					try {
						sortorder(allcameranames);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

	public static void sortorder(List<WebElement> elementList) throws InterruptedException {
		
		 ArrayList<String> beforesort = new ArrayList<String>(); 
		 ArrayList<String> aftersort = new ArrayList<String>(); 

			Thread.sleep(3000);
			
		System.out.println(elementList.size());

		for (int i = 1; i<elementList.size(); i++)
			 
		{
			
			System.out.println(elementList.get(i).getText());
		beforesort.add(elementList.get(i).getText());

		}
aftersort=beforesort ;
		Collections.sort(beforesort);
		System.out.println("After sorting ---------");

		for (int i = 1; i<beforesort.size(); i++)
			 
		{


		System.out.println(beforesort.get(i));
		

			}
		
		if (Arrays.equals(beforesort.toArray(), aftersort.toArray())) 
			System.out.println("Same"); 
		else
			System.out.println("Not same");
		
		}
}

	
//***************************************************//
//Class to define Page Objects
//All Web Elements of the screen defined here
//***************************************************//
class OverView_PageObjects {

@CacheLookup
@AndroidFindBy(id = "status_icon_container")
//@iOSFindBy(xpath = "//XCUIElementTypeButton[contains(@label,'rmed')]")
@iOSFindBy(id = "StatusTile")
public WebElement overViewScreen;

@CacheLookup
@AndroidFindBy(id = "android:id/alertTitle")
public WebElement updateAlert;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.Button[@text='Ignore']")
public WebElement ignoreUpdate;

@CacheLookup
@AndroidFindBy(id = "tagline")
public WebElement fingerprint_tagline;

@CacheLookup
@AndroidFindBy(id = "secondary_action")
public WebElement ignore_fingerprint;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='24/7 Video Recording']")
public WebElement cvr_onboarding_24_7;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='Navigate up']")
public WebElement cvr_onboarding_back_button;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Got it, thanks']")
public WebElement auth4allPopup;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Disarmed\"]")
@AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Disarmed Button']")
public WebElement disarm_status;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Armed Away Button']")
@iOSFindBy(id = "btn_armed_away")
public WebElement arm_away_button;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Armed Stay Button']")
@iOSFindBy(id = "btn_armed_stay")
public WebElement arm_stay_button;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc='Armed Night Button']")
@iOSFindBy(id = "btn_armed_night")
public WebElement arm_night_button;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[contains(@content-desc,'Armed')]")
@iOSFindBy(xpath ="////XCUIElementTypeButton[contains(@id,'btn_armed')]")
public WebElement arm_button;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='5']")
@iOSFindBy(id = "keypad_button_5")
public WebElement pin_5_text;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Arming Away…']")
@iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Arming Away...\"]")
public WebElement arming_away_status;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='Arming Stay…']")
@iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Arming Stay...\"]")
public WebElement arming_stay_status;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='Arming Night…']")
@iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Arming Night...\"]")
public WebElement arming_night_status;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[@instance=2]")
@iOSFindBy(id = "arm_Night_button")
public WebElement arm_night_mode;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[@instance=1]")
@iOSFindBy(id = "arm_Away_button")
public WebElement arm_away_mode;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[@instance=0]")
@iOSFindBy(id = "arm_Stay_button")
public WebElement arm_stay_mode;

@CacheLookup
@AndroidFindBy(id = "navigation_item_container_history")
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Activity\"]")
public WebElement activity_icon;

@CacheLookup
//@AndroidFindBy(xpath = "//android.widget.TextView[@text='Activity']") //Build < 9.40
@AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='Activity']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Activity\"]")
public WebElement activity_title;

@CacheLookup
//@AndroidFindBy(xpath = "//android.widget.FrameLayout[@content-desc='Automation']") //Build < 9.40 
@AndroidFindBy(id = "navigation_label_automation")
//@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Automation\"]")
@iOSFindBy(id = "Automation")
public WebElement automation_icon;

//@CacheLookup
@AndroidFindBy(id = "headerText")
//@AndroidFindBy(xpath = "//android.view.View[@content-desc='Automation']")
@iOSFindBy(xpath = "(//XCUIElementTypeOther[@name=\"Automation\"])[2]")
public WebElement automation_title;

//@CacheLookup
//@AndroidFindBy(xpath = "//android.widget.TextView[@text='More']") //Build < 9.40
@AndroidFindBy(xpath = "//android.widget.TextView[@text='More']") //content-desc has no value, hence used parent class to differentiate it from the nav bar 'more' element
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"More\"]")
public WebElement more_title;

@CacheLookup
//@iOSFindBy(xpath = "//XCUIElementTypeButton[@name='Notification Center']")
@iOSFindBy(id = "overview_alerts_button")
@AndroidFindBy(id = "alert_overview")
public WebElement alert_icon;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='All Alerts']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"All Alerts\"]")
public WebElement alert_title;

//@CacheLookup //Since back button is only available for partial pages, caching the property returns false for isDisplayed, hence exception part is not reachable
//@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Back\"]")
@iOSFindBy(xpath = "//*[@name='back'] | //*[@name='Back'] | //*[@name='backButton']") //Camera settings has 'Back' as value, for 9.42 build 'back' changed to 'backButton'
@AndroidFindBy(xpath = "//android.widget.ImageButton[@content-desc='Navigate up']")
public WebElement back_button;

//thebelow 2 are specific for android only not available for iOS
@CacheLookup
@AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Back']")
public WebElement automation_back_button;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"More\"]")
@AndroidFindBy(id = "navigation_label_more")
public WebElement more_icon;

@CacheLookup
@AndroidFindBy(id = "zones_sensors_bar")
@iOSFindBy(xpath = "//XCUIElementTypeStaticText/following-sibling::XCUIElementTypeOther/XCUIElementTypeButton")
public WebElement sensors_status;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeApplication[@name=\"X-FINITY Home\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeImage[1]")
public WebElement sensors_status_arrow;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"Tell Us\"]")
public WebElement tell_us;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Sensors & Detectors']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Sensors & Detectors\"]")
public WebElement sensors_title;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Tell us how we\'re doing\"]")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name='XfinityHome.NPSFeedbackView']/XCUIElementTypeStaticText")
public WebElement feedback_title;

@CacheLookup
@AndroidFindBy(id = "feedback_card")
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"How are we doing? Tell us\"]")
public WebElement feedback_card;

@CacheLookup
@AndroidFindBy(id = "camera_frame")
@iOSFindBy(xpath = "//XCUIElementTypeButton[contains(@name,'cameraThumbnail')]")
//@iOSFindBy(xpath="(//XCUIElementTypeCell[3]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeButton)[1]")
WebElement camera_thumbnail;


@iOSFindBy(xpath="(//XCUIElementTypeCell[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeButton)[1]")
WebElement internet_camera_thumbnail;

//the below 3 are available in android not available in iOS so used alternative 
@CacheLookup
@AndroidFindBy(id = "camera_name")
WebElement cameraName;

@CacheLookup
@AndroidFindBy(id = "camera_timestamp")
WebElement cameraTimestamp;

@CacheLookup
@AndroidFindBy(id = "video_details_player_fragment_container")
public WebElement camera_video_details;

@CacheLookup
@AndroidFindBy(id = "com.comcast.beta:id/activity_overview")
// @FindBy(xpath = "//android.widget.TextView[@text='Latest Activity']")
@iOSFindBy(xpath = "//XCUIElementTypeOther[@value='latest activity']")
public WebElement latest_activity_on_overview;

@CacheLookup
@AndroidFindBy(id = "lights_title")
@iOSFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"toggle_light_label\"])[1]")
public WebElement lightTile;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Add a Device']")
//@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Add a Device\"]")
@iOSFindBy(xpath="//XCUIElementTypeOther[@name=\"AddDeviceTile\"]")  //  id for >9.40 build
public WebElement add_a_device;

//@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Add a Device']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Add a Device\"]")
public WebElement add_a_device_title;

//@CacheLookup
@AndroidFindBy(id = "sensor_title")
@iOSFindBy(id = "security_tile")
public WebElement security_sensors;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Hazard Detectors']")
@iOSFindBy(id = "hazard_tile")
public WebElement hazard_detectors;

@CacheLookup
@AndroidFindBy(xpath="//android.widget.TextView[@text='Security Sensors']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Security Sensors\"]")
public WebElement security_sensors_title;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Hazard Detectors']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Hazard Detectors\"]")
public WebElement hazard_detectors_title;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Lights']")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Lights\"]")
public WebElement LightsTitle;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Sign Out']")
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name='Sign Out']")
public WebElement signOutId;

@CacheLookup
@AndroidFindBy(id = "new_mode_button")
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name='Vacation' or @name='Home' or @name='Night' or @name='Away']")
public WebElement controlModeStatus;

//@CacheLookup
@AndroidFindBy(id = "thermostat_info")
//@iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"Thermostats\"]") ---  this can be used to locate if there are thermostats like the complete set of thermostat tiles
//   @iOSFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"thermostatNameLabel\"])[1]")
@iOSFindBy(id = "Thermostats")
public WebElement thermostatTile;

@CacheLookup
//@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"System status\"]")
@iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"StatusTile\"]")
public WebElement system_status;

@CacheLookup
@AndroidFindBy(id = "navigation_label_overview")
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Overview\"]")
//@AndroidFindBy(id = "navigation_label_overview")
public WebElement overview_icon;

@CacheLookup
@iOSFindBy(id = "Close")
public WebElement close_button;

@CacheLookup
@iOSFindBy(id = "Remove Feedback Prompt")
public WebElement feedback_tile_close;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"Thermostats\"]")
public WebElement thermostats_section;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"Lights\"]")
public WebElement lights_section;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Continue\"]")
public WebElement continue_button;

@CacheLookup
@iOSFindBy(id = "We'd like to send you push notifications")
public WebElement push_notification_screen;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeStaticText[@name=\"We'd like to add XFINITY Home to your contacts\"]")
public WebElement add_to_contacts_screen;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeAlert[@name=\"Home Unexpectedly Quit\"]")
public WebElement crash_header;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Send Report\"]")
public WebElement send_report_button;

@CacheLookup
//@iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"cameraAccessibilityHeader\"]/following:: XCUIElementTypeButton[1]")
@iOSFindBy(xpath = "(//XCUIElementTypeButton[2])")
public WebElement camera_name_timestamp;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeButton[@name='Vacation' or @name='Home' or @name='Night' or @name='Away']")
public WebElement control_validation;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Overview\"]")
@AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='Overview']")
public WebElement internet_validation;

@CacheLookup
@AndroidFindBy(id = "carousel_viewpager")
@iOSFindBy(id = "carouselTile")
public WebElement upsellTile;

@CacheLookup
@AndroidFindBy(xpath = "//android.support.v4.view.ViewPager[@content-desc=\"See What\'s Happening, Explore the Xfinity Camera. Double tap to activate.\"]")
@iOSFindBy(xpath = "//XCUIElementTypeScrollView[contains(@name,'Record now, view later, Xfinity Camera with 24/7 Recording,')]")
public WebElement xcamUpsell;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Xfinity Camera']")
@iOSFindBy(xpath="//XCUIElementTypeNavigationBar[@name=\"Xfinity Camera\"]")
public WebElement xCamupsellTitle;

@CacheLookup
@AndroidFindBy(id = "cvr_learn_buy_button")
@iOSFindBy(id = "Get a Camera")
public WebElement buyNowLink;

@CacheLookup
@AndroidFindBy(id = "cvr_learn_more_link")
@iOSFindBy(id="Learn More")
public WebElement learnMoreLink;

@CacheLookup
@AndroidFindBy(xpath = "//android.view.View[@text='XH Camera']")
@iOSFindBy(xpath = "//*[@id=\\\"eq_hero_1\\\"]/img[contains(@alt, 'XH Camera')]")
public WebElement learnScreenCameraiCon;

@CacheLookup
//@AndroidFindBy(xpath = "//android.widget.ImageButton[@index=1]")
@AndroidFindBy(id = "com.android.chrome:id/close_button")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Xfinity Camera\"]/XCUIElementTypeButton")
public WebElement closeButton;

@CacheLookup
@AndroidFindBy(xpath = "//android.view.View[@text='xfinity']")
public WebElement xfinityIcon;

@CacheLookup
@AndroidFindBy(xpath = "//android.support.v4.view.ViewPager[@content-desc='Save Energy and Money, Explore Zen Thermostats. Double tap to activate.']")
@iOSFindBy(xpath = "//XCUIElementTypeScrollView[contains(@name,'Save Energy and Money, Explore Zen Thermostats,')]") 
public WebElement zen_carousel;

@CacheLookup
@iOSFindBy(xpath = "//XCUIElementTypeScrollView[contains(@name,'Having Trouble?, Chat with us to get help and be on your way')]")
@AndroidFindBy(xpath = "//android.support.v4.view.ViewPager[@content-desc='Having Trouble?, Chat with us to get help and be on your way. Double tap to activate.']")
public WebElement troubleCarousel;

@CacheLookup
@AndroidFindBy(id = "bboffline_title")
@iOSFindBy(id = "Your Touchscreen is not connected to WiFi.")
public WebElement bbOfflineTitle;

@CacheLookup
@AndroidFindBy(id = "doorlock_info")
@iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"Locks\"]")
public WebElement LockTile;

@AndroidFindBy(id = "doorlock_status")
@iOSFindBy(xpath = "(//XCUIElementTypeStaticText[@name='Unlocked' or @name='Locked'])[1]")
public WebElement DoorLockStatus ;

@AndroidFindBy(xpath="//android.widget.TextView[@text='Locking…' or @text='Unlocking…']")
@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name='Locking…' or @name='Unlocking…']")
public WebElement DoorLockingStatus;

@CacheLookup
@AndroidFindBy(id = "lock_icon")
//@iOSFindBy(xpath = "(//XCUIElementTypeStaticText[@name='Locked' or @name='Unlocked'])[1]/preceding-sibling::XCUIElementTypeButton")
@iOSFindBy(xpath="(//XCUIElementTypeStaticText[@name='Locked' or @name='Unlocked'])[1]/parent::XCUIElementTypeCell/XCUIElementTypeButton")
public WebElement DoorLockIcon ;

@iOSFindBy(id = "all_off_button")
public WebElement cancel_arming_button;

@iOSFindBy(id = "Get a Thermostat")
public WebElement zenbuyNowLink;


@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Zen Thermostat\"]")
public WebElement zenHeader;

@iOSFindBy(id = "zen_upsell")
public WebElement zen_icon;

@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name=\"Zen Thermostat\"]/XCUIElementTypeButton")
public WebElement zen_close_button;

@CacheLookup
@AndroidFindBy(id = "cvr_recording")	
public WebElement recCameraThumbNail;

@CacheLookup
@iOSFindBy(id = "exit_button")
public WebElement alertExitButton;

@AndroidFindBy(id = "camera_name")
//@iOSFindBy(xpath="//XCUIElementTypeCell/*/*/*/XCUIElementTypeButton")
@iOSFindBy(xpath = "//XCUIElementTypeButton[contains(@name,'cameraThumbnail')]")
public List<WebElement> camerasList;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Smoke']")
@iOSFindBy(id = "Smoke")
public List<WebElement> SmokeSensor;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Water']")
@iOSFindBy(id = "Water")
public List<WebElement> WaterSensor;

@CacheLookup
@AndroidFindBy(id = "status")
@iOSFindBy(xpath = "//XCUIElementTypeNavigationBar[@name='Armed Away' or @name='Disarmed' or @name='Armed Night' or @name='Armed Stay']")
public WebElement Status;

@FindBy(id = "mainForm")
//@iOSFindBy(xpath="//XCUIElementTypeStaticText[contains(@name,'please provide at least one option below.')]")
public WebElement cimaSupportScreen;


//@AndroidFindBy(xpath = "//android.widget.Button[@text='Ask me later']")
//@iOSFindBy(xpath = "//XCUIElementTypeButton[@name='Ask me later']")
@FindBy(xpath = "//*/button[@value='askMeLater']")
public WebElement askMeLaterButton;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[contains(@resource-id,'pcicon_door_')]")
@iOSFindBy(xpath = "//XCUIElementTypeCell[starts-with(@name,'Door')]")
public List<WebElement> DoorSensor;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[contains(@resource-id,'pcicon_window_')]")
@iOSFindBy(xpath = "//XCUIElementTypeCell[starts-with(@name,'Window')]")
public List<WebElement> WindowSensor;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[contains(@resource-id,'pcicon_motion')]")
@iOSFindBy(xpath = "//XCUIElementTypeCell[contains(@name,'Motion')]")
public List<WebElement> MotionSensor;

@CacheLookup
@AndroidFindBy(xpath = "//android.widget.ImageView[contains(@resource-id,'pcicon_glass_')]")
@iOSFindBy(xpath = "//XCUIElementTypeCell[contains(@name,'Glass Break')]")
public List<WebElement> GlassBreakSensor;

@iOSFindBy(xpath="//*[@name=\"Thermostats\"]/parent::XCUIElementTypeOther/descendant::XCUIElementTypeCell/XCUIElementTypeStaticText[1]")
@AndroidFindBy(id="thermostat_name")
public List<WebElement> IotList;

@AndroidFindBy(id="alertTitle")
@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name=\"Error Updating Door-Lock\"]")
public WebElement alertHeader;

@AndroidFindBy(id="button1")
@iOSFindBy(id="OK")
public WebElement alert_ok;

@CacheLookup
@AndroidFindBys(@AndroidFindBy(id="com.comcast.beta:id/camera_name"))
public List<WebElement> cameranames;


}
