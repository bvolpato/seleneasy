/**
 * Copyright (C) 2017 Bruno Candido Volpato da Cunha (brunocvcunha@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brunocvcunha.seleneasy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.InvalidCookieDomainException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

/**
 * Seleneasy API. Brings some new functionalities on top of selenium WebDriver.
 * 
 * @author Bruno Candido Volpato da Cunha
 *
 */
@Log4j
@Getter
@Setter
@RequiredArgsConstructor
public class Seleneasy {

    @NonNull
    private WebDriver driver;

    private int defaultWaitInSeconds = 10;
    
    /**
     * Constructor without specifying the driver, will use a default one
     */
    public Seleneasy() {
        setup();
    }

    /**
     * Sets up the driver
     */
    public void setup() {
        if (driver == null) {
            driver = new FirefoxDriver();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    driver.quit();
                } catch (Exception e) {
                    //it's ok.
                }
            }));
        }
    }

    /**
     * Navigates the browser to the specified URL
     * 
     * @param url
     *            URL to navigate
     */
    public void open(String url) {
        log.info("Navigating to [" + url + "]");
        driver.get(url);
    }

    /**
     * Gets the DOM of the url
     * 
     * @param url
     *            URL
     * @return DOM
     */
    public String getPageSource(String url) {
        log.info("Getting DOM of URL: [" + url + "]");

        open(url);
        return driver.getPageSource();
    }
    
    /**
     * Gets the DOM of the current page
     * 
     * @return DOM
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * @return takes a screenshot
     */
    public File takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    }

    /**
     * Take a screenshot and save into the destination
     * 
     * @param destFile
     *            Destination file
     * @throws IOException
     */
    public void takeScreenshot(File destFile) throws IOException {
        File scrFile = takeScreenshot();
        FileUtils.copyFile(scrFile, destFile);
    }

    /**
     * 
     */
    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * Gets the DOM of the url
     * 
     * @param url
     *            URL
     * @return Document
     */
    public Document getDocument(String url) {
        return Jsoup.parse(getPageSource(url));
    }

    /**
     * Gets the document of the current url
     * 
     * @return Document
     */
    public Document getDocument() {
        return Jsoup.parse(driver.getPageSource());
    }

    /**
     * Execute JavaScript
     * 
     * @param script
     *            Script to execute
     * @return Script result
     */
    public Object executeJavaScript(String script) {
        return ((JavascriptExecutor) driver).executeScript(script);
    }

    /**
     * Scroll to top of the page
     */
    public void scrollToTop() {
        executeJavaScript("window.scrollTo(0, 0);return true");
    }
    
    /**
     * Scroll to bottom of the page
     */
    public void scrollToBottom() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight);return true");
    }

    /**
     * Wait for element to be visible using the default wait, and return it
     * 
     * @param by
     *            Selector
     * @return Element
     * @throws Exception
     */
    public WebElement waitVisible(By by) throws Exception {
        return waitVisible(by, defaultWaitInSeconds);
    }
    
    /**
     * Wait for element to be visible, and return it
     * 
     * @param by
     *            Selector
     * @param timeoutinSeconds
     *            Max time to wait
     * @return Element
     * @throws Exception
     */
    public WebElement waitVisible(By by, int timeoutinSeconds) throws Exception {
        return new WebDriverWait(driver, timeoutinSeconds).until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    
    /**
     * Wait for element to be visible using the default wait, and return it
     * 
     * @param condition
     *            Condition
     * @return Element
     * @throws Exception
     */
    public <T> T waitForCondition(ExpectedCondition<T> condition) throws Exception {
        return waitForCondition(condition, defaultWaitInSeconds);
    }
    
    /**
     * Wait for element to be visible, and return it
     * 
     * @param condition
     *            Condition
     * @param timeoutinSeconds
     *            Max time to wait
     * @return Element
     * @throws Exception
     */
    public <T> T waitForCondition(ExpectedCondition<T> condition, int timeoutinSeconds) throws Exception {
        return new WebDriverWait(driver, timeoutinSeconds).until(condition);
    }

    /**
     * Wait for element to be visible using the default wait, click if param says so and return it
     * 
     * @param by
     *            Selector
     * @param click
     *            Should click on element
     * @return Element
     * @throws Exception
     */
    public WebElement waitClickable(By by, boolean click) throws Exception {
        return waitClickable(by, click, defaultWaitInSeconds);
    }
    
    /**
     * Save the cookies to a file
     * @param file File to save
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void saveCookies(File file) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(driver.manage().getCookies());
        oos.close();
    }
    
    /**
     * Load the cookies from a file
     * @param file File to read
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ClassNotFoundException 
     */
    public void loadCookies(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.loadCookies(file, null);
    }
    
    
    /**
     * Load the cookies from a file
     * @param file File to read
     * @param domain Domain to import cookies
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ClassNotFoundException 
     */
    public void loadCookies(File file, String domain) throws FileNotFoundException, IOException, ClassNotFoundException {
        if (!file.exists()) {
            return;
        }
        
        ObjectInputStream ios = new ObjectInputStream(new FileInputStream(file));
        Set<Cookie> cookies = (Set<Cookie>) ios.readObject();
        ios.close();
        
        for (Cookie cookie : cookies) {
            log.info("Loading cookie: " + cookie.toString());
            
            try {
                driver.manage().addCookie(cookie);
            } catch (InvalidCookieDomainException e) {
                
                e.printStackTrace();
                if (domain != null) {
                    
                    if (cookie.getDomain().startsWith(".") && domain.indexOf(cookie.getDomain()) != -1) {
                        Cookie cookie2 = new Cookie(cookie.getDomain(), cookie.getValue(), domain, cookie.getPath(), cookie.getExpiry(), cookie.isSecure(), cookie.isHttpOnly());
                        driver.manage().addCookie(cookie2);
                    }
                    
                } else {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Wait for element to be visible, click if param says so and return it
     * 
     * @param by
     *            Selector
     * @param click
     *            Should click on element
     * @param timeoutinSeconds
     *            Max time to wait
     * @return Element
     * @throws Exception
     */
    public WebElement waitClickable(By by, boolean click, int timeoutinSeconds) throws Exception {

        WebElement element = new WebDriverWait(driver, timeoutinSeconds)
                .until(ExpectedConditions.elementToBeClickable(by));

        if (click) {
            element.click();
        }

        return element;
    }

    /**
     * Shuts down the driver
     */
    public void shutdown() {
        try {
            driver.quit();
        } catch (Exception e) {
            log.warn("Error shutting down the driver", e);
        }
    }

    /**
     * @return Current URL
     */
    public String getUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * @return screenshot
     */
    public File getScreenshot() {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        return screenshotFile;
    }
    
    /**
     * @return screenshot
     */
    public byte[] getScreenshotBytes() {
        byte[] screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        return screenshotFile;
    }

    
}