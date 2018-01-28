package io.vickze.lock.service;

import io.vickze.entity.SysMenuDO;
import io.vickze.service.SysMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author vick.zeng
 * @email 2512522383@qq.com
 * @create 2018-01-27 18:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private SysMenuService sysMenuService;

    @Test
    public void test() {
        SysMenuDO sysMenuDO = sysMenuService.get(1L);
        sysMenuService.update(sysMenuDO);
    }
}
