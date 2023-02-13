package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    /**
     * Метод проверки регистрации пользователя, затем переход на главную страницу.
     */
    @Test
    public void whenRegisterUserThenGetMainPage() {
        var user = new User(1, "Ivan", "1@ya.ru", "meow123");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Метод проверки входа пользователя по логину и паролю, затем переход на главную страницу.
     */
    @Test
    public void whenLoginUserThenGetMainPage() {
        var request = new MockHttpServletRequest();
        var user = new User(1, "Ivan", "1@ya.ru", "meow123");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));
        when(userService.findByEmailAndPassword("1@ya.ru", "meow123")).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        userController.register(model, user);
        var session = request.getSession();
        session.setAttribute("user", Optional.of(user));
        var view = userController.loginUser(user, model, request);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/index");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Метод проверки вывода страницы с регистрацией пользователя.
     */
    @Test
    public void whenGetRegistationPage() {
        var view = userController.getRegistationPage();
        assertThat(view).isEqualTo("users/register");
    }

    /**
     * Метод проверки вывода страницы со входом зарегистрированного пользователя.
     */
    @Test
    public void whenGetLoginPage() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenGetLogoutPage() {
        var request = new MockHttpServletRequest();
        var session = request.getSession();
        var view = userController.logout(session);

        assertThat(view).isEqualTo("redirect:/users/login");
    }
}