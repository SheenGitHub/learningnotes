package com.sheen.pc;

import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by zxj7044 on 2018-11-16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PcApplication.class)
public class PcApplicationTest {
    private TestSuite testSuite;

    @Before
    public void setUp() throws Exception {
        testSuite = new TestSuite();
        testSuite.addTestSuite(IUserServiceTest.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        TestRunner.run(testSuite);
    }

}