package io.vickze.lock;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Test;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-23 15:34
 */
public class ShiroTest {

    @Test
    public void test() {
        System.out.println(new Sha256Hash("admin", " YzcmCZNvbXocrsz9dm8e").toString());
    }
}
