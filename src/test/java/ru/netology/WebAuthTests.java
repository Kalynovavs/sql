package ru.netology;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.open;

public class WebAuthTests {

    @AfterAll
    static void resetData() {
        DataHelper.clearDb();
    }


    @Test
    @SneakyThrows
    void shouldSuccessAuth() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validCode(verificationCode);
    }

    @Test
    @SneakyThrows
    void shouldFaledAuth() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthWrongInfo();
        loginPage.wrongPassword(authInfo);
        loginPage.clearAuth();
        loginPage.wrongPassword(authInfo);
        loginPage.clearAuth();
        loginPage.wrongPassword(authInfo);
        loginPage.clearAuth();
        loginPage.authBeforeBlocked(authInfo);
    }
}