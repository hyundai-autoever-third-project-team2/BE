package com.autoever.carstore.user.service.implement;

import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;

}
