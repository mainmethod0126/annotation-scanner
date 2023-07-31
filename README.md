<!-- TOC -->

- [annotation-scanner](#annotation-scanner)
  - [Usage](#usage)
    - [Example](#example)
      - [result](#result)

<!-- /TOC -->
# annotation-scanner

It's a service that helps find specific annotations used in a project.

## Usage

### Example

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

#### result

```bash
# Standard output
io.github.mainmethod0126.annotation.scanner.TestOrder
io.github.mainmethod0126.annotation.scanner.TestProduct
io.github.mainmethod0126.annotation.scanner.TestUser
```
