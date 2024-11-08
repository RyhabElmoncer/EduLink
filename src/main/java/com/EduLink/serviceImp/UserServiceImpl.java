package com.EduLink.serviceImp;
import com.EduLink.Models.User;
import com.EduLink.repository.UserRepository;
import com.EduLink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void deleteUser(String userId) {
        // Vérifiez si l'utilisateur existe
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Supprimez l'utilisateur
        userRepository.delete(existingUser);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
