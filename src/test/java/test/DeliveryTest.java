package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Keys;
import util.ScreenShooterReportPortalExtension;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static util.LoggingUtils.logInfo;

@ExtendWith({ScreenShooterReportPortalExtension.class})
class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        Data.UserInfo validUser = Data.Registration.generateUser("Ru");
        int daysToAddForFirstMeeting = 5;
        String firstMeetingDate = Data.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = Data.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL + "A", Keys.DELETE));
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        logInfo("В поле ввода дата введено: " + firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        logInfo("В поле ввода Имя введено: " + validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        logInfo("В поле ввода телефон введено: " + validUser.getPhone());
        $("[data-test-id=agreement]").click();
        logInfo("клик по соглашению: ");
        SelenideElement button = $x("//*[contains(text(), 'Запланировать')]");
        button.click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(Condition.visible);
        logInfo("Появилось сообщение об успешно запланированной встрече на " + firstMeetingDate);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME, Keys.BACK_SPACE));
        logInfo("Стерились значения первой планируемой даты ");
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        logInfo("В поле дата введена вторая дата: " + secondMeetingDate);
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible);
        logInfo("Уведомление о уже запланированной встрече");
        $(byText("Перепланировать")).click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(Condition.visible);
        logInfo("Успешно перепланирована встреча на " + secondMeetingDate);

    }


}
