package abuldovi.apps.bugtracker.services;

import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(UserEntity s){
        s.setPassword(passwordEncoder.encode(s.getPassword()));
        s.setCreatedat(LocalDateTime.now());
        usersRepository.save(s);
    }
}
