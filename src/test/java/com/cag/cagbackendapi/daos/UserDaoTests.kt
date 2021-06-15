package com.cag.cagbackendapi.daos

import com.cag.cagbackendapi.constants.LoggerMessages
import com.cag.cagbackendapi.daos.impl.UserDao
import com.cag.cagbackendapi.dtos.UserDto
import com.cag.cagbackendapi.dtos.UserRegistrationDto
import com.cag.cagbackendapi.dtos.UserUpdateDto
import com.cag.cagbackendapi.entities.UserEntity
import com.cag.cagbackendapi.repositories.UserRepository
import com.cag.cagbackendapi.services.uuid.impl.UuidService
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.slf4j.Logger
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserDaoTests {
    private var userRepository: UserRepository = mock()
    private var passwordEncoder: PasswordEncoder = mock()
    private var uuidService: UuidService = mock()
    private var logger: Logger = mock()

    @InjectMocks
    private lateinit var userDao: UserDao

    @Test
    fun saveUser_validUser_logsAndSucceeds() {
        // assemble
        val inputPass = "password"
        val generatedSessionId = UUID.randomUUID()
        val inputUser = UserRegistrationDto("testy", "tester", "testytester@aol.com", inputPass, true, true)
        val encodedPass = "encodedPass"
        val inputUserWithEncodedPass = UserRegistrationDto("testy", "tester", "testytester@aol.com", encodedPass, true, true)
        val userEntity = UserEntity(
            userId = null,
            first_name = inputUser.first_name,
            last_name = inputUser.last_name,
            email = inputUser.email,
            pass = encodedPass,
            active_status = true,
            session_id = generatedSessionId.toString(),
            img_url = null,
            agreed_18 = inputUser.agreed_18,
            agreed_privacy = inputUser.agreed_privacy
        )
        val expectedUser = UserDto(null, "testy", "tester", "testytester@aol.com", true, generatedSessionId.toString(), null, true, true)

        whenever(passwordEncoder.encode(inputUser.pass)).thenReturn(encodedPass)
        doNothing().whenever(logger).info(LoggerMessages.LOG_SAVE_USER(inputUserWithEncodedPass))
        whenever(uuidService.generateRandomUUID()).thenReturn(generatedSessionId)
        whenever(userRepository.save(userEntity)).thenReturn(userEntity)

        // act
        val resultUser = userDao.saveUser(inputUser)

        // assert
        assertEquals(expectedUser, resultUser)

        verify(passwordEncoder).encode(inputPass)
        verify(logger).info(LoggerMessages.LOG_SAVE_USER(inputUserWithEncodedPass))
        verify(uuidService).generateRandomUUID()
        verify(userRepository).save(userEntity)
        verifyNoMoreInteractions(passwordEncoder, logger, userRepository)
    }

    @Test
    fun getUser_validInput_returnsUserObject() {
        val userUUID = UUID.randomUUID()
        val expectedUserEntity = UserEntity(
            userId = userUUID,
            first_name = "testfname",
            last_name = "testlname",
            email = "testemail",
            pass = "testencodedPass",
            active_status = true,
            session_id = "testgeneratedSessionId",
            img_url = null,
            agreed_18 = true,
            agreed_privacy = true
        )

        doNothing().whenever(logger).info(LoggerMessages.GET_USER(userUUID))
        whenever(userRepository.getByUserId(userUUID)).thenReturn(expectedUserEntity)

        val actualUserDto = userDao.getUser(userUUID)

        assertEquals(expectedUserEntity.toDto(), actualUserDto)

        verify(logger).info(LoggerMessages.GET_USER(userUUID))
        verify(userRepository).getByUserId(userUUID)
        verifyNoMoreInteractions(logger, userRepository)
        verifyZeroInteractions(passwordEncoder, uuidService)
    }

    @Test
    fun loginAndGetUser_validUser_logsInUserAndSucceeds() {
        // assemble
        val userUUID = UUID.randomUUID()
        val generatedSessionId = UUID.randomUUID().toString()
        val inputPass = "password"
        val inputUser = UserRegistrationDto("testy", "tester", "testytester@aol.com", inputPass, true, true)
        val encodedPass = "encodedPass"
        val userEntity = UserEntity(
            userId = null,
            first_name = inputUser.first_name,
            last_name = inputUser.last_name,
            email = inputUser.email,
            pass = encodedPass,
            active_status = true,
            session_id = generatedSessionId,
            img_url = null,
            agreed_18 = inputUser.agreed_18,
            agreed_privacy = inputUser.agreed_privacy
        )
        val expectedUser = UserDto(null, "testy", "tester", "testytester@aol.com", true, generatedSessionId, null, true, true)

        doNothing().whenever(logger).info(LoggerMessages.LOGIN_USER(userUUID))
        whenever(userRepository.getByUserId(userUUID)).thenReturn(userEntity)
        whenever(passwordEncoder.matches(inputPass, userEntity.pass)).thenReturn(true)

        // act
        val resultUser = userDao.loginAndGetUser(userUUID, inputPass)

        // assert
        assertEquals(expectedUser, resultUser)

        verify(logger).info(LoggerMessages.LOGIN_USER(userUUID))
        verify(userRepository).getByUserId(userUUID)
        verify(passwordEncoder).matches(inputPass, userEntity.pass)
        verifyNoMoreInteractions(passwordEncoder, logger, userRepository)
        verifyZeroInteractions(uuidService)
    }

    @Test
    fun updateUser_validUser_updatesAndSucceeds() {
        // assemble
        val userUpdateDto = UserUpdateDto("newFName", "newLName", "newEmail")
        val userUUID = UUID.randomUUID()
        val oldUserEntity = UserEntity(
            userId = userUUID,
            first_name = "oldFName",
            last_name = "oldLName",
            email = "oldEmail",
            pass = "password",
            active_status = true,
            session_id = "generatedSessionId",
            img_url = null,
            agreed_18 = true,
            agreed_privacy = true
        )
        val newUserEntity = UserEntity(
            userId = userUUID,
            first_name = userUpdateDto.first_name,
            last_name = userUpdateDto.last_name,
            email = userUpdateDto.email,
            pass = "password",
            active_status = true,
            session_id = "generatedSessionId",
            img_url = null,
            agreed_18 = true,
            agreed_privacy = true
        )

        doNothing().whenever(logger).info(LoggerMessages.LOG_UPDATE_USER(userUpdateDto))
        whenever(userRepository.getByUserId(userUUID)).thenReturn(oldUserEntity)
        whenever(userRepository.save(oldUserEntity)).thenReturn(newUserEntity)

        // act
        val resultUser = userDao.updateUser(userUUID, userUpdateDto)

        // assert
        assertEquals(newUserEntity.toDto(), resultUser)

        verify(logger).info(LoggerMessages.LOG_UPDATE_USER(userUpdateDto))
        verify(userRepository).getByUserId(userUUID)
        verify(userRepository).save(oldUserEntity)
        verifyNoMoreInteractions(logger, userRepository)
        verifyZeroInteractions(uuidService, passwordEncoder)
    }

    @Test
    fun deleteUser_validInput_returnsDeletedUserObject() {
        val userUUID = UUID.randomUUID()
        val expectedUserEntity = UserEntity(
            userId = userUUID,
            first_name = "testfname",
            last_name = "testlname",
            email = "testemail",
            pass = "testencodedPass",
            active_status = true,
            session_id = "testgeneratedSessionId",
            img_url = null,
            agreed_18 = true,
            agreed_privacy = true
        )

        doNothing().whenever(logger).info(LoggerMessages.DELETE_USER(userUUID))
        whenever(userRepository.getByUserId(userUUID)).thenReturn(expectedUserEntity)
        doNothing().whenever(userRepository).deleteById(userUUID)

        val actualDeletedUserDto = userDao.deleteUser(userUUID)

        assertEquals(expectedUserEntity.toDto(), actualDeletedUserDto)

        verify(logger).info(LoggerMessages.DELETE_USER(userUUID))
        verify(userRepository).getByUserId(userUUID)
        verify(userRepository).deleteById(userUUID)
        verifyNoMoreInteractions(logger, userRepository)
        verifyZeroInteractions(uuidService, passwordEncoder)
    }
}