package software.kloud.kmscore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.kloud.KmsCoreApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KmsCoreApplicationTests {
	static {
		instance = KmsCoreApplication.LoadedClassesHolder.getInstance();

	}

	private static KmsCoreApplication.LoadedClassesHolder instance;


	@Test
	public void contextLoads() {
	}

}
