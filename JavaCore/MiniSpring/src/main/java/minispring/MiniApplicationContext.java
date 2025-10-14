package minispring;

import minispring.annotation.Autowired;
import minispring.annotation.Component;
import minispring.annotation.Scope;
import minispring.lifecycle.InitializingBean;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MiniApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final String basePackage;

    public MiniApplicationContext(String basePackage) {
        this.basePackage = basePackage;
        initializeContext();
    }

    private void initializeContext() {
        try {
            scanComponents();
            injectDependencies();
            initializationBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void scanComponents() throws Exception {
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException();
        }

        File directory = new File(resource.toURI());
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (!file.getName().endsWith(".class")) {
                continue;
            }
            String className = basePackage + "." + file.getName().replace(".class", "");
            Class<?> clazz = Class.forName(className);

            if (clazz.isAnnotationPresent(Component.class)) {
                Object instance = createBean(clazz);
                beans.put(clazz, instance);
            }
        }
    }

    private Object createBean(Class<?> clazz) throws Exception {
        Scope scope = clazz.getAnnotation(Scope.class);
        if (scope != null && scope.value() == "prototype") {
            return clazz.getDeclaredConstructor().newInstance();
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    private void injectDependencies() throws IllegalAccessException {
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                Class<?> dependencyType = field.getType();
                Object dependency = beans.get(dependencyType);

                if (dependency == null) {
                    try {
                        dependency = createBean(dependencyType);
                        beans.put(dependencyType, dependency);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                field.setAccessible(true);
                field.set(bean, dependency);
            }
        }
    }

    private void initializationBean() {
        beans.values().stream()
                .filter(bean -> bean instanceof InitializingBean)
                .map(bean -> (InitializingBean) bean)
                .forEach(InitializingBean::afterPropertiesSet);
    }

    public <T> T getBean(Class<T> type) {
        Object bean = beans.get(type);
        if (bean == null) {
            throw new RuntimeException();
        }
        return (T) bean;
    }
}
