package io.github.mainmethod0126.annotation.scanner;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationScanner {

    // Specifies the top-level package or namespace name to search for classes.
    private String rootPackageName;
    private List<Class<?>> classes = new ArrayList<>();

    public AnnotationScanner(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * Receives a parameter of type 'Class<? extends Annotation>' and searches for
     * classes containing the specified annotation within the given
     * 'rootPackageName' and its subpackages.
     * 
     * @param annotationClass
     * @return The list of classes using annotations
     * @throws ClassNotFoundException
     */
    public List<Class<?>> scanClass(Class<? extends Annotation> annotationClass) throws ClassNotFoundException {

        this.classes.clear();

        // Obtains the classpath
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        String packagePath = packageToPath(this.rootPackageName);

        for (String classpathEntry : classpathEntries) {
            scanClass(new File(classpathEntry, packagePath), pathToPackage(packagePath), annotationClass);
        }

        return classes;
    }

    private void scanClass(File rootDir, String namespace,
            Class<? extends Annotation> annotationClass) throws ClassNotFoundException {

        File[] files = rootDir.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {
                scanClass(file, namespace.replace('/', '.') + "." + file.getName(), annotationClass);
            } else {

                if (!file.getName().endsWith(".class")) {
                    continue;
                }

                // After removing the .class extension from the class file name, we obtain the
                // class name
                String className = namespace + "."
                        + file.getName().replaceAll(".class$", "");

                // We dynamically load the class.
                Class<?> loadedClass = Class.forName(className);

                if (loadedClass.isAnnotationPresent(annotationClass)) {
                    // We add the loaded class to the list to be returned.
                    classes.add(loadedClass);
                }
            }
        }
    }

    /**
     * Converts a given path to a package representation by replacing slashes ('/')
     * with dots ('.').
     *
     * @param value The path to be converted.
     * @return The package representation of the input path.
     */
    private String pathToPackage(String value) {
        return value.replace('/', '.');
    }

    /**
     * Converts a given package representation to a path by replacing dots ('.')
     * with slashes ('/').
     *
     * @param value The package representation to be converted.
     * @return The path representation of the input package.
     */
    private String packageToPath(String value) {
        return value.replace('.', '/');
    }

}
