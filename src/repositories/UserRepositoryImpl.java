package repositories;

import models.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 scan file data/users.txt -> add object models.User 
 user.txt format: username:password:role1,role2
 */
public class UserRepositoryImpl implements IUserRepository {
    private static final String FILE_PATH = "data/user.txt";
    private final List<User> users = new ArrayList<>();

    public UserRepositoryImpl() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); //delete leading/trailing whitespace
                if (line.isEmpty()) continue;
                String[] parts = line.split(":"); //split username, password, roles
                if (parts.length >= 3) {
                    //save role
                    Set<String> roles = new HashSet<>(Arrays.asList(parts[2].split(",")));

                    //add user
                    users.add(new User(parts[0].trim(), parts[1].trim(), roles));
                }
            }
            System.out.println("[UserRepository] Loaded " + users.size() + " accounts from file.");
        } catch (IOException e) {
            System.out.println("[UserRepository] Cannot read file '" + FILE_PATH + "': " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }
}
