package ru.netology;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.open;

public class DbInteractionDbUtils {

    @AfterAll
    @SneakyThrows
    static void ClearDb() {
        var runner = new QueryRunner();
        var foreignKey = "SET FOREIGN_KEY_CHECKS=?";
        var clearUsers = "DELETE FROM users";
        var clearCards = "DELETE FROM cards";
        var clearCodes = "DELETE FROM auth_codes";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );

        ) {
            runner.update(conn, foreignKey, 0);
            runner.update(conn, clearUsers);
            runner.update(conn, clearCards);
            runner.update(conn, clearCodes);
            runner.update(conn, foreignKey, 1);
        } catch (Throwable err){
            System.out.println("ОШИБКА");
            System.out.println(err);
        }
    }


    @Test
    @SneakyThrows
    void shouldSuccessAuth() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        System.out.println(verificationCode);
        var dashboardPage = verificationPage.validCode(verificationCode);
    }

    @Test
    @SneakyThrows
    void shouldFaledAuth() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        loginPage.wrongPassword(authInfo);
        loginPage.clearAuth();
        loginPage.wrongPassword(authInfo);
        loginPage.clearAuth();
        loginPage.wrongPassword(authInfo);
        loginPage.clearAuth();
        loginPage.authBeforeBlocked(authInfo);
    }
}