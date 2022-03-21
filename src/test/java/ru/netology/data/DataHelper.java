package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.mode.AuthCode;
import ru.netology.mode.User;

import java.sql.DriverManager;
import java.util.Locale;

public class DataHelper {

    private DataHelper(){};

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static String getRandomPassword(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.lorem().characters(8,true);
    }


    @Value
    public static class Verification {
        private String code;
    }

    public static Verification getVerificationCodeFor(AuthInfo authInfo) {
        var runner = new QueryRunner();
        var userSQL = "SELECT * FROM users WHERE login=?";
        var authSQL = "SELECT * FROM auth_codes WHERE user_id=?";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );

        ) {
            var userData = runner.query(conn, userSQL, new BeanHandler<>(User.class),"vasya");
            System.out.println(userData.getId());
            var authCode = runner.query(conn, authSQL, new BeanHandler<>(AuthCode.class), userData.getId());
            System.out.println(authCode.getCode());
            return new Verification(authCode.getCode());
        } catch (Throwable err){
            System.out.println("ОШИБКА");
            System.out.println(err);
        }
        return null;
    }

}
