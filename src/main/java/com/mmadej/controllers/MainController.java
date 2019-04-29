package com.mmadej.controllers;

import com.mmadej.entities.Languages;
import com.mmadej.entities.Repository;
import com.mmadej.entities.Statistics;
import com.mmadej.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class MainController {


    @Autowired
    private StatisticsService statisticsService;



    @GetMapping(value = "/{user}", produces = "application/json")
    public Statistics statisticsController(@PathVariable String user){

        List<Repository> repositoryList = statisticsService.getRepositoryList(user);
        Languages languages = statisticsService.sumLanguageBytes(user, repositoryList);

        Statistics statistics = new Statistics();
        statistics.setRepositoriesCount(repositoryList.size());
        statistics.setLanguages(languages);
        return statistics;
    }

}
