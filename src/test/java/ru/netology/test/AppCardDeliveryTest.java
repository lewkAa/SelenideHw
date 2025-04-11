package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {

    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void ShoudlSendFormWithValidData() {

        String planningDate = generateDate(4, "dd.MM.YYYY");

        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE).setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Алексей Харламов");
        $("[data-test-id='phone'] input").setValue("+79158855445");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(Selectors.withText("Встреча успешно забронирована на")).shouldHave(text(planningDate)).should(Condition.visible, Duration.ofSeconds(15));

    }

    @Test
    void ShouldSendFormWithInputHelpAndManualDataSelection() {
        String planningDate = generateDate(7, "dd.MM.yyyy");
        String planningDateDay = generateDate(7, "dd");

        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Мо");
        $(Selectors.withText("Москва")).click();
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        if ($$("[data-day]").find(exactText(planningDateDay)).exists()) {
            $$("[data-day]").find(exactText(planningDateDay)).click();
        } else {
            $("[data-step='1']").click();
            $$("[data-day]").find(exactText(planningDateDay)).click();
        }
        $("[data-test-id='name'] input").setValue("Алексей Харламов");
        $("[data-test-id='phone'] input").setValue("+79158855445");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $(Selectors.withText("Успешно!")).should(Condition.visible, Duration.ofSeconds(15));
        $(Selectors.withText("Встреча успешно забронирована на")).shouldHave(text(planningDate)).should(Condition.visible, Duration.ofSeconds(15));
    }
}
