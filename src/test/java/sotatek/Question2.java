package sotatek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import models.CompareObject;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Question2 {
	@Managed(driver = "chrome")
	WebDriver driver;
	private final String product = "iphone 11";
	private final String url1 = "https://tiki.vn/";
	private final String url2 = "https://www.lazada.vn/";

	@Test
	public void TC_01() throws InterruptedException, JsonProcessingException {
		driver.get(url1);
		driver.manage().window().maximize();
		Thread.sleep(Constants.TIME_WAIT_FINISH_OPEN_MAX_BROWSER);
		WebElement searchBoxTiki = findElementXPathConditionExist("//input[@data-view-id= 'main_search_form_input']", driver);
		searchBoxTiki.sendKeys(product);
		WebElement searchButtonTiki = findElementXPathConditionExist("//button[@data-view-id= 'main_search_form_button']", driver);
		searchButtonTiki.click();
		List<WebElement> productsTiki = findElementsXPathConditionExist("//span[@class = 'style__StyledItem-sc-18svp8n-0 fkDgwT']", driver);
		productsTiki.get(0).click();
		WebElement nameTiki = findElementXPathConditionExist("//h1[@class='title']", driver);
		WebElement priceTiki = findElementXPathConditionExist("//div[@class = 'product-price__current-price']", driver);
		CompareObject tiki = new CompareObject();
		tiki.setWebsite("Tiki");
		tiki.setNameProduct(nameTiki.getText());
		Long priceIphone11Tiki = Long.valueOf(priceTiki.getText().substring(0, 10).replace(".", ""));
		tiki.setPriceProduct(priceIphone11Tiki);
		tiki.setUrlProduct(driver.getCurrentUrl());

		driver.get(url2);
		driver.manage().window().maximize();
		Thread.sleep(Constants.TIME_WAIT_FINISH_OPEN_MAX_BROWSER);
		WebElement searchBoxLazada = findElementXPathConditionExist("//input[@type='search']", driver);
		searchBoxLazada.sendKeys(product);
		WebElement searchButtonLazada = findElementXPathConditionExist("//button[@class='search-box__button--1oH7']", driver);
		searchButtonLazada.click();
		List<WebElement> productsLazada = findElementsXPathConditionExist("//div[@class='Ms6aG MefHh']", driver);
		productsLazada.get(0).click();
		WebElement nameLazada = findElementXPathConditionExist("//h1[@class='pdp-mod-product-badge-title']", driver);
		WebElement priceLazada = findElementXPathConditionExist("//span[@class = 'pdp-price pdp-price_type_normal pdp-price_color_orange pdp-price_size_xl']", driver);
		CompareObject lazada = new CompareObject();
		lazada.setWebsite("Lazada");
		lazada.setNameProduct(nameLazada.getText());
		Long priceIphone11Lazada = Long.valueOf(priceLazada.getText().substring(1, 11).replace(",", ""));
		lazada.setPriceProduct(priceIphone11Lazada);
		lazada.setUrlProduct(driver.getCurrentUrl());

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String tikiString = ow.writeValueAsString(tiki);
		String lazadaString = ow.writeValueAsString(lazada);
		if (priceIphone11Tiki < priceIphone11Lazada) {
			System.out.println(lazadaString);
			System.out.println(tikiString);
		} else {
			System.out.println(tikiString);
			System.out.println(lazadaString);
		}
	}

	private WebElement findElementXPathConditionExist(String xPathName, WebDriver driver) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.TIME_WAIT_FIND_ELEMENT_EXIST_DOM);
			By ByElement = getByElementXPath(xPathName);
			return wait.until(ExpectedConditions.presenceOfElementLocated(ByElement));
		} catch (TimeoutException ex) {
			return null;
		}
	}

	private List<WebElement> findElementsXPathConditionExist(String xPathName, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.TIME_WAIT_FIND_ELEMENT_EXIST_DOM);
			By ByElement = getByElementXPath(xPathName);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(ByElement));
			return element.findElements(ByElement);
		} catch (Exception ex) {
			return null;
		}

	}

	private By getByElementXPath(String xPathName) {
		return By.xpath(xPathName);
	}

	private boolean isLoadingPage(WebDriver driver) {
		boolean check = false;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (Constants.IS_NOT_FINISH_LOADING_PAGE) {
			check = js.executeScript("return document.readyState").equals("complete");
			if (check) {
				break;
			}
		}
		return check;
	}
}