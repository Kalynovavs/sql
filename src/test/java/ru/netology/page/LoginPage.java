package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public void auth(String login, String pass) {
        loginField.setValue(login);
        passwordField.setValue(pass);
        loginButton.click();
    }

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        auth(authInfo.getLogin(), authInfo.getPassword());
        return new VerificationPage();
    }

    public void clearAuth () {
        loginField.doubleClick();
        loginField.sendKeys(Keys.BACK_SPACE);
        passwordField.doubleClick();
        passwordField.sendKeys(Keys.BACK_SPACE);
    }

    public void wrongPassword(DataHelper.AuthInfo authInfo) {
        auth(authInfo.getLogin(), authInfo.getPassword());
        errorNotification.shouldBe(Condition.visible);
    }

    public void authBeforeBlocked(DataHelper.AuthInfo authInfo) {
        auth(authInfo.getLogin(), authInfo.getPassword());
        errorNotification.shouldBe(Condition.visible);
    }

}
