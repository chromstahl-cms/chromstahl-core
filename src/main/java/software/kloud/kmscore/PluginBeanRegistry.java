package software.kloud.kmscore;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.*;
import software.kloud.KmsCoreApplication;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class PluginBeanRegistry {

    private static List<Class<? extends Annotation>> springAnnotations;

    static {
        springAnnotations = new ArrayList<>();
        springAnnotations.add(Component.class);
        springAnnotations.add(Controller.class);
        springAnnotations.add(Indexed.class);
        springAnnotations.add(Repository.class);
        springAnnotations.add(Service.class);
    }

    private static boolean checkIfClassIsSpringStereotype(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredAnnotations())
                .anyMatch(a -> springAnnotations.contains(a.getClass()));
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor getRegistry() {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                for (Class<?> clazz : KmsCoreApplication.LoadedClassesHolder.getInstance().getAll()) {
                    if (checkIfClassIsSpringStereotype(clazz)) {
                        continue;
                    }
                    AnnotatedBeanDefinition def = new AnnotatedGenericBeanDefinition(clazz);
                    registry.registerBeanDefinition(clazz.getName(), def);
                }
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

            }
        };
    }
}
