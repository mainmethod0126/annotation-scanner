<!-- TOC -->

- [annotation-scanner](#annotation-scanner)
  - [Usage](#usage)
    - [Example](#example)
      - [Default class loader](#default-class-loader)
      - [Select class loader](#select-class-loader)
      - [result](#result)
  - [Why do we receive a class loader as an argument?](#why-do-we-receive-a-class-loader-as-an-argument)

<!-- /TOC -->
<!-- /TOC -->
<!-- /TOC -->
# annotation-scanner

It's a service that helps find specific annotations used in a project.

---

## Usage

### Example

#### Default class loader

```java

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

```

#### Select class loader

When using the Spring framework, it is often necessary to specify the class loader as Spring-specific class loaders are frequently utilized.

```java
public class AnnotationScannerTest {

    @Test
    @DisplayName("This service locates classes that are using a specific annotation within the given package and its subpackages.")
    public void testScanClass_whenNormalParam_thenSuccess() throws ClassNotFoundException {

        // given
        String rootPackage = "io.github.mainmethod0126.annotation.scanner";

        // when
        AnnotationScanner annotationScanner = new AnnotationScanner(rootPackage);
        List<Class<?>> classes = annotationScanner.scanClass(TestAnnotation.class, this.getClass().getClassLoader());

        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
        }

        // then
        assertThat(classes).isNotNull().isNotEmpty();
    }

}

```

#### result

```bash
# Standard output
io.github.mainmethod0126.annotation.scanner.TestOrder
io.github.mainmethod0126.annotation.scanner.TestProduct
io.github.mainmethod0126.annotation.scanner.TestUser
```

---

## Why do we receive a class loader as an argument?

When considering the scenario where the 'User' class exists, if two different class loaders, A and B, each load the 'User' class separately, comparing the types of the 'User' class loaded by different class loaders would result in them being recognized as distinct classes. In such cases, when a regular user anticipates the recognition of the same class, unexpected outcomes can arise.

