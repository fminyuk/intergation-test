package org.nnc.research.it;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @RequestMapping(value = "get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User get(@PathVariable("id") final int id) {
        final User user = new User();

        user.setId(id);
        user.setName("Andrey");

        return user;
    }
}
