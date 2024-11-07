package ru.parfenov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

/**
 * Класс даёт модель, которая используется для создания привычки
 */
@Entity
@Table(name = "habits", schema = "ht_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
    /**
     * ID, уникальный идентификатор привычки. Он не зависит от её хозяина-юзера
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * юзер, который создал эту привычку в приложении
     */
    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_reference"))
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
     * Описание привычки
     */
    private String description;

    /**
     * Дата создания привычки
     */
    private LocalDate dateOfCreate;

    /**
     * Запланированное первое выполнение
     */
    private LocalDate plannedFirstPerform;

    /**
     * Предыдущая дата выполнения привычки по графику. Первоначально null, потом меняется, только если было новое выполнение
     */
    private LocalDate plannedPrevPerform;

    /**
     * Следующая дата выполнения привычки по графику. Первоначально = plannedFirstPerform + frequency, потом меняется, только если было новое выполнение
     */
    private LocalDate plannedNextPerform;

    /**
     * Дата реального выполнения привычки. На неё ориентир при выставлении новых сроков
     */
    private LocalDate lastRealPerform;

    /**
     * Частота выполнений. Модно выставить от 1го дня до 90(Можно изменить в настройках)
     */
    @Column(name = "frequency")
    private Period frequency;

    /**
     * Количество реальных выполнений привычки. +1 после каждого выполнения
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
                ", dateOfCreate=" + dateOfCreate.toString() +
                ", plannedFirstPerform=" + plannedFirstPerform.toString() +
                ", plannedPrevPerform=" + plannedPrevPerform.toString() +
                ", plannedNextPerform=" + plannedNextPerform.toString() +
                ", lastRealPerform=" + lastRealPerform.toString() +
                ", frequency=" + frequency.getDays() +
                ", performsAmount=" + performsAmount +
                '}';
    }
}