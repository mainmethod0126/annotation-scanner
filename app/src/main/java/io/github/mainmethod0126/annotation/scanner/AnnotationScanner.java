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
     * Note: This function does not take a class loader as a parameter; instead, it
     * uses the class loader of the provided annotationClass by default. If you wish
     * to specify a class loader, please use the scanClass function that accepts
     * classLoader as an argument.
     * 
     * @param annotationClass
     * @return
     * @throws ClassNotFoundException
     */
    public List<Class<?>> scanClass(Class<? extends Annotation> annotationClass)
            throws ClassNotFoundException {
        return scanClass(annotationClass, annotationClass.getClassLoader());
    }

    /**
     * Receives a parameter of type 'Class<? extends Annotation>' and searches for
     * classes containing the specified annotation within the given
     * 'rootPackageName' and its subpackages.
     * 
     * @param annotationClass
     * @param classLoader     Since an unintended selection of a different class
     *                        loader can lead to recognizing the same class file as
     *                        a different class, we specify the class loader.
     * @return The list of classes using annotations
     * @throws ClassNotFoundException
     */
    public List<Class<?>> scanClass(Class<? extends Annotation> annotationClass, ClassLoader classLoader)
            throws ClassNotFoundException {

        this.classes.clear();

        // Obtains the classpath
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        String packagePath = packageToPath(this.rootPackageName);

        for (String classpathEntry : classpathEntries) {
            scanClass(new File(classpathEntry, packagePath), pathToPackage(packagePath), annotationClass, classLoader);
        }

        return classes;
    }

    private void scanClass(File rootDir, String namespace,
            Class<? extends Annotation> annotationClass, ClassLoader classLoader) throws ClassNotFoundException {
        File[] files = rootDir.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {
                scanClass(file, namespace.replace('/', '.') + "." + file.getName(), annotationClass, classLoader);
            } else {

                if (!file.getName().endsWith(".class")) {
                    continue;
                }

                // After removing the .class extension from the class file name, we obtain the
                // class name
                String className = namespace + "."
                        + file.getName().replaceAll(".class$", "");

                /*
                 * If classLoader is not specified, the class loader of the method that invoked
                 * forName() is used, which can inadvertently result in the use of different
                 * class loaders. As a result, the same class file can be mistakenly identified
                 * as different classes. Therefore, to address this issue, we have made it
                 * explicit to specify the class loader, ensuring a clear choice of which class
                 * loader is used to load the class.
                 */
                Class<?> loadedClass = Class.forName(className, true, classLoader);

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
