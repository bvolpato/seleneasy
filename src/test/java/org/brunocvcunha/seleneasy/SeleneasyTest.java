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

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Seleneasy Tests
 * 
 * @author Bruno Candido Volpato da Cunha
 *
 */
public class SeleneasyTest {

    @Test
    public void testSimple() throws Exception {
        Seleneasy seleneasy = new Seleneasy();
        seleneasy.setDefaultWaitInSeconds(5);
        
        Document document = seleneasy.getDocument("https://github.com/brunocvcunha");
        assertEquals("Bruno Candido Volpato da Cunha", document.select("span.vcard-fullname").text());
        
        seleneasy.waitClickable(By.cssSelector("a[href='/brunocvcunha?tab=stars']"), true);
        seleneasy.waitClickable(By.cssSelector("a[rel='next']"), true);
        
        Boolean onRightPage = seleneasy.waitForCondition(ExpectedConditions.urlContains("page=2"));
        assertTrue(onRightPage);
        assertEquals("https://github.com/brunocvcunha?page=2&tab=stars", seleneasy.getUrl());
        
    }

}