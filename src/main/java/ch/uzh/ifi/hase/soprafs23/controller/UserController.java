package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameRoomPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameRoomService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;
    private final DTOMapper dtoMapper;

    UserController(UserService userService, DTOMapper dtoMapper) {
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers(@RequestHeader(value = "Authorization", required = false) String bearerToken) {

        throwForbiddenWhenNoBearerToken(bearerToken);

        // fetch all users in the internal representation
        List<User> users = userService.getUsers(bearerToken);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(dtoMapper.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getUser(@PathVariable Long id,
                                    @RequestHeader(value = "Authorization", required = false) String bearerToken) {

        throwForbiddenWhenNoBearerToken(bearerToken);

        User user = userService.getUser(id, bearerToken);
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        userGetDTOs.add(dtoMapper.convertEntityToUserGetDTO(user));

        return userGetDTOs;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {

        // convert API user to internal representation
        User userInput = dtoMapper.convertUserPostDTOtoEntity(userPostDTO);

        // check if password and username is set
        if (userInput.getPassword() == null || userInput.getUsername() == null) {
            String baseErrorMessage = "Oups, your request is wrong. ";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage));
        }

        // create user
        User createdUser = userService.createUser(userInput);
        // convert internal representation of user back to API
        return dtoMapper.convertEntityToUserGetDTO(createdUser);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable Long id,
                           @RequestHeader(value = "Authorization", required = false) String bearerToken,
                           @RequestBody UserPutDTO userPutDTO) {

        throwForbiddenWhenNoBearerToken(bearerToken);

        User userInput = dtoMapper.convertUserPutDTOtoEntity(userPutDTO);

        userService.updateUser(id, bearerToken, userInput);

    }

    public void throwForbiddenWhenNoBearerToken(String bearerToken) {
        if (Objects.isNull(bearerToken)) {
            String baseErrorMessage = "You need to log in to see this information.";
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format(baseErrorMessage));
        }
    }

}
