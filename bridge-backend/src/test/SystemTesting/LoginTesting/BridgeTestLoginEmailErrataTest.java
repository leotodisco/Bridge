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
public class BridgeTestLoginEmailErrataTest {
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
  public void bridgeTestLoginEmailErrata() {
    // Test name: BridgeTestLoginEmailErrata
    // Step # | name | target | value | comment
    // 1 | open | /login |  | 
    driver.get("http://localhost:5174/login");
    // 2 | setWindowSize | 789x864 |  | 
    driver.manage().window().setSize(new Dimension(789, 864));
    // 3 | click | id=email |  | 
    driver.findElement(By.id("email")).click();
    // 4 | type | id=email | g.montella@email.falsa | 
    driver.findElement(By.id("email")).sendKeys("g.montella@email.falsa");
    // 5 | click | id=password |  | 
    driver.findElement(By.id("password")).click();
    // 6 | type | id=password | 123Password! | 
    driver.findElement(By.id("password")).sendKeys("123Password!");
    // 7 | click | css=.formButton |  | 
    driver.findElement(By.cssSelector(".formButton")).click();
  }
}
