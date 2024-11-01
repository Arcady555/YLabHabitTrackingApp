package ru.parfenov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.dto.habit.*;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/habits")
public class HabitController {
    private final UserService userService;
    private final HabitService habitService;

    @Autowired
    public HabitController(UserService userService, HabitService habitService, HabitDTOMapper dtoMapper) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Страница, где пользователь создаёт свою привычку с занесением в базу данных
     * Метод обработает HTTP запрос Post.
     *
     * @param request  HTTP запрос
     * @param habitDTO сущность Habit, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/create")
    public ResponseEntity<HabitGeneralDTO> create(HttpServletRequest request, @RequestBody HabitCreateDTO habitDTO) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<HabitGeneralDTO> habit = habitService.create(user, habitDTO);
            return habit
                    .map(habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обработка запроса на вывод привычек юзера и статистики по ним за кокой-либо период
     *
     * @param request  HTTP запрос
     * @param dateFrom начальная дата периода для вывода статистики
     * @param dateTo   конечная дата периода для вывода статистики
     * @return ответ сервера
     */
    @GetMapping("/statistic")
    public ResponseEntity<List<HabitStatisticDTO>> statistic(HttpServletRequest request, @PathVariable String dateFrom, @PathVariable String dateTo) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<HabitStatisticDTO> result = habitService.statisticForUser(user, dateFrom, dateTo);
            if (!result.isEmpty()) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обработка запроса на выполнение юзером своей привычки
     *
     * @param request HTTP запрос
     * @param habitId ID привычки
     * @return ответ сервера
     */
    @PostMapping("/perform")
    public ResponseEntity<HabitGeneralDTO> perform(HttpServletRequest request, @PathVariable long habitId) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<HabitGeneralDTO> result = habitService.perform(user, habitId);
            return result.map(
                            habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
                    );
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обработка запроса на обновление юзером своей привычки
     *
     * @param request  HTTP запрос
     * @param habitDTO сущность Habit, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/update")
    public ResponseEntity<HabitGeneralDTO> update(HttpServletRequest request, @RequestBody HabitUpdateDTO habitDTO) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            Optional<HabitGeneralDTO> result = habitService.updateByUser(userOptional.get(), habitDTO);
            return result.map(
                            habitGeneralDTO -> new ResponseEntity<>(habitGeneralDTO, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)
                    );
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обработка запроса на вывод привычек юзера по заданным параметрам
     *
     * @param request      HTTP запрос
     *                     Параметры ниже могут быть заданы пустой строкой, если не участвуют в отборе:
     * @param usefulness   полезность
     * @param active       активность
     * @param name         название привычки
     * @param description  часть строки в описании привычки
     * @param dateOfCreate дата создания
     * @param frequency    частота выполнения в днях
     * @return ответ сервера
     */
    @GetMapping("/your-list-by-param")
    public ResponseEntity<List<HabitGeneralDTO>> findByParam(
            HttpServletRequest request,
            @PathVariable String usefulness,
            @PathVariable String active,
            @PathVariable String name,
            @PathVariable String description,
            @PathVariable String dateOfCreate,
            @PathVariable String frequency
    ) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            List<HabitGeneralDTO> result =
                    habitService.findByParameters(userOptional.get(), usefulness, active, name, description, dateOfCreate, frequency);
            if (!result.isEmpty()) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обработка запроса на вывод всех привычек юзера
     *
     * @param request      HTTP запрос
     * @return ответ сервера
     */
    @GetMapping("/your-all-list")
    public ResponseEntity<List<HabitGeneralDTO>> findYourAll(HttpServletRequest request) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            List<HabitGeneralDTO> result = habitService.findByUser(userOptional.get());
            if (!result.isEmpty()) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обработка запроса на вывод всех привычек юзера к выполнению сегодня
     *
     * @param request      HTTP запрос
     * @return ответ сервера
     */
    @GetMapping("/your-today-list")
    public ResponseEntity<List<HabitGeneralDTO>> findTodayList(HttpServletRequest request) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            List<HabitGeneralDTO> result = habitService.todayPerforms(userOptional.get());
            if (!result.isEmpty()) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}