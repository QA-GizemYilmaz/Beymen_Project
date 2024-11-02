import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExcelReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePageTest {
    static WebDriver driver;


    @Before
    public void setup(){
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe");
        WebDriverManager.chromedriver().setup();


    }
    @Test
    public void checkHeader(){
        driver = new ChromeDriver();
        driver.get("https://www.beymen.com/tr");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isTitleCorrect = wait.until(ExpectedConditions.titleContains("Beymen"));
        Assert.assertTrue("Ana sayfa açılmadı!", isTitleCorrect);
    }


    @Test
    public void testExcelData() {
        //List<String> terms = new ArrayList<>();
        String excelFilePath = "src/main/resources/data.xlsx";
        ExcelReader excelReader = new ExcelReader(excelFilePath);
        String searchTerm1 = excelReader.getData(0, 0, 0);
        //terms.add(searchTerm1);
        System.out.println("Arama Terimi 1: " + searchTerm1);
        String searchTerm2 = excelReader.getData(0, 0, 1);
        //terms.add(searchTerm2);
        System.out.println("Arama Terimi 2: " + searchTerm2);
        //System.out.println(terms);
        excelReader.close();
        //return  searchTerm1,searchTerm2;
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
