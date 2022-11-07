package kz.kolesa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import kz.kolesa.data.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class JunitTests {
    @BeforeAll
    static void configure() {
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
    }

    @ValueSource(strings = {"Двигатель на subaru", "Mersedes w221"})
    @ParameterizedTest(name = "Checking the search result {0}")
    void testSearchTest(String testData) {
        open("https://kolesa.kz/zapchasti/");
        $("#_txt_").setValue(testData).pressEnter();
        $("#results h1").shouldHave(text(testData));
    }

    @CsvSource({
            "джинсы мужские, По запросу «джинсы мужские» найдено",
            "худи с принтом, По запросу «худи с принтом» найдено"
    }) //test_data
    @ParameterizedTest(name = "Наименование результата поиска и наличие в нем ключевого слова {0}")
    void wildberriesSearchCommonTestDifferentExpectedText(String searchQuery, String expectedText) {
        open("https://www.wildberries.ru/");
        $("#searchInput").click();
        $("#searchInput").setValue(searchQuery).pressEnter();
        $$("div.searching-results__inner")
                .shouldHave(CollectionCondition.texts(expectedText))
                .shouldHave(CollectionCondition.texts(searchQuery));
    }

    static Stream<Arguments> MenuList() {
        return Stream.of(
                Arguments.of(Locale.Русский, List.of("Легковые", "Дилеры", "Мототехника", "Водный транспорт")),
                Arguments.of(Locale.Қазақша, List.of("Жеңіл авто", "Дилерлер", "Мототехника", "Су көлігі"))
        );
    }

    @MethodSource("MenuList")
    @ParameterizedTest(name = "Check menu names for locale: {0}")
    void testSitemMenuListText(Locale locale, List<String> buttonsTexts) {
        open("https://kolesa.kz/");
        $(".header-menu-dropdown__toggler-icon").click();
        $$(".locale-menu__item").find(text(locale.name())).click();
        $$(".action-list li").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }


}

