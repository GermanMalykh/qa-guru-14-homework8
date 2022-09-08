package test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@DisplayName("Web тесты")
public class WebTest {

    @BeforeAll
    static void configure() {
        Configuration.browserSize = "1920x1080";
    }

    @ValueSource(strings = {"Яблоки", "Чай"})
    @ParameterizedTest(name = "Товар \"{0}\", зарегистрирован в магазине")
    @DisplayName("Поиск")
    void searchProductsTest(String testData) {
        open("https://lenta.com/");
        $(".catalog-search__field").setValue(testData);
        $(".catalog-search__nodes").$(byText(testData)).click();
        $$("div .catalog-grid__grid").shouldBe(CollectionCondition.sizeGreaterThan(0));
    }


    //    //    Для работы с несколькими аргументами (Данные из CsvSource с ожидаемым результатом)
//    @CsvSource(value = {
//            "Selenide, это фреймворк для автоматизированного тестирования",
//            "Allure java, -framework успешно применяется в работе автоматизатора"
//    })
//    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса \"{0}\"")
//    @DisplayName("Поиск")
//    void searchWithTwoArgumentsTest(String testData, String expectedResult) {
//        open("https://ya.ru");
//        $("#text").setValue(testData);
//        $("button[type='submit']").click();
//        $$("li.serp-item")
//                .filter(not(text("Реклама")))
//                .first()
//                .shouldHave(text(expectedResult));
//    }
//
//    //    Для работы с несколькими аргументами, сложный (Данные из CsvSource с ожидаемым результатом)
    static Stream<Arguments> dataProviderForCheckBrandsAfterSearchTest() {
        return Stream.of(
                Arguments.of("Яблоки",
                        List.of("Прочие Товары", "ЧТМ fantasy brands")),
                Arguments.of("Чай",
                        List.of("365 ДНЕЙ", "AHMAD TEA", "AKBAR", "AZERCAY", "BASILUR"))
        );
    }

    @MethodSource("dataProviderForCheckBrandsAfterSearchTest")
    @ParameterizedTest(name = "Для товара \"{0}\", отображаются брэнды \"{1}\"")
    void checkBrandsAfterSearchTest(String product, List<String> expectedBrands) {


        open("https://lenta.com/");
        $(".catalog-search__field").setValue(product);
        $(".catalog-search__nodes").$(byText(product)).click();
        $$(".catalog-filters-control .simplebar-content")
                .shouldHave(CollectionCondition.exactTexts(expectedBrands));

    }
//
//    //    Для работы со словарём
//    @EnumSource(Lang.class)
//    @ParameterizedTest
//    void selenideSiteMenuLangEnumTest(Lang lang) {
//        open("https://Selenide.org");
//        $$("#languages a").find(text(lang.name())).click();
//        $("#selenide-logo").shouldBe(visible);
//    }

}
