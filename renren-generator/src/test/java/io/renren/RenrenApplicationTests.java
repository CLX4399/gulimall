package io.renren;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RenrenApplicationTests {

	@Test
	public void contextLoads() {
		String name = "clx";
		String name1 = "clx1";
		String name2 = "clx112321";
		String school = "小学校";
		System.out.println(name.hashCode());
		System.out.println(name1.hashCode());
		System.out.println(name2.hashCode());
	}

}
