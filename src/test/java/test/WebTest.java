package test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
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
        $$(".catalog-grid__grid").shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    @CsvSource(value = {
            "Яблоки, Фрукты",
            "Чай, Чай"
    })
    @ParameterizedTest(name = "Категория \"{1}\" присутствует для продукта \"{0}\"")
    @DisplayName("Отображение категории")
    void checkCategoryAfterSearchTest(String testData, String expectedResult) {
        open("https://lenta.com/");
        $(".catalog-search__field").setValue(testData);
        $(".catalog-search__nodes").$(byText(testData)).click();
        $(".catalog-tree__list").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> dataProviderForCheckCatalogAfterSearchTest() {
        return Stream.of(
                Arguments.of("Яблоки",
                        List.of("Готовая продукция " + "Грибы " + "Овощи " + "Фрукты " +
                                "Слива, алыча " + "Арбуз " + "Дыня " + "Бананы " + "Виноград " +
                                "Груши " + "Персики, нектарины " + "Фруктовые снеки и нарезки " +
                                "Цитрусовые " + "Экзотические фрукты " + "Яблоки " + "Ягоды")),
                Arguments.of("Чай",
                        List.of("Какао и горячий шоколад " + "Кофе " + "Чай " + "Черный чай " + "Зеленый чай " +
                                "Травяной чай " + "Другие сорта чая " + "Подарочные чайные наборы"))
        );
    }

    @MethodSource("dataProviderForCheckCatalogAfterSearchTest")
    @ParameterizedTest(name = "Для товара \"{0}\", отображаются каталоги \"{1}\"")
    @DisplayName("Отображение каталогов")
    void checkCatalogAfterSearchTest(String product, List<String> expectedBrands) {

        open("https://lenta.com/");
        $(".catalog-search__field").setValue(product);
        $(".catalog-search__nodes").$(byText(product)).click();
        $$(".catalog-tree__list")
                .shouldHave(CollectionCondition.exactTexts(expectedBrands));

    }

}