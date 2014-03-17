package org.cauli.test;

import org.cauli.ui.annotation.Require;
import org.cauli.ui.runner.CauliUIRunner;
import org.cauli.ui.selenium.browser.Engine;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.cauli.ui.selenium.browser.Auto.*;

/**
 * Created by celeskyking on 14-3-1
 */
//@RunWith(CauliUIRunner.class)
public class BaseTest {


    //@Test
    //@Param("test.txt")
    public void testOne() throws InterruptedException {
        require(Engine.FIREFOX);
        go("http://www.baidu.com");
        $("#kw1").input("北京");
        $("#su1").click();
        Thread.sleep(1000);
        //currentPage().assertTitle("百度一下，你就知道");
        quit();
    }


    //@Test
    @Require(Engine.FIREFOX)
    public void testTwo() throws InterruptedException {
        go("http://l-payserver12.pay.beta.cn6.qunar.com/pay/API/TestPage.php");
        select("#selectVersion").selectByValue("20130808");
        select("#selectCommand").selectByValue("CashierPay");
        String tableLocation = "xpath->.//*[@id='testpage']/table";
        table(tableLocation).cell(3,2).find("tagName->input").input("FLIGHT");
        table(tableLocation).cell(4,2).find("tagName->input").input("testDaiGou");
        table(tableLocation).cell(5,2).find("tagName->input").input("PAY");
        table(tableLocation).cell(8,2).find("tagName->input").input("0.01");
        table(tableLocation).cell(11,2).find("tagName->input").input("product_detail");
        table(tableLocation).cell(14,2).find("tagName->input").input("STANDARD");
        table(tableLocation).cell(15,2).find("tagName->input").input("CNY");
        table(tableLocation).cell(16, 2).find("tagName->input").clear();
        table(tableLocation).cell(20,2).find("tagName->input").clear();
        table(tableLocation).cell(21,2).find("tagName->input").input("buyForexAmount;0.01_-$curId;USD");
        table(tableLocation).cell(23,1).find("tagName->input").click();
        selectLastOpenedPage().find("xpath->.//input[@type='submit']").click();
        $("#show_creditcard_online").click();
        Thread.sleep(1000);
        $("#all_online_card_list_ctn").find("className->btn_submit").click();
        quit();
    }



}
