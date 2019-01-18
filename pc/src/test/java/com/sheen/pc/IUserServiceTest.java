package com.sheen.pc;

import com.github.pagehelper.PageInfo;
import com.sheen.pc.mapper.IUserMapper;
import com.sheen.pc.model.User;
import com.sheen.pc.service.IUserService;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zxj7044 on 2018-11-16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PcApplication.class)
public class IUserServiceTest extends TestCase {

    @Autowired
    private IUserService userService;

    private User user;
    @Before
    public void setUp() throws Exception {

        assertNotNull(userService);
        user = new User();
        user.setName("陈意");
    }

    @Test
    public void testListUsers() throws Exception {
        List<User> userList = userService.listUsers(user);
        assertEquals("Result is get","陈意",userList.get(0).getName());
    }

    @Test
    public void testLindByPage() throws Exception {
        PageInfo<User> userPageInfo = userService.findByPage(1, 10);
        assertEquals("Result size is 3", 3, userPageInfo.getSize());

    }

}