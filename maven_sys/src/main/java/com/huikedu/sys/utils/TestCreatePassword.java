package com.huikedu.sys.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;


public class TestCreatePassword {

    @Test
    public void testGenaratePassword(){

        String password ="123456";
        //加盐值
        //Object  salt = ByteSource.Util.bytes("admin") ;
        Object  salt = ByteSource.Util.bytes("test") ;
        System.out.println(salt);
        //Object  salt2 = ByteSource.Util.bytes("zhangsan") ;
        //散列的次数，注意跟
        int hashIterations = 2;

        Md5Hash md5Hash = new Md5Hash(password, salt,2);
        System.out.println(md5Hash);

        SimpleHash hashValues = new SimpleHash("md5",password,salt,hashIterations);

        System.out.println(hashValues);
    }
}
