package edu.induma.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.induma.entity.UserEntity;
import edu.induma.handler.BinWebSocketHandler;
import edu.induma.model.User;
import edu.induma.repository.UserRepository;
import edu.induma.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final ObjectMapper objectMapper;
    final BinWebSocketHandler binWebSocketHandler;


    @Override
    public void add(User user) {
        userRepository.save(objectMapper.convertValue(user, UserEntity.class));
        binWebSocketHandler.sendMessageToAll(user);
    }

    @Override
    public List<User> get() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach( user -> {
            users.add(objectMapper.convertValue(user, User.class));
        });
        return users;
    }
}
