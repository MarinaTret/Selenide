import com.codeborne.selenide.Browser;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.open;

public class CardWithDeliveryTest {

    private String generateData(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
//успешная отправка формы
    void ValidForm() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + planningDate));
    }

    @Test
        //невалидный город
    void invalidCity() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Париж");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
        //не введен город
    void EmptyCity() {
        open("http://localhost:9999");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
        //невалидная дата
    void InvalidDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(0, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
        //нелидное имя
    void InvalidName() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
        //не введено имя
    void EmptyName() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='phone'] input").setValue("+7923456789");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
        //неалидный телефон
    void InvalidPhone() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+792345679");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
        //не указан телефон
    void EmptyPhone() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
        //пустой Checkbox
    void EmptyCheckbox() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("button.button").click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldBe(Condition.visible)
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
        //выбор города из выпадающено списка
    void selectingСity() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Са");
        $$(".menu-item__control").findBy(Condition.text("Санкт-Петербург")).click();
        String planningDate = generateData(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + planningDate));
    }

    @Test
        //выбор даты из выпадающено списка
    void selectingDate() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateData(3, "dd.MM.yyyy");
        //$("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").click();
        if (!generateData(3, "MM").equals(generateData(7, "MM"))) {
            $$("div.calendar__arrow.calendar__arrow_direction_right").get(1).click();
            return;
        }
        $$(".calendar-input__calendar-wrapper").findBy(Condition.text(generateData(7, "dd"))).click();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79234567899");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='notification']")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + planningDate));
    }
}
