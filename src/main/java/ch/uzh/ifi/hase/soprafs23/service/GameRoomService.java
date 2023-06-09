package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.GameRoom;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class GameRoomService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public GameRoomService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User retrieveUserFromRepo(long userId){
        return userRepository.findByid(userId);
    }

    public GameRoom initGameRoom(GameRoom gameRoom) {
        gameRoom.setRoomCode(new Random().nextInt(100000, 1000000));
        //list unmodifiable -> every time pass a new one
        gameRoom.setMembers(List.of(gameRoom.getLeader()));
        return gameRoom;
    }

    public GameRoom addPlayerToGameRoom(GameRoom gameRoom, long userId) {
        //list unmodifiable -> every time pass a new one
        gameRoom.setMembers(List.of(gameRoom.getLeader()));
        User player = retrieveUserFromRepo(userId);
        List<User> members = Stream.concat(gameRoom.getMembers().stream(), Stream.of(player)).toList();
        gameRoom.setMembers(members);
        return gameRoom;
    }
}
