package sample.application.api.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sample.application.api.ClassUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

/// Checks if the classes in the package [sample.application.api.model] use the annotation [org.jetbrains.annotations.Nullable]
/// to indicate when an attribute can be null, instead of other possible annotations like [jakarta.annotation.Nullable]
/// or [org.springframework.lang.Nullable].
///
/// This will ensure consistency in the type of annotation used in the project.
/// Additionally, the typescript-generator will generate such classes in the models.ts file
/// and it identifies which fields can be null through certain specific Nullable annotations.
///
/// However, it does not support all possible annotations that might be used for this purpose.
/// To avoid obscure errors during the build when running the typescript-generator,
/// this test class was created to ensure that only the annotation [org.jetbrains.annotations.Nullable]
/// is used. Otherwise, it displays a clearer error message.
///
/// TODO: Needs to find all classes that implement the BaseModel interface
///
/// @author Manoel Campos
/// @link [typescript-generator](https://github.com/vojtechhabarta/typescript-generator)
public class NullableAnnotationImportTest {
    private static final List<String> nonAllowedNullableAnnotations = List.of("org.springframework.lang.Nullable", "jakarta.annotation.Nullable");
    private static final String allowedAnnotation = "org.jetbrains.annotations.Nullable";

    @Test @Disabled
    public void nullableAnnotationImport() throws IOException {
        final var packageName = getClass().getPackageName();
        final var classes = ClassUtils.getClassesForPackage(packageName);

        for (final var clazz : classes) {
            final var path = Paths.get("src/main/java", clazz.getName().replace('.', '/') + ".java");
            final var content = ClassUtils.readJavaFileImports(path);
            nonAllowedNullableAnnotations.forEach(annotation -> {
                assertFalse(
                    content.contains("import %s;".formatted(annotation)),
                    () -> "Class %s should not import the annotation %s but %s".formatted(clazz.getName(), annotation, allowedAnnotation));
            });
        }
    }

}
