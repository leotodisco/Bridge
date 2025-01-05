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
public class BridgeTestInserimentoLavoroCAPErratoTest {
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
  public void bridgeTestInserimentoLavoroCAPErrato() {
    driver.get("http://localhost:5174/");
    driver.manage().window().setSize(new Dimension(1680, 962));
    driver.findElement(By.cssSelector(".section:nth-child(2) h3")).click();
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
    driver.findElement(By.id("titolo")).click();
    driver.findElement(By.id("titolo")).sendKeys("Cercasi Sviluppatore Software");
    driver.findElement(By.cssSelector(".switch-slider")).click();
    driver.findElement(By.id("maxCandidature")).click();
    driver.findElement(By.id("maxCandidature")).sendKeys("20");
    driver.findElement(By.id("posizione")).click();
    driver.findElement(By.id("posizione")).sendKeys("Sviluppatore Software");
    driver.findElement(By.id("nomeAzienda")).click();
    driver.findElement(By.id("nomeAzienda")).sendKeys("Azienda Informatica");
    driver.findElement(By.cssSelector(".inlineCityDetails > input:nth-child(1)")).click();
    driver.findElement(By.cssSelector(".inlineCityDetails > input:nth-child(1)")).sendKeys("09:00");
    driver.findElement(By.cssSelector(".inlineCityDetails > input:nth-child(2)")).click();
    driver.findElement(By.cssSelector(".inlineCityDetails > input:nth-child(2)")).sendKeys("18:00");
    driver.findElement(By.id("tipoContratto")).click();
    {
      WebElement dropdown = driver.findElement(By.id("tipoContratto"));
      dropdown.findElement(By.xpath("//option[. = 'Apprendistato']")).click();
    }
    driver.findElement(By.id("retribuzione")).click();
    driver.findElement(By.id("retribuzione")).sendKeys("3000");
    driver.findElement(By.id("nomeSede")).click();
    driver.findElement(By.id("nomeSede")).sendKeys("Sede");
    driver.findElement(By.id("infoUtili")).click();
    driver.findElement(By.id("infoUtili")).sendKeys("unc9cnnr30cn30crn3");
    driver.findElement(By.cssSelector("input:nth-child(3)")).click();
    driver.findElement(By.cssSelector("input:nth-child(3)")).sendKeys("Via");
    driver.findElement(By.cssSelector("input:nth-child(5)")).click();
    driver.findElement(By.cssSelector("input:nth-child(5)")).sendKeys("13");
    driver.findElement(By.cssSelector("input:nth-child(7)")).click();
    driver.findElement(By.cssSelector("input:nth-child(7)")).sendKeys("Salerno");
    driver.findElement(By.cssSelector("input:nth-child(9)")).click();
    driver.findElement(By.cssSelector("input:nth-child(9)")).sendKeys("12A45");
    driver.findElement(By.cssSelector("input:nth-child(11)")).click();
    driver.findElement(By.cssSelector("input:nth-child(11)")).sendKeys("SA");
    driver.findElement(By.cssSelector("button:nth-child(12)")).click();
    {
      WebElement element = driver.findElement(By.cssSelector("button:nth-child(12)"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = driver.findElement(By.tagName("body"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element, 0, 0).perform();
    }
  }
}
