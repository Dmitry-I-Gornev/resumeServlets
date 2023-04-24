/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  21 нояб. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.util;

import ru.inock.webServletResime.sql.SqlHelper;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtils {
    public static final SqlHelper sqlHelper = new SqlHelper();

    public static String LogIn(String userName, String userPassw) throws SQLException {
        return sqlHelper.execute(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_password,user_role FROM users u WHERE u.user_login = ?"
            );
            ps.setString(1,userName);
            ResultSet rs = ps.executeQuery();

            String role = "OTHER";
            if (rs.next()){
                if (DigestUtils.md5Hex(userPassw).equals(rs.getString("user_password"))){
                    role = rs.getString("user_role");

                }
            }
            return role;
        });
    }
}
