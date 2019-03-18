package software.kloud.kmscore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.kloud.KMSPluginSDK.IKMSPlugin;
import software.kloud.KMSPluginSDK.KMSPlugin;
import software.kloud.kmscore.util.LocalDiskStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class KmsCoreApplication {

    public static void main(String[] args) throws IOException, PluginRegisterException {
        PluginLoader pluginLoader = new PluginLoader(new File("plugins"));
        var allClasses = pluginLoader.load();

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
        SpringApplication.run(KmsCoreApplication.class, args);
    }

    public static class LoadedClassesHolder {
        private List<Class<?>> classes;

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

