package jar;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Very simple test that uses Selenium Grid.
 *
 * @author Kohsuke Kawaguchi
 */
public class Selenium2Test extends TestCase {
    public void testFoo() throws Exception {
        WebDriver wd =  new Augmenter().augment(new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), DesiredCapabilities.firefox()));
        try {
            wd.get("http://www.yahoo.com/");
            wd.findElement(By.name("p")).sendKeys("hello world");
            wd.findElement(By.id("search-submit")).click();
//        wd.waitForPageToLoad("10000");

            assertTrue(wd.getTitle().contains("hello world"));
            assertTrue(wd.getTitle().contains("Yahoo"));

            System.out.println("[[ATTACHMENT|" +
            ((TakesScreenshot) wd).getScreenshotAs(PERSISTENT_FILE)+ "]]");
        } finally {
            wd.close();
        }
    }

    private static final OutputType<File> PERSISTENT_FILE
            = new OutputType<File>() {
        public File convertFromBase64Png(String base64Png) {
            return save(BYTES.convertFromBase64Png(base64Png));
        }

        public File convertFromPngBytes(byte[] data) {
            return save(data);
        }

        private File save(byte[] data) {
            try {
                File tmpFile = Files.createTempFile("screenshot", ".png").toFile();
                FileUtils.writeByteArrayToFile(tmpFile, data);
                return tmpFile;
            } catch (IOException e) {
                throw new WebDriverException(e);
            }
        }
    };
}
