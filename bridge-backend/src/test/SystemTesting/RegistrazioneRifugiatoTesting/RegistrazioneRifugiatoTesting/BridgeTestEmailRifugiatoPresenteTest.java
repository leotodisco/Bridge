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
public class BridgeTestEmailRifugiatoPresenteTest {
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
  public void bridgeTestEmailRifugiatoPresente() {
    driver.get("http://localhost:5174/login");
    driver.manage().window().setSize(new Dimension(1710, 981));
    driver.findElement(By.id("email")).sendKeys("mariozurolo00@gmail.com");
    driver.findElement(By.id("password")).sendKeys("Prova123!");
    driver.findElement(By.cssSelector(".formLink")).click();
    driver.findElement(By.cssSelector(".formEditText:nth-child(10)")).sendKeys("mariozurolo00@gmail.com");
    driver.findElement(By.cssSelector(".password-field:nth-child(11) > .formEditText")).sendKeys("Prova123!");
    driver.findElement(By.cssSelector("form > .formEditText:nth-child(1)")).click();
    driver.findElement(By.cssSelector("form > .formEditText:nth-child(1)")).sendKeys("Mario");
    driver.findElement(By.cssSelector(".formEditText:nth-child(2)")).sendKeys("Zurolo");
    driver.findElement(By.cssSelector(".formEditText:nth-child(3)")).sendKeys("0002-07-30");
    driver.findElement(By.cssSelector(".formEditText:nth-child(3)")).sendKeys("0020-07-30");
    driver.findElement(By.cssSelector(".formEditText:nth-child(3)")).sendKeys("0200-07-30");
    driver.findElement(By.cssSelector(".formEditText:nth-child(3)")).sendKeys("2000-07-30");
    driver.findElement(By.cssSelector(".formEditText:nth-child(4)")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector(".formEditText:nth-child(4)"));
      dropdown.findElement(By.xpath("//option[. = 'Maschio']")).click();
    }
    driver.findElement(By.cssSelector(".formEditText:nth-child(5)")).click();
    driver.findElement(By.cssSelector(".formEditText:nth-child(5)")).sendKeys("Francese");
    driver.findElement(By.cssSelector(".formEditText:nth-child(6)")).sendKeys("Italiano Francese Inglese");
    driver.findElement(By.cssSelector(".formEditText:nth-child(7)")).sendKeys("Falegnameria, Lavorare in team");
    driver.findElement(By.cssSelector(".formEditText:nth-child(8)")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector(".formEditText:nth-child(8)"));
      dropdown.findElement(By.xpath("//option[. = 'Laurea']")).click();
    }
    driver.findElement(By.cssSelector(".formEditText:nth-child(9)")).click();
    {
      WebElement dropdown = driver.findElement(By.cssSelector(".formEditText:nth-child(9)"));
      dropdown.findElement(By.xpath("//option[. = 'Rifugiato']")).click();
    }
    driver.findElement(By.cssSelector(".password-field:nth-child(11) > .formEditText")).click();
    driver.findElement(By.cssSelector(".password-field:nth-child(11) > .formEditText")).click();
    {
      WebElement element = driver.findElement(By.cssSelector(".password-field:nth-child(11) > .formEditText"));
      Actions builder = new Actions(driver);
      builder.doubleClick(element).perform();
    }
    driver.findElement(By.id("conferma-password")).click();
    driver.findElement(By.id("conferma-password")).sendKeys("Prova123!");
    driver.findElement(By.cssSelector(".formEditText:nth-child(13)")).click();
    driver.findElement(By.cssSelector(".formEditText:nth-child(13)")).sendKeys("C:\\fakepath\\tiger-jpg.jpg");
    driver.findElement(By.cssSelector(".formButton")).click();
  }
}
