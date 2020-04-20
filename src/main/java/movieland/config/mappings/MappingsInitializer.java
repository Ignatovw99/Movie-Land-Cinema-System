package movieland.config.mappings;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MappingsInitializer {

    private static final String ROOT_PACKAGE_NAME = "movieland";

    public static void initMappings(ModelMapper mapper) {
        String configureMappingsMethodName = CustomMappable.class.getDeclaredMethods()[0]
                .getName();

        getClassesWithCustomMappings()
                .forEach(modelClass -> invokeMethodFromClass(modelClass, configureMappingsMethodName, mapper));
    }

    private static List<Class<?>> getClassesWithCustomMappings() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AssignableTypeFilter(CustomMappable.class));

        Set<BeanDefinition> candidates = scanner.findCandidateComponents(MappingsInitializer.ROOT_PACKAGE_NAME);

        return candidates
                .stream()
                .map(BeanDefinition::getBeanClassName)
                .map(MappingsInitializer::getClassFromName)
                .filter(Objects::nonNull)
                .filter(CustomMappable.class::isAssignableFrom)
                .collect(Collectors.toList());
    }

    private static Class<?> getClassFromName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void invokeMethodFromClass(Class<?> modelClass, String methodName, Object... params) {
        try {
            Method method = modelClass.getDeclaredMethod(methodName, ModelMapper.class);
            Object modelInstance = modelClass.getDeclaredConstructor().newInstance();
            method.invoke(modelInstance, params);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
