import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExcelReader;
import com.beymen.utilities.ProductPage;


import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageTest {
    static WebDriver driver;

    @Before
    public void setup(){
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2); // 1: izin ver, 2: engelle
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.beymen.com/tr");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement AcceptPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        AcceptPopup.click();
        WebElement KADIN = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("genderWomanButton")));
        KADIN.click();



    }
    @Test
    public void checkHeader(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isTitleCorrect = wait.until(ExpectedConditions.titleContains("Beymen"));
        Assert.assertTrue("Ana sayfa açılmadı!", isTitleCorrect);
    }

    @Test
    public void testExcelData() {
        String excelFilePath = "src/main/resources/data.xlsx";
        ExcelReader excelReader = new ExcelReader(excelFilePath);
        String searchTerm1 = excelReader.getData(0, 0, 0);
        System.out.println("Arama Terimi 1: " + searchTerm1);
        String searchTerm2 = excelReader.getData(0, 0, 1);
        System.out.println("Arama Terimi 2: " + searchTerm2);
        excelReader.close();

    }

    @Test
    public void searchForShorts() throws InterruptedException, IOException {
        //enter the value of "şort" and clear it and enter "gömlek" as a value
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/header/div/div/div[2]/div/div/input"))).click();
        WebElement SearchArea = driver.findElement(By.xpath("//*[@id=\"o-searchSuggestion__input\"]"));
        SearchArea.sendKeys("şort");
        Thread.sleep(2000);
        SearchArea.clear();
        Thread.sleep(2000);
        SearchArea.sendKeys("gömlek");
        SearchArea.sendKeys(Keys.ENTER);

        // verify searching is successful
        WebElement my = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"productListTitle\"]")));
        String resultTitle = my.getText();
        Thread.sleep(2000);
        if (resultTitle.contains("gömlek") && resultTitle.contains("bulundu")) {
            System.out.println("Search successful");
        } else {
            System.out.println("Search failed");
        }

        Thread.sleep(2000);
        ProductPage productPage = new ProductPage(driver);
        String productPrice = productPage.selectRandomProductAndSaveDetails();
        productPage.addToCart();
        Assert.assertTrue("Fiyat doğrulaması başarısız!", productPage.verifyPriceInCart(productPrice));
        Assert.assertTrue("Ürün miktarı doğrulaması başarısız!", productPage.increaseQuantityAndVerify());
        Assert.assertTrue("Sepet boş değil!", productPage.emptyCartAndVerify());

    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
