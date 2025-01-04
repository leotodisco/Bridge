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
public class CorrettoTest {
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
  public void corretto() {
    driver.get("http://localhost:5174/");
    driver.manage().window().setSize(new Dimension(945, 1012));
    driver.findElement(By.cssSelector(".sidebar-toggle-btn")).click();
    driver.findElement(By.cssSelector(".menu-item > span")).click();
    {
      WebElement element = driver.findElement(By.linkText("Area Personale"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.linkText("Corsi")).click();
    {
      WebElement element = driver.findElement(By.linkText("Eventi"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.cssSelector(".sidebar-overlay")).click();
    driver.findElement(By.cssSelector(".btn > svg")).click();
    driver.findElement(By.cssSelector(".formField:nth-child(1) > .InputField")).click();
    driver.findElement(By.cssSelector(".formField:nth-child(1) > .InputField")).sendKeys("Corso di Italiano");
    driver.findElement(By.cssSelector(".formField:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".formField:nth-child(2) > .InputField")).click();
    driver.findElement(By.cssSelector(".formField:nth-child(2) > .InputField")).sendKeys("Questo è un corso introduttivo all’apprendimento della lingua italiana");
    driver.findElement(By.cssSelector(".formField:nth-child(3) > .InputField")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector(".formField:nth-child(3) > .InputField"));
      dropdown.findElement(By.xpath("//option[. = 'LINGUE']")).click();
    }
    driver.findElement(By.cssSelector(".formField:nth-child(4) > .InputField")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector(".formField:nth-child(4) > .InputField"));
      dropdown.findElement(By.xpath("//option[. = 'ITALIANO']")).click();
    }
    driver.findElement(By.cssSelector(".fileButton:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".fileButton:nth-child(6)")).click();
  }
}
