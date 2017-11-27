import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;

public class Main {

    private static WebDriver driver;

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();

        String intranetUrl = "https://intranet.upv.es/pls/soalu/est_intranet.Ni_portal_n?P_IDIOMA=i";

        driver.get(intranetUrl);

        driver.findElement(By.name("dni")).sendKeys("añsdlfja");
        System.out.println("Writing wrong alphabetic user . . .");

        driver.findElement(By.name("clau")).sendKeys("qwer");
        System.out.println("Writing wrong alphabetic password . . .");

        driver.findElement(By.xpath("//tbody/tr[3]/td/input")).click();
        fluentWait(By.id("DIVescudoN3"));

        try {
            driver.findElement(By.className("upv_textoerror"));
            System.out.println("\tTest result: PASS. Wrong user and password.\n");
            driver.findElement(By.xpath("//div[1]/div[2]/table/tbody/tr/td/a")).click();
        } catch (NoSuchElementException e){
            System.out.println("\tTest result: FAIL. Closing.\n");
            driver.close();
            System.exit(0);
        }

        //CHANGE DNI TO 12345678 TO SEE ANOTHER TYPE OF ERROR.
        driver.findElement(By.name("dni")).sendKeys("87654321");
        System.out.println("Writing wrong numeric user . . .");

        driver.findElement(By.name("clau")).sendKeys("1234");
        System.out.println("Writing wrong numeric password . . .");

        driver.findElement(By.xpath("//tbody/tr[3]/td/input")).click();
        fluentWait(By.id("DIVescudoN3"));

        try {
            driver.findElement(By.className("upv_textoerror"));
            System.out.println("\tTest result: PASS. Wrong user and password.\n");
            driver.findElement(By.xpath("//div[1]/div[2]/table/tbody/tr/td/a")).click();
        } catch (NoSuchElementException e){
            System.out.println("\tTest result: FAIL. Closing.\n");
            driver.close();
            System.exit(0);
        }

        //CHANGE DNI TO A CORRECT ONE TO MAKE IT WORKS.
        driver.findElement(By.name("dni")).sendKeys("********");
        System.out.println("Writing correct numeric user . . .");

        //CHANGE PW TO A CORRECT ONE TO MAKE IT WORKS.
        driver.findElement(By.name("clau")).sendKeys("****");
        System.out.println("Writing correct numeric password . . .");

        driver.findElement(By.xpath("//tbody/tr[3]/td/input")).click();
        fluentWait(By.id("DIVescudoN2"));

        try {
            driver.findElement(By.id("DIVbannerN2"));
            System.out.println("\tTest result: PASS. Correct user and password. Logged in.\n");
        } catch (NoSuchElementException e){
            System.out.println("\tTest result: FAIL. Closing.");
            driver.close();
            System.exit(0);
        }

        driver.findElement(By.linkText("Intranet")).click();
        fluentWait(By.id("int_principal"));
        System.out.println("Entering to Intranet . . .");

        driver.findElement(By.linkText("Cita previa consulta médica")).click();
        fluentWait(By.id("int_contenedor"));

        driver.findElement(By.id("p_campo_213365_10")).sendKeys("99/99/9999 99:99");
        System.out.println("Trying to date at an invalid date . . .");

        driver.findElement(By.xpath("//tbody/tr[3]/td/input")).click();
        fluentWait(By.id("int_contenedor"));
        System.out.println("\tTest result: PASS. Invalid date\n");

        driver.findElement(By.xpath("//div/div[2]/table/tbody/tr/td/a")).click();
        fluentWait(By.id("int_contenedor"));

        String parentHandle = driver.getWindowHandle(); //Get the current window handle
        driver.findElement(By.className("upv_enlace")).click();

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);        //Switch focus of WebDriver to the next found window handle (that's your newly opened window)
        }

        fluentWait(By.partialLinkText("Primera"));

        driver.findElement(By.partialLinkText("Primera")).click();
        System.out.println("Trying to date at the next available date . . .");
        fluentWait(By.id("pagina"));

        driver.findElement(By.linkText("Aceptar")).click();

        driver.switchTo().window(parentHandle);         //Switch again to the parent window handle

        fluentWait(By.id("int_contenedor"));

        driver.findElement(By.xpath("//tbody/tr[3]/td/input")).click();
        fluentWait(By.id("contenido"));
        System.out.println("\tTest result: PASS. Correct date. Dated on: " +
                driver.findElement(By.xpath("//tbody/tr[3]/td")).getText() + ".\n");

        driver.findElement(By.linkText("Cerrar")).click();
        fluentWait(By.id("int_principal"));

        driver.findElement(By.linkText("Ver citas médicas solicitadas")).click();
        fluentWait(By.id("contenido"));

        driver.findElement(By.partialLinkText("Eliminar")).click();
        System.out.println("Trying to delete the dating . . .");
        driver.switchTo().alert().accept();
        System.out.println("\tTest result: PASS. Dating deleted.\n");
        System.out.println("Closing . . .");
        driver.close();
        System.exit(0);
    }

    public static WebElement fluentWait(final By locator) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });

        return foo;
    }
}
