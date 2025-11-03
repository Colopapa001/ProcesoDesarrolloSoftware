package com.sportsmatching;

import com.sportsmatching.controller.MatchController;
import com.sportsmatching.controller.UserController;
import com.sportsmatching.data.ReferenceData;
import com.sportsmatching.model.SkillLevel;
import com.sportsmatching.model.SportType;
import com.sportsmatching.repository.InMemoryMatchRepository;
import com.sportsmatching.repository.InMemoryUserRepository;
import com.sportsmatching.service.MatchService;
import com.sportsmatching.service.MatchingService;
import com.sportsmatching.strategy.BySkillLevelStrategy;
import com.sportsmatching.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        // Initialize reference data from JSON
        ReferenceData ref = ReferenceData.get();

        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryMatchRepository matchRepository = new InMemoryMatchRepository();

        MatchingService matchingService = new MatchingService(new BySkillLevelStrategy());
        MatchService matchService = new MatchService(matchRepository, userRepository, matchingService);

        UserController userController = new UserController(userRepository);
        MatchController matchController = new MatchController(matchService);
        ConsoleView view = new ConsoleView(userController, matchController);

        // Seed a few users using reference data
        SportType football = ref.sport("FOOTBALL");
        SkillLevel inter = ref.skill("INTERMEDIATE");
        SkillLevel adv = ref.skill("ADVANCED");
        SkillLevel beg = ref.skill("BEGINNER");

        userController.registerUser("ana", "ana@example.com", "secret", football, inter);
        userController.registerUser("beto", "beto@example.com", "secret", football, adv);
        userController.registerUser("cami", "cami@example.com", "secret", football, beg);

        view.runDemo();
    }
}

