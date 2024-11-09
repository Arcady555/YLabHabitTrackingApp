package ru.parfenov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.anotation.EnableParfenovCustomAspect;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.service.HabitService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/habits")
@RequiredArgsConstructor
@Tag(name = "Контроллер привычек", description = "служит для работы юзеров с привычками")
public class HabitController {
    private final HabitService habitService;

    /**
     * Страница, где пользователь создаёт свою привычку с занесением в базу данных
     * Метод обработает HTTP запрос Post.
     *
     * @param habitDTO сущность Habit, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @Operation(
            summary = "Создание",
            description = "Создание привычки юзером, её сохранение в БД"
    )
    @PostMapping("/create")
    @EnableParfenovCustomAspect
    public ResponseEntity<HabitGeneralDTO> create(@RequestBody HabitCreateDTO habitDTO) {
        Optional<HabitGeneralDTO> habitGeneralDTO = habitService.create(habitDTO);
        if (habitGeneralDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return habitGeneralDTO
                    .map(habitGeneralDTOGet -> new ResponseEntity<>(habitGeneralDTOGet, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
    }

    /**
     * Обработка запроса на вывод привычек юзера и статистики по ним за кокой-либо период
     *
     * @param dateFrom начальная дата периода для вывода статистики
     * @param dateTo   конечная дата периода для вывода статистики
     * @return ответ сервера
     */
    @Operation(
            summary = "Статистика",
            description = "Вывод списка привычек со статистикой по ним"
    )
    @GetMapping("/statistic")
    @EnableParfenovCustomAspect
    public ResponseEntity<List<HabitStatisticDTO>> statistic(
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        List<HabitStatisticDTO> result = habitService.statisticForUser(dateFrom, dateTo);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Обработка запроса на выполнение юзером своей привычки
     *
     * @param habitId ID привычки
     * @return ответ сервера
     */
    @Operation(
            summary = "Выполнение",
            description = "Выполнение юзером своей привычки"
    )
    @PostMapping("/perform/{habitId}")
    @EnableParfenovCustomAspect
    public ResponseEntity<HabitGeneralDTO> perform(@PathVariable long habitId) {
        Optional<HabitGeneralDTO> result = habitService.perform(habitId);
        return result.map(
                habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
                );
    }

    /**
     * Обработка запроса на обновление юзером своей привычки
     *
     * @param habitDTO сущность Habit, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @Operation(
            summary = "Обновление",
            description = "Изменение юзером данных по своей привычке"
    )
    @PostMapping("/update")
    @EnableParfenovCustomAspect
    public ResponseEntity<HabitGeneralDTO> update(@RequestBody HabitUpdateDTO habitDTO) {
        Optional<HabitGeneralDTO> result = habitService.updateByUser(habitDTO);
        return result.map(
                habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
                );
    }

    /**
     * Обработка запроса на вывод привычек юзера по заданным параметрам
     * Параметры ниже могут не заданы, если не участвуют в отборе:
     *
     * @param usefulness   полезность
     * @param active       активность
     * @param name         название привычки
     * @param description  часть строки в описании привычки
     * @param dateOfCreate дата создания
     * @param frequency    частота выполнения в днях
     * @return ответ сервера
     */
    @Operation(
            summary = "Поиск по параметрам",
            description = "Вывод списка своих привычек, отсортированных по указанным параметрам"
    )
    @GetMapping("/your-list-by-param")
    @EnableParfenovCustomAspect
    public ResponseEntity<List<HabitGeneralDTO>> findByParam(
            @RequestParam(required = false) String usefulness,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String dateOfCreate,
            @RequestParam(required = false) int frequency
    ) {
        List<HabitGeneralDTO> result =
                habitService.findByParameters(usefulness, active, name, description, dateOfCreate, frequency);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Обработка запроса на вывод всех привычек юзера
     *
     * @return ответ сервера
     */
    @Operation(
            summary = "Все привычки",
            description = "Вывод списка своих привычек"
    )
    @GetMapping("/your-all-list")
    @EnableParfenovCustomAspect
    public ResponseEntity<List<HabitGeneralDTO>> findYourAll() {
        List<HabitGeneralDTO> result = habitService.findByUser();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    /**
     * Обработка запроса на вывод всех привычек юзера к выполнению сегодня
     *
     * @return ответ сервера
     */
    @Operation(
            summary = "Привычки на сегодня",
            description = "Вывод своих привычек, намеченных на сегодня"
    )
    @GetMapping("/your-today-list")
    @EnableParfenovCustomAspect
    public ResponseEntity<List<HabitGeneralDTO>> findTodayList() {
        List<HabitGeneralDTO> result = habitService.todayPerforms();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}