package com.huikedu.sys.common;

import com.huikedu.sys.domain.AdminUser;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {

    public static void encryptPassword(AdminUser user) {
        user.setPasswordSalt( ByteSource.Util.bytes(user.getAccount()).toHex());
        String newPassword = new SimpleHash(
                "md5",
                user.getPassword(),
                ByteSource.Util.bytes(user.getPasswordSalt()),
                2).toHex();

        user.setPassword(newPassword);
    }
}
