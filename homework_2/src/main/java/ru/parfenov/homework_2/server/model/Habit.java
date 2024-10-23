package ru.parfenov.homework_2.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

/**
 * Класс даёт модель, которая используется для создания привычки
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Habit {
    /**
     * ID, уникальный идентификатор привычки. Он не зависит от её хозяина-юзера
     */
    private long id;
    /**
     * юзер, который создал эту привычку в приложении
     */
    private User user;
    /**
     * Полезная эта привычка или нет, по мнению создателя? Если нет, то неуспешное её выполнение
     * будет сигналить юзеру, что он постепенно от неё отказывается !)))
     */
    private boolean useful;
    /**
     * Активна эта привычка или нет? Если нет, то она не будет беспокоить юзера напоминаниями и статистикой
     */
    private boolean active;
    /**
     * Сколько непрерывных исполнений получилось выполнить по этой привычке? Чем меньше - тем лучше(если она полезная) !
     * Если всего одна - то юзер вообще ни разу не вышел из графика выполнений! Cool!!!!!
     */
    private int streaksAmount;
    /**
     * Как назвали эту привычку?
     */
    private String name;
    /**
     * А можно подробнее?
     */
    private String description;
    /**
     * Когда её создали?
     */
    private LocalDate dateOfCreate;
    /**
     * И когда планируется первое выполнение?
     */
    private LocalDate  plannedFirstPerform;
    /**
     * А когда последний раз нужно было выполнить привычку? ;) Она не поменяется, если ничего не было в реале
     */
    private LocalDate plannedPrevPerform;
    /**
     * Ну то есть через столько -то (периодичность, частота) дней следующее выполнение?
     */
    private LocalDate plannedNextPerform;
    /**
     * Так а реально когда последний раз выполнял?
     */
    private LocalDate lastRealPerform;
    /**
     * Вот она, частота выполнений
     */
    private Period frequency;
    /**
     * А реальное количество выполнений - это вот
     */
    private int performsAmount;

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", user=" + user.getEmail() +
                ", usefulness=" + useful +
                ", active=" + active +
                ", streaksAmount=" + streaksAmount +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateOfCreate=" + dateOfCreate +
                ", plannedFirstPerform=" + plannedFirstPerform +
                ", plannedPrevPerform=" + plannedPrevPerform +
                ", plannedNextPerform=" + plannedNextPerform +
                ", lastRealPerform=" + lastRealPerform +
                ", frequency=" + frequency +
                ", performsAmount=" + performsAmount +
                '}';
    }
}