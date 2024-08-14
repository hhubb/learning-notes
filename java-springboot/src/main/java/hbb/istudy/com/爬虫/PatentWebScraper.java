package hbb.istudy.com.爬虫;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author binbin
 * 财务报告爬虫
 * 巨潮网
 * @Date 2024 08 12 15 17
 **/
@Slf4j
public class PatentWebScraper {
    static String SEARCH_URL = "https://www.chonghus.com/search_qc?Search_type=sqr&Context=%E7%BE%8E%E7%9A%84&Page=1&title=%E7%94%B3%E8%AF%B7%E4%BA%BA";
    static String DRIVER_PATH = "C:/Users/admin/Desktop/海尔工作文档/爬虫/chromedriver-win64/chromedriver.exe";
    static String companyName = "美的";
    static String savePath = "C:/Users/admin/Desktop/海尔工作文档/爬虫/专利/";
    static String parentPath = savePath + companyName;
    static String PDF_PATH = "https://www.chonghus.com/hxapi2/pat/pdf?vType=V&id=%s#toolbar=0";
    static Map<String, String> downLoadFilePath = new HashMap<>();
    static Map<String, String> fileHrefPath = new HashMap<>();
    static volatile int totalCount = 0;
    static volatile int downLoadCount = 0;
    static volatile int errorCount = 0;

    public static void main(String[] args) throws IOException {
        StrBuilder errorInfo = new StrBuilder();
        try {
            getReportInfo();
            totalCount = downLoadFilePath.size();

            for (Map.Entry<String, String> entry : downLoadFilePath.entrySet()) {
                try {
                    downLoadPDF(entry.getKey(), entry.getValue());
                } catch (Exception exception) {
                    String errorMessage = entry.getKey() + "报告下载异常，下载地址：" + downLoadFilePath.get(entry.getKey()) + "；原连接：" + fileHrefPath.get(entry.getKey());
                    errorInfo.append(errorMessage);
                    log.error(errorMessage);
                    exception.getStackTrace();
                    errorCount++;
                }
            }
            Entity entity = Entity.create("report_info")
                    .set("company_name", companyName)
                    .set("search_url", SEARCH_URL)
                    .set("report_type", ReportTypeEnum.Financial.getCode())
                    .set("success_count", downLoadCount)
                    .set("failure_count", errorCount)
                    .set("failure_info", errorInfo.toString())
                    .set("downLoad_path", parentPath);
            Db.use().insertForGeneratedKey(entity);
            log.info(companyName + "财务报告，总计：" + totalCount + ";下载成功：" + downLoadCount + ";下载失败：" + errorCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getReportInfo() throws IOException {

        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        // 设置最长等待时间
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(SEARCH_URL);
        while (true) {
            WebElement element = driver.findElement(By.className("el-table__body"));
            WebElement tbody = element.findElement(By.tagName("tbody"));
            List<WebElement> trList = tbody.findElements(By.tagName("tr"));
            if (trList.isEmpty()) {
                continue;
            }
            for (WebElement tr : trList) {
                List<WebElement> tdList = tr.findElements(By.tagName("td")); // 获取第一个tr中的所有td
                if (trList.isEmpty()) {
                    continue;
                }
                WebElement td = tdList.get(1);
                WebElement table = td.findElement(By.tagName("table"));
                List<WebElement> tableTrs = table.findElements(By.tagName("tr"));
                if (tableTrs.size()<5){
                    continue;
                }
                WebElement tableTr =  tableTrs.get(4);
                List<WebElement> tableTrTds = tableTr.findElements(By.tagName("td")); // 获取第一个tr中的所有td
                if (tableTrTds.isEmpty()) {
                    continue;
                }

                // 获取第一个tr中的所有td
                WebElement spanElement = td.findElement(By.tagName("span"));
                String fileName = spanElement.getText();
                WebElement aElement = td.findElement(By.tagName("a"));
                String href = aElement.getAttribute("href");
                String pdfId = aElement.getAttribute("data-id");
                String date = extractDate(href);
                String pdfPath = String.format(PDF_PATH, date, pdfId);
                log.info(fileName + ":" + String.format(PDF_PATH, date, pdfId));
                downLoadFilePath.put(fileName, pdfPath);
                fileHrefPath.put(fileName, href);
            }


            // 如果有下一页就点击下一页
            if (!check(driver, By.className("btn-next"))) {
                log.info("没有下一页啦");
                break;
            }
            WebElement element1 = driver.findElement(By.className("btn-next"));
            element1.click();
            log.info("点击进入下一页");
            // 等待标签出现变化
            sleep(1000);
        }
    }

    public static String extractDate(String url) {
        // 正则表达式匹配 announcementTime 参数
        Pattern pattern = Pattern.compile("announcementTime=(\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(url);

        // 查找匹配项并返回日期
        if (matcher.find()) {
            return matcher.group(1); // 获取第一个捕获组，即日期部分
        }
        return null; // 如果没有匹配项，则返回 null
    }

    private static void downLoadPDF(String fileName, String pdfPath) throws IOException {
        URL url = new URL(pdfPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        File dir = new File(parentPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String saveFileName = parentPath + "/" + fileName + ".pdf";
        File file = new File(saveFileName);
        //文件存在就不下载了
        if (file.exists()) {
            downLoadCount++;
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(parentPath + "/" + fileName + ".pdf");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = inputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, len);
        }
        fileOutputStream.close();
        inputStream.close();
        downLoadCount++;
    }

    // 等待一定时间
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 判断某个元素是否存在
    public static boolean check(WebDriver driver, By selector) {
        try {
            WebElement webElement = driver.findElement(selector);
            String disabled = webElement.getAttribute("disabled");
            if (null != disabled && disabled.equals("true")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static double parseDoubleStr(String doublestr) {
        if (doublestr.equals("-")) {
            return 0.0;
        } else {
            return Double.parseDouble(doublestr.replaceAll(",", ""));
        }
    }

    public static long parseLongStr(String longstr) {
        // System.out.println("longstr=" + longstr);
        int flag = 1;
        if (longstr.contains("-1")) {
            flag = -1;
        }
        longstr = longstr.replaceAll("-", "");
        longstr = longstr.replaceAll(",", "");
        // 如果有小数点
        if (longstr.contains(".")) {
            longstr = longstr.replaceAll("\\.", "");
            return Long.parseLong(longstr) * 100 * flag;
        } else { // 没有小数点
            return Long.parseLong(longstr) * 10000 * flag;
        }
    }

    // 关闭当前窗口
    public static void closeWindow(WebDriver driver) {
        // 获取所有句柄的集合
        List<String> winHandles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window((String) winHandles.get(1));
        driver.close();
        driver.switchTo().window((String) winHandles.get(0));
    }

}
