package software.kloud.kmscore.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.kloud.KMSPluginSDK.IKMSPlugin;
import software.kloud.KMSPluginSDK.KMSPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Set;

public class PluginManager {
    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    private final Set<PluginHolder> pluginsSet;

    public PluginManager() {
        this.pluginsSet = new LinkedHashSet<>();
    }

    public void registerPlugin(Class<? extends IKMSPlugin> cls) throws PluginRegisterException {
        KMSPlugin meta = cls.getAnnotation(KMSPlugin.class);
        IKMSPlugin p;
        try {
            p = cls.cast(cls.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new PluginRegisterException("Not able to create Plugin from classLoader", e);
        } catch (NoSuchMethodException e) {
            throw new PluginRegisterException("Couldn't find a suitable constructor. Please add a empty one", e);
        }
        var ph = new PluginHolder<>(meta.name(), meta.author(), meta.version(), meta.priority(), p);
        pluginsSet.add(ph);
        logger.info(String.format(
                "Loaded plugin '%s' from %s in version %s",
                meta.name(),
                meta.author(),
                meta.version()
        ));
    }

    public Set<PluginHolder> getPluginsSet() {
        return pluginsSet;
    }

    public static class PluginHolder<T extends IKMSPlugin> implements Comparable<PluginHolder> {
        final String name;
        final String author;
        final String version;
        final short priority;
        public final T plugin;

        PluginHolder(String name, String author, String version, short priority, T plugin) {
            this.name = name;
            this.author = author;
            this.version = version;
            this.priority = priority;
            this.plugin = plugin;
        }

        @Override
        public int compareTo(PluginHolder o) {
            return Short.compare(this.priority, o.priority);
        }
    }
}
