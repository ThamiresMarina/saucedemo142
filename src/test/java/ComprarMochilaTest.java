import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ComprarMochilaTest {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUpBeforeClass() {
        
        WebDriverManager.chromedriver().clearResolutionCache().driverVersion("130.0").setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("ChromeDriver inicializado com sucesso!");
    }

    @AfterAll
    public static void tearDownAfterClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testComprarMochila() {
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().setSize(new Dimension(1552, 928));
        
        // Login
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("*[data-test=\"username\"]"))).sendKeys("standard_user");
        driver.findElement(By.cssSelector("*[data-test=\"password\"]")).sendKeys("secret_sauce");
        driver.findElement(By.cssSelector("*[data-test=\"login-button\"]")).click();
        
        // Verifica se o login foi bem-sucedido
        WebElement productsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".title")));
        Assertions.assertEquals("Products", productsHeader.getText(), "Login failed or unexpected page.");

        // Adiciona ao carrinho
        driver.findElement(By.cssSelector("*[data-test=\"add-to-cart-sauce-labs-backpack\"]")).click();
        driver.findElement(By.cssSelector("*[data-test=\"shopping-cart-badge\"]")).click();

        // Verifica se o item está no carrinho
        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart_item")));
        Assertions.assertNotNull(cartItem, "Item was not added to the cart.");

        // Checkout
        driver.findElement(By.cssSelector("*[data-test=\"checkout\"]")).click();
        driver.findElement(By.cssSelector("*[data-test=\"firstName\"]")).sendKeys("THAMIRES");
        driver.findElement(By.cssSelector("*[data-test=\"lastName\"]")).sendKeys("DOS SANTOS");
        driver.findElement(By.cssSelector("*[data-test=\"postalCode\"]")).sendKeys("1750-002");
        driver.findElement(By.cssSelector("*[data-test=\"continue\"]")).click();
        driver.findElement(By.cssSelector("*[data-test=\"finish\"]")).click();

        // Verifica se a compra foi finalizada
        WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".complete-header")));
        Assertions.assertEquals("Thank you for your order!", confirmationMessage.getText(), "Purchase was not completed successfully.");

        System.out.println("Teste concluído com sucesso!");
    }
}

    