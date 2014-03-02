package org.cauli.test;

import org.cauli.ui.annotation.Require;
import org.cauli.ui.runner.CauliUIRunner;
import org.cauli.ui.selenium.browser.Engine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.cauli.ui.selenium.browser.Auto.*;


/**
 * Created by celeskyking on 14-3-1
 */
@RunWith(CauliUIRunner.class)
public class BaseTest {

    static {
        System.setProperty("webdriver.firefox.bin","D:\\tools\\useful\\Mozilla Firefox\\firefox.exe");
    }

    @Test
    @Require(Engine.FIREFOX)
    public void testOne() throws InterruptedException {
        go("http://www.baidu.com");
        $("#kw1").input("北京");
        $("#su1").click();
        Thread.sleep(3000);
        quit();

    }



}
