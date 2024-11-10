package ru.parfenov.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Список всех юзеров из хранилища
     *
     * @return список юзеров
     */
    @Override
    List<User> findAll();

    /**
     * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     *
     * @param role  роль юзера
     * @param name  имя юзера
     * @param block заблокирован ли он
     * @return список юзеров по данным параметрам
     */
    @Query("SELECT u FROM User u WHERE " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:name = '' OR u.name = :name) AND " +
            "(:blockStr = '' OR u.blocked = :block)")
    List<User> findByParameters(
            @Param("role")Role role,
            @Param("name")String name,
            @Param("blockStr")String blockStr,
            @Param("block")boolean block
    );

    /**
     * Поиск юзера по его емайл
     *
     * @param email емайл юзера
     * @return user модель -user
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск юзера пол его емайл и паролю
     *
     * @param email    емайл юзера
     * @param password пароль юзера
     * @return user модель -user
     */
    Optional<User> findByEmailAndPassword(String email, String password);
}