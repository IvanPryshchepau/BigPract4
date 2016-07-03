import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Function {

    String base_url = "http://localhost/";
    File path_to_profile = new File("e:/WebDriverProfile/");
    FirefoxProfile profile = new FirefoxProfile(path_to_profile);
    WebDriver driver = null;

    String error;

    boolean autorized = false;

    public Function() {
        driver = new FirefoxDriver(profile);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(base_url);
    }

    public boolean pageContainsText(String page, String text, boolean typeTest){
        driver.get(page);
        boolean result = driver.findElement(By.xpath("//html")).getText().contains(text);
        error = "(text not found)";
        if (typeTest){
            return result;
        } else {
            error = "";
            return !result;
        }

    }

    public boolean pageContainsLinkText(String page, String text, boolean typeTest){
        driver.get(page);
        boolean result = false;
        error = "(linktext not found)";
        try {
            result = driver.findElement(By.linkText(text)).isDisplayed();
        } catch (NoSuchElementException e){
            result = false;
            error = "(NoSuchElement)";
        }
        if (typeTest) {
            return result;
        } else {
            return !result;
        }
    }

    public void registration(String login, String email, String password){
        //disable captcha!!!
        driver.get(base_url);
        try {
            if (driver.findElement(By.xpath("//li[@id=\"username_logged_in\"]")).isDisplayed()) {
                driver.findElement(By.xpath("//li[@id=\"username_logged_in\"]")).click();
                driver.findElement(By.xpath("//li[@class=\"small-icon icon-logout\"]")).click();
                autorized = false;
            }
        } catch (NoSuchElementException e) {}
        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("agreed")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(login);
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("new_password")).clear();
        driver.findElement(By.id("new_password")).sendKeys(password);
        driver.findElement(By.id("password_confirm")).clear();
        driver.findElement(By.id("password_confirm")).sendKeys(password);
        driver.findElement(By.xpath("//input[@type=\"submit\"][@class=\"button1 default-submit-action\"]")).click();
    }

    public boolean autorize(String login, String password, boolean typeTest){
        try {
            if (driver.findElement(By.xpath("//li[@id=\"username_logged_in\"]")).isDisplayed()) {
                driver.findElement(By.xpath("//li[@id=\"username_logged_in\"]")).click();
                driver.findElement(By.xpath("//li[@class=\"small-icon icon-logout\"]")).click();
                autorized = false;
            }
        } catch (NoSuchElementException e) {}

        driver.findElement(By.xpath("//input[@id=\"username\"]")).clear();
        driver.findElement(By.xpath("//input[@id=\"username\"]")).sendKeys(login);
        driver.findElement(By.xpath("//input[@id=\"password\"]")).clear();
        driver.findElement(By.xpath("//input[@id=\"password\"]")).sendKeys(password);
        driver.findElement(By.xpath("//input[@type=\"submit\"]")).click();

        boolean result = false;

        try {
            result = driver.findElement(By.xpath("//li[@id=\"username_logged_in\"]/div/a/span")).isEnabled();
        } catch (NoSuchElementException e){}

        try {
            if (!result && driver.findElement(By.className("error")).isEnabled()) {
                result = false;
                error = "(" + driver.findElement(By.className("error")).getText() + ")";
            }
        } catch (NoSuchElementException e){}

        autorized = result;
        if (typeTest){
            return result;
        } else {
            return !result;
        }
    }

    public boolean search(String text, int count, boolean typeTest){

        driver.get(base_url);
        driver.findElement(By.id("keywords")).clear();
        driver.findElement(By.id("keywords")).sendKeys(text);
        driver.findElement(By.xpath("//button[@class=\"button icon-button search-icon\"][@title=\"Search\"]")).click();
        String result = driver.findElement(By.xpath("//h2[@class=\"searchresults-title\"]")).getText();
        error = "(" +driver.findElement(By.xpath("//h2[@class=\"searchresults-title\"]")).getText()+ ")";

        if (typeTest){
            return result.contains(" " + String.valueOf(count) + " ");
        } else {
            return !result.contains(" " + String.valueOf(count) + " ");
        }


    }

    public boolean addTopic(String forum, String topic, boolean typeTest){
        if (autorized) {
            driver.get(base_url);
            Collection<WebElement> forums = driver.findElements(By.xpath("//div[@id=\"page-body\"]"));
            Iterator<WebElement> i = forums.iterator();
            WebElement forumElement = null;
            while (i.hasNext()) {
                forumElement = i.next();
                if (forumElement.findElement(By.linkText(forum)).isDisplayed()) {
                    forumElement.findElement(By.linkText(forum)).click();
                    break;
                } else {
                    error = "(forum not found)";
                    return false;
                }
            }
            driver.findElement(By.linkText("New Topic")).click();
            driver.findElement(By.id("subject")).clear();
            driver.findElement(By.id("subject")).sendKeys(topic);
            driver.findElement(By.xpath("//textarea[@id=\"message\"]")).clear();
            driver.findElement(By.xpath("//textarea[@id=\"message\"]")).sendKeys(topic);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//input[@type=\"submit\"][@class=\"button1 default-submit-action\"]")).click();
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className("postbody")));
            boolean result = false;
            try {
                if (driver.findElement(By.xpath("//div[@id=\"page-body\"]/h2/a")).getText().equals(topic)){
                    return true;
                }
            } catch (NoSuchElementException e){
                error = "(topic add incorrectly)";
            }
            return false;
        } else {
            error = "(not autorized)";
            return false;
        }
    }

    public boolean addComment(String forum, String topic, String comment, boolean typeTest) {
        if (autorized) {
            driver.get(base_url);
            Collection<WebElement> forums = driver.findElements(By.xpath("//div[@id=\"page-body\"]"));
            Iterator<WebElement> i = forums.iterator();
            WebElement forumElement = null;
            while (i.hasNext()) {
                forumElement = i.next();
                if (forumElement.findElement(By.linkText(forum)).isDisplayed()) {
                    forumElement.findElement(By.linkText(forum)).click();
                    break;
                } else {
                    error = "(forum not found)";
                    return false;
                }
            }
            Collection<WebElement> topics = driver.findElements(By.xpath("//div[@id=\"page-body\"]"));
            Iterator<WebElement> i1 = topics.iterator();
            WebElement topicElement = null;
            while (i1.hasNext()) {
                topicElement = i1.next();
                if (topicElement.findElement(By.linkText(topic)).isDisplayed()) {
                    topicElement.findElement(By.linkText(topic)).click();
                    break;
                } else {
                    error = "(topic not found)";
                    return false;
                }
            }
            driver.findElement(By.linkText("Post Reply")).click();
            driver.findElement(By.xpath("//textarea[@id=\"message\"]")).clear();
            driver.findElement(By.xpath("//textarea[@id=\"message\"]")).sendKeys(comment);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//input[@type=\"submit\"][@class=\"button1 default-submit-action\"]")).click();

            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("page-body")));

            Collection<WebElement> comments = driver.findElements(By.xpath("//div[@class=\"content\"]"));
            Iterator<WebElement> i2 = comments.iterator();
            WebElement commentElement = null;
            while (i2.hasNext()) {
                commentElement = i2.next();
                if (commentElement.getText().equalsIgnoreCase(comment)) {
                    return true;
                }
            }
            error = "(comment add incorrectly)";
            return false;
        } else {
            error = "(not autorized)";
            return false;
        }
    }

    public boolean editComment(String comment, boolean typeTest){

        if (autorized){

            driver.findElement(By.xpath("//li[@id=\"username_logged_in\"]")).click();
            driver.findElement(By.linkText("Profile")).click();
            driver.findElement(By.linkText("Search user’s posts")).click();
            if (driver.findElement(By.xpath("//h2[@class=\"searchresults-title\"]")).getText().contains(" 0 ")){
                error = "(user have not posts)";
                return false;
            }
            driver.findElement(By.linkText("Jump to post")).click();
            driver.findElement(By.xpath("//a[@class=\"button icon-button edit-icon\"][@title=\"Edit post\"]")).click();
            driver.findElement(By.xpath("//textarea[@id=\"message\"]")).clear();
            driver.findElement(By.xpath("//textarea[@id=\"message\"]")).sendKeys(comment);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.findElement(By.xpath("//input[@type=\"submit\"][@class=\"button1 default-submit-action\"]")).click();
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("page-body")));

            Collection<WebElement> comments = driver.findElements(By.xpath("//div[@class=\"content\"]"));
            Iterator<WebElement> i2 = comments.iterator();
            WebElement commentElement = null;
            while (i2.hasNext()) {
                commentElement = i2.next();
                if (commentElement.getText().equalsIgnoreCase(comment)) {
                    return true;
                }
            }
            error = "(comment edit incorrectly)";
            return false;

        } else {
            error = "(not autorized)";
            return false;
        }

    }

    public boolean isOnline(String user, boolean typeTest){
        driver.get(base_url);
        WebElement whoIsOnline = driver.findElement(By.xpath("//div[@class=\"stat-block online-list\"]/p"));
        if (typeTest){
            return whoIsOnline.findElement(By.linkText(user)).isEnabled();
        } else {
            return !whoIsOnline.findElement(By.linkText(user)).isEnabled();
        }
    }

    public int getTotalPosts(){
        driver.get(base_url);
        return Integer.parseInt(driver.findElement(By.xpath("//div[@class=\"stat-block statistics\"]/p/strong[1]")).getText());
    }

    public int getTotalTopics(){
        driver.get(base_url);
        return Integer.parseInt(driver.findElement(By.xpath("//div[@class=\"stat-block statistics\"]/p/strong[2]")).getText());
    }

    public int getTotalMembers(){
        driver.get(base_url);
        return Integer.parseInt(driver.findElement(By.xpath("//div[@class=\"stat-block statistics\"]/p/strong[3]")).getText());
    }

}
