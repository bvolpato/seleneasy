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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Seleneasy Tests
 * 
 * @author Bruno Candido Volpato da Cunha
 *
 */
public class SeleneasyTest {

    private WebDriver driver;

    //@Before
    public void setup() {
        driver = new FirefoxDriver();
    }

    //@After
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    //@Test
    public void testSimple() throws Exception {

        Seleneasy seleneasy = new Seleneasy(driver);
        seleneasy.setDefaultWaitInSeconds(5);
        seleneasy.loadCookies(new File("/tmp/cookies.txt"));

        Document document = seleneasy.getDocument("https://github.com/brunocvcunha");
        assertEquals("Bruno Volpato", document.select("span.vcard-fullname").text());

        seleneasy.waitClickable(By.cssSelector("a[href='/brunocvcunha?tab=stars']"), true);

        Boolean onRightPage = seleneasy.waitForCondition(ExpectedConditions.urlContains("tab=stars"));
        assertTrue(onRightPage);
        assertEquals("https://github.com/brunocvcunha?tab=stars", seleneasy.getUrl());

    }

}