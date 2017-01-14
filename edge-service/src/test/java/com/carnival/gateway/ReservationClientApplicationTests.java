package com.carnival.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReservationClientApplication.class)
@WebAppConfiguration
public class ReservationClientApplicationTests {

	@Test
	public void contextLoads() {
	}

}
