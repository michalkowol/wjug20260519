package eu.codeloop.ai.wjug.architecture

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.DisplayName

@AnalyzeClasses(
    packages = ["eu.codeloop.ai.wjug"],
    importOptions = [DoNotIncludeTests::class]
)
@DisplayName("In application")
class DomainBoundariesArchUnitTest {

    @ArchTest
    val `classes should be free of cyclic dependencies`: ArchRule = slices()
        .matching("eu.codeloop.ai.wjug.(*)..")
        .should()
        .beFreeOfCycles()

    @ArchTest
    val `domain package should have no dependencies on application or boundary packages`: ArchRule = noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..application..", "..boundary..")

    @ArchTest
    val `the application package should not have dependencies on the boundary package`: ArchRule = noClasses()
        .that()
        .resideInAPackage("..application..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..boundary..")
}
