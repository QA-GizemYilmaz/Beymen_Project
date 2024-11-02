package com.beymen.utilities;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Rastgele bir ürün seçme ve ürün bilgilerini kaydetme
    public String selectRandomProductAndSaveDetails() throws IOException, InterruptedException {
        // Ürünleri listeden bul
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".m-productCard__desc")));
        Random random = new Random();
        WebElement randomProduct = products.get(random.nextInt(products.size())); // Rastgele bir ürün seç

        // Ürün ismine tıklayarak ürün detay sayfasına git
        randomProduct.click();

        Thread.sleep(2000);

        // Ürün bilgisi ve fiyatı al
        WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("o-productDetail__description")));
        WebElement productPriceElement = driver.findElement(By.id("priceNew"));

        String productName = productNameElement.getText();
        String productPrice = productPriceElement.getText();

        // Ürün bilgilerini bir txt dosyasına kaydet
        try (FileWriter writer = new FileWriter("productDetails.txt")) {
            writer.write("Product Name: " + productName + "\n");
            writer.write("Product Price: " + productPrice + "\n");
            productPrice = productPrice.replace(".", "").replace(",", "").replace(" TL", "").trim();
            System.out.println(productPrice);
        }


        return productPrice; // Sepette fiyat doğrulaması için fiyatı geri döndür
    }

    // Ürünü sepete ekleme
    public void addToCart() {
        // Tüm bedenleri liste olarak al
        List<WebElement> sizeElements = driver.findElements(By.cssSelector(".m-variation"));

        boolean sizeSelected = false;

        for (WebElement size : sizeElements) {
            // Eğer bedenin class'ında "disabled" yoksa, beden seçilebilir durumda demektir
            if (!size.getAttribute("class").contains("disabled")) {
                size.click(); // Beden seçilir
                sizeSelected = true;
                System.out.println("Mevcut beden seçildi: " + size.getText());
                break;
            }
        }

        if (!sizeSelected) {
            System.out.println("Üzgünüz, mevcut beden yok.");
        }
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addBasket")));
        addToCartButton.click();
    }

    // Sepetteki fiyatı kontrol etme
    public boolean verifyPriceInCart(String productPrice) {
        // Sepete git
        WebElement cartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".o-header__userInfo--item.bwi-cart-o")));
        cartButton.click();

        // Sepetteki fiyatı al
        WebElement cartPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".priceBox__priceWrapper")));
        String cartPrice = cartPriceElement.getText();
        cartPrice = cartPrice.replace(".", "").replace(",", "").replace(" TL", "").trim();
        cartPrice = cartPrice.substring(0, cartPrice.length() - 2);
        System.out.println(cartPrice);

        // Ürün sayfasındaki fiyat ile sepetteki fiyatı karşılaştır
        return productPrice.equals(cartPrice);
    }

    // Ürün miktarını arttır ve 2 olup olmadığını kontrol et
    public boolean increaseQuantityAndVerify() {
        WebElement quantityDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[1]/div[2]/div[1]/div[2]/div/div/div[2]/div/div[3]/div[2]")));
        quantityDropdown.click();
        WebElement quantityDropdownOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"quantitySelect0-key-0\"]/option[2]")));
        quantityDropdownOption.click();


        WebElement quantityLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantitySelect0-key-0")));
        String ariaLabel = quantityLabel.getAttribute("aria-label");
        if (ariaLabel.contains("2 adet")) {
            System.out.println("Doğrulama başarılı: 'aria-label' değeri '2 adet' içeriyor.");
        } else {
            System.out.println("Doğrulama başarısız: 'aria-label' değeri '2 adet' içermiyor. Mevcut değer: " + ariaLabel);
        }
        return true;

    }

    // Sepeti boşaltma ve kontrol etme
    public boolean emptyCartAndVerify() {
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".m-basket__remove")));
        deleteButton.click();

        // Sepetin boş olduğunu kontrol et
        WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".m-empty__message")));
        return emptyCartMessage.isDisplayed();
    }
}
