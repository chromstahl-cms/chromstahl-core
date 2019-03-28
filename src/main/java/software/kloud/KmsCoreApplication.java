package software.kloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import software.kloud.KMSPluginSDK.IKMSPlugin;
import software.kloud.KMSPluginSDK.KMSPlugin;
import software.kloud.classery.jar.JarUnpackingException;
import software.kloud.classery.loader.ClasseryLoader;
import software.kloud.kmscore.plugin.PluginManager;
import software.kloud.kmscore.plugin.PluginRegisterException;
import software.kloud.kmscore.util.LocalDiskStorage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableWebSecurity
@SpringBootApplication
@ComponentScan("software.kloud")
public class KmsCoreApplication {

    public static void main(String[] args) throws IOException, PluginRegisterException, JarUnpackingException, NoSuchAlgorithmException {
        beforeSpringInit();
        SpringApplication.run(KmsCoreApplication.class, args);
    }

    private static void beforeSpringInit() throws IOException, PluginRegisterException, JarUnpackingException, NoSuchAlgorithmException {
        var loader = new ClasseryLoader(
                LocalDiskStorage.getStaticRoot(), Collections.singletonList(new File("plugins/")));
        var allClasses = loader.load();

        LoadedClassesHolder.getInstance().addClasses(allClasses);
        PluginManager pluginManager = new PluginManager();

        @SuppressWarnings("unchecked") var pluginClasses = allClasses.stream()
                .filter(c -> c.getDeclaredAnnotation(KMSPlugin.class) != null)
                .map(c -> (Class<? extends IKMSPlugin>) c)
                .collect(Collectors.toList());

        for (Class<? extends IKMSPlugin> pluginClass : pluginClasses) {
            pluginManager.registerPlugin(pluginClass);
        }

        for (PluginManager.PluginHolder pluginHolder : pluginManager.getPluginsSet()) {
            pluginHolder.plugin.init();
        }
    }

    public static class LoadedClassesHolder {
        private List<Class<?>> classes = new ArrayList<>();

        private LoadedClassesHolder() {

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

        private static class INSTANCE_HOLDER {
            static LoadedClassesHolder INSTANCE = new LoadedClassesHolder();
        }
    }
}

