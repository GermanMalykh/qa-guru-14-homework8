package com.lenta.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@DisplayName("Web тесты")
public class WebTests extends TestBase {

    private final static String
            PAGE_URL = "https://lenta.com";

    private SelenideElement
            searchInput = $(".catalog-search__field"),
            selectSection = $(".catalog-search__nodes"),
            productTree = $(".catalog-tree__list");

    private ElementsCollection
            productListCollection = $$(".catalog-grid__grid"),
            productTreeCollection = $$(".catalog-tree__list");

    @ValueSource(strings = {"Яблоки", "Чай"})
    @ParameterizedTest(name = "Товар \"{0}\", зарегистрирован в магазине")
    @DisplayName("Поиск")
    void searchProductsTest(String testData) {
        step("Открываем главную страницу " + PAGE_URL, () -> {
            open(PAGE_URL);
            clearBrowserCookies();
            clearBrowserLocalStorage();
        });
        step("Вводим в поле поиска " + testData, () -> {
            searchInput.setValue(testData);
        });
        step("Выбираем " + testData + " в списке разделов поиска", () -> {
            selectSection.$(byText(testData)).click();
        });
        step("Проверяем наличие товара " + testData + " в магазине ", () -> {
            productListCollection.shouldBe(CollectionCondition.sizeGreaterThan(0));
        });

    }

    @CsvSource(value = {
            "Яблоки, Фрукты",
            "Чай, Чай"
    })
    @ParameterizedTest(name = "Категория \"{1}\" присутствует для продукта \"{0}\"")
    @DisplayName("Отображение категории")
    void checkCategoryAfterSearchTest(String testData, String expectedResult) {
        step("Открываем главную страницу " + PAGE_URL, () -> {
            open(PAGE_URL);
            clearBrowserCookies();
            clearBrowserLocalStorage();
        });
        step("Вводим в поле поиска " + testData, () -> {
            searchInput.setValue(testData);
        });
        step("Выбираем " + testData + " в списке разделов поиска", () -> {
            selectSection.$(byText(testData)).click();
        });
        step("Находим " + testData + " среди категорий", () -> {
            productTree.shouldHave(text(expectedResult));
        });

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
    void checkCatalogAfterSearchTest(String product, List<String> expectedCatalogs) {
        step("Открываем главную страницу " + PAGE_URL, () -> {
            open(PAGE_URL);
            clearBrowserCookies();
            clearBrowserLocalStorage();
        });
        step("Вводим в поле поиска " + product, () -> {
            searchInput.setValue(product);
        });
        step("Выбираем " + product + " в списке разделов поиска", () -> {
            selectSection.$(byText(product)).click();
        });
        step("Находим " + product + " среди категорий", () -> {
            productTreeCollection.shouldHave(CollectionCondition.exactTexts(expectedCatalogs));
        });
    }

}