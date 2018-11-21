package com.duobi.manager;

import com.duobi.manager.sys.utils.Digests;
import com.duobi.manager.sys.utils.Encodes;
import com.duobi.manager.sys.utils.Global;
import jdk.nashorn.api.tree.GotoTree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
//        String[] arrayA = new String[] { "C", "B", "A", "D", "E", "F", "A" };
//        String[] arrayB = new String[] { "B", "D", "F", "G", "H", "K", "A" };
//        List<String> listA = Arrays.asList(arrayA);
//        List<String> listB = Arrays.asList(arrayB);
//        //2个数组取并集
//        System.out.println(ArrayUtils.toString(CollectionUtils.union(listA, listB)));
//
//
//        Set elts = new HashSet(listA);
////        elts.addAll(listB);
//
//        Iterator it = elts.iterator();
//        while(it.hasNext()) {
//            Object obj = it.next();
//            System.out.println(obj.toString());
//        }

        String t = "ttttt";
        if (t.equals(null)) System.out.println("equal");
        else System.out.println("不相等");

	}

    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = Encodes.decodeHex(password.substring(0,16));
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
    }

}
