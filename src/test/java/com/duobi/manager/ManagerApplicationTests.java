package com.duobi.manager;

import com.duobi.manager.sys.utils.Digests;
import com.duobi.manager.sys.utils.Encodes;
import com.duobi.manager.sys.utils.Global;
import jdk.nashorn.api.tree.GotoTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.duobi.manager.sys.utils.PasswordUtils.HASH_INTERATIONS;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagerApplicationTests {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
    public static final String DEFAULT_PASSWORD = "123456";

	@Test
	public void contextLoads() {
//	    String encrytPassword = "c90df626ad909b4da746e364d81231c1f9e553d95c6c61e863abfa61";
//        Boolean b = validatePassword("1234567",encrytPassword);
//        System.out.println("比对结果："+b);

        ChildClass o = new ChildClass();
        o.functionB();

        FatherClass b = new FatherClass();
        b.functionA();

        FatherClass c = new ChildClass();
        c.functionA();

	}

    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = Encodes.decodeHex(password.substring(0,16));
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
    }

}
