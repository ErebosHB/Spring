package de.encoway.backend.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User save(User user);

    User findUserByUsername(String username);

    void delete(User user);


}
