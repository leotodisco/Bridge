// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class TC8Test {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void tC8() {
    driver.get("http://localhost:5174/");
    driver.manage().window().setSize(new Dimension(782, 823));
    driver.findElement(By.cssSelector(".sidebar-toggle-btn")).click();
    driver.findElement(By.cssSelector(".menu-item > span")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("li:nth-child(2) span"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.linkText("Consulenze")).click();
    driver.findElement(By.cssSelector(".sidebar-overlay")).click();
    driver.findElement(By.cssSelector(".btn")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".btn"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    driver.findElement(By.cssSelector("form > .formEditText:nth-child(3)")).click();
    driver.findElement(By.cssSelector("form > .formEditText:nth-child(3)")).click();
    driver.findElement(By.cssSelector("form > .formEditText:nth-child(3)")).sendKeys("Consulenza legale per rifugiati");
    driver.findElement(By.cssSelector(".formEditText:nth-child(4)")).click();
    driver.findElement(By.cssSelector(".formEditText:nth-child(4)")).sendKeys("Consulenza per documenti, permessi di soggiorno, pratiche fiscali e gestione amministrativa");
    driver.findElement(By.cssSelector("button:nth-child(6)")).click();
    driver.findElement(By.cssSelector("select:nth-child(1)")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector("select:nth-child(1)"));
      dropdown.findElement(By.xpath("//option[. = 'Lunedi']")).click();
    }
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(1) > input:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(1) > input:nth-child(2)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(1) > input:nth-child(2)"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(1) > input:nth-child(2)")).sendKeys("12:30");
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(1) > input:nth-child(3)")).sendKeys("14:03");
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(1) > input:nth-child(3)")).sendKeys("14:30");
    driver.findElement(By.cssSelector(".formEditText:nth-child(8)")).click();
    driver.findElement(By.cssSelector(".formEditText:nth-child(8)")).sendKeys("3202347075\\");
    driver.findElement(By.cssSelector(".formEditText:nth-child(8)")).sendKeys("3202347075");
    driver.findElement(By.cssSelector(".formEditText:nth-child(9)")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector(".formEditText:nth-child(9)"));
      dropdown.findElement(By.xpath("//option[. = 'LEGALE']")).click();
    }
    driver.findElement(By.cssSelector(".candidatureInput")).click();
    driver.findElement(By.cssSelector(".candidatureInput")).sendKeys("4");
    driver.findElement(By.cssSelector("form")).click();
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(13) > .formEditText:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(13) > .formEditText:nth-child(1)")).sendKeys("Via Roma");
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(13) > .formEditText:nth-child(2)")).sendKeys("24");
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(14) > .formEditText:nth-child(1)")).sendKeys("Roma");
    driver.findElement(By.cssSelector(".inlineCityDetails:nth-child(14) > .formEditText:nth-child(2)")).sendKeys("11118");
    driver.findElement(By.cssSelector(".inlineCityDetails > .formEditText:nth-child(3)")).sendKeys("R");
    driver.findElement(By.id("creaConsulenzaButton")).click();
  }
}
