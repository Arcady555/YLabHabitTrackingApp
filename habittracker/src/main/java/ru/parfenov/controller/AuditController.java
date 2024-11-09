package ru.parfenov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.parfenov.model.LogRecord;
import ru.parfenov.service.LogService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@Tag(name = "Контроллер аудита", description = "служит для работы с логами юзеров, попавших в БД")
public class AuditController {
    private final LogService logService;

    /**
     * Данный метод, доступный только админу(через SecurityFilterChain), позволяет посмотреть те логи,
     * которые сохранены в БД, отсортировав их по параметрам(указывать можно не все):
     *
     * @param userId       ID юзера
     * @param action       его действие (название метода в блоке SERVICE)
     * @param dateTimeFrom с какого времени
     * @param dateTimeTo   по какое время
     * @return ответ сервера в виде требуемого списка
     */
    @Operation(
            summary = "Поиск по параметрам",
            description = "Вывод списка логов, отсортированных по указанным параметрам"
    )
    @GetMapping("/find-by-parameters")
    public ResponseEntity<List<LogRecord>> findUsersByParam(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String dateTimeFrom,
            @RequestParam(required = false) String dateTimeTo
    ) {
        List<LogRecord> logsRecords = logService.findByParameters(userId, action, dateTimeFrom, dateTimeTo);
        return !logsRecords.isEmpty() ?
                new ResponseEntity<>(logsRecords, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}