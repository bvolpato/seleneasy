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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
@AllArgsConstructor
public class Seleneasy {

    private RemoteWebDriver driver;

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
            driver = new SafariDriver();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                driver.quit();
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
     * @return Document
     */
    public Document getDocument() {
        return Jsoup.parse(driver.getPageSource());
    }

    /**
     * Execute JavaScript
     * @param script Script to execute
     * @return Script result
     */
    public Object executeJavaScript(String script) {
        return driver.executeScript(script);
    }
    
    /**
     * Scroll to bottom of the page
     */
    public void scrollToBottom() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight);return true");
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
}