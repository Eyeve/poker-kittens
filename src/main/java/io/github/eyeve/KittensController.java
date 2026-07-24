package io.github.eyeve;

import io.github.eyeve.model.User;
import io.github.eyeve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KittensController {

    private final UserRepository repository;

    @GetMapping("/show")
    public String show() {
        return repository.findAll().toString();
    }

    @GetMapping("/add/{data}")
    public String show(@PathVariable("data") String data) {
        String[] args = data.split("-");
        String name = args[0];
        String pass = args[1];

        User temp = new User();
        temp.setUsername(name);
        temp.setPassword(pass);
        repository.save(temp);
        return "Added " + name + ", with passwd: " + pass;
    }

    @GetMapping("/clear")
    public String clear() {
        repository.deleteAll();
        return "Cleared";
    }
}
