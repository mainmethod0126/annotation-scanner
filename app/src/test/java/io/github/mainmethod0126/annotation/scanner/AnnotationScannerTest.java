package io.github.mainmethod0126.annotation.scanner;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AnnotationScannerTest {

    @Test
    @DisplayName("This service locates classes that are using a specific annotation within the given package and its subpackages.")
    public void testScanClass_whenNormalParam_thenSuccess() throws ClassNotFoundException {

        // given
        String rootPackage = "io.github.mainmethod0126.annotation.scanner";

        // when
        AnnotationScanner annotationScanner = new AnnotationScanner(rootPackage);
        List<Class<?>> classes = annotationScanner.scanClass(TestAnnotation.class);

        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
        }

        // then
        assertThat(classes).isNotNull().isNotEmpty();
    }

}
