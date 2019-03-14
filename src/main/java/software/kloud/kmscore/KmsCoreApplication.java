package software.kloud.kmscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.kloud.kmscore.util.LocalDiskStorage;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class KmsCoreApplication {

	public static void main(String[] args) {
		LocalDiskStorage.getInstance().init();
		SpringApplication.run(KmsCoreApplication.class, args);
	}

	public static class LoadedClassesHolder {
		private List<Class<?>> classes;

		private LoadedClassesHolder() {

		}

		private static class INSTANCE_HOLDER {
			static LoadedClassesHolder INSTANCE = new LoadedClassesHolder();
		}

		public static LoadedClassesHolder getInstance() {
			return INSTANCE_HOLDER.INSTANCE;
		}

		public void addClasses(List<Class<?>> classes) {
			this.classes = new ArrayList<>(classes);
		}

		public List<Class<?>> getAll() {
			return this.classes;
		}
	}
}

