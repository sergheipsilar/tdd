package com.frequentis.tdd;

import com.frequentis.tdd.data.Users;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    private UserController sut;
    private UserRepository userRepository;

    @Before
    public void setUp(){
        userRepository = mock(UserRepository.class);
        sut = new UserController(userRepository);
    }

    @Test
    public void create_newUser_storesUserInRepository(){
        // Given
        User user = Users.random();

        // When
        sut.create(user);

        // Then
        verify(userRepository).save(user);
    }

    @Test
    public void create_newUser_returnsNewlyCreatedUser(){
        // Given
        User newUser = Users.random();
        User dbUser = prepareNewUserInRepository(newUser);

        // When
        User actualUser = sut.create(newUser);

        // Then
        assertThat("Expected user to match", actualUser, equalTo(dbUser));
    }

    @Test
    public void getAll_usersPresent_returnAllUsersFromRepository(){
        // Given
        List<User> users = prepareUsersInRepository();

        // When
        List<User> actualUsers = sut.getAll();

        // Then
        assertThatAllUsersMatche(users, actualUsers);
    }

    private void assertThatAllUsersMatche(final List<User> users, final List<User> actualUsers) {
        assertThat("Expected all users returned from repository", actualUsers.size(), equalTo(users.size()));
        for (User user : users) {
            assertThat("Expected user returned from repository", actualUsers, hasItem(equalTo(user)));
        }
    }

    private List<User> prepareUsersInRepository() {
        List<User> users = Lists.newArrayList(Users.randomWithId(), Users.randomWithId());
        when(userRepository.findAll()).thenReturn(users);
        return users;
    }

    private User prepareNewUserInRepository(final User user) {
        User dbUser = Users.randomWithId();
        when(userRepository.save(user)).thenReturn(dbUser);
        return dbUser;
    }
}