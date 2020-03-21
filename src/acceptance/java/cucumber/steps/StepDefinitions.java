package cucumber.steps;

import static org.assertj.core.api.Assertions.*;

import io.cucumber.java8.En;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class StepDefinitions implements En {
  private final ChromeOptions options =
      new ChromeOptions().addArguments("--ignore-certificate-errors", "--headless");
  private final WebDriver driver = new ChromeDriver(options);
  private final String BASE_URI =
      String.format(
          "https://%s:%s", System.getProperty("testHost"), System.getProperty("testPort"));

  public StepDefinitions() {
    When(
        "user visits {string} page",
        (String page) -> {
          driver.get(BASE_URI + page);
        });

    Then(
        "page contains elements by {string}",
        (String cssSelector) -> {
          List<WebElement> results = driver.findElements(By.cssSelector(cssSelector));
          assertThat(results)
              .as("Number of elements found with css-selector '%s'", cssSelector)
              .size()
              .isGreaterThan(0);
        });
  }
}
