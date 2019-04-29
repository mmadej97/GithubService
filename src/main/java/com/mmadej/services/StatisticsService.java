package com.mmadej.services;

import com.mmadej.entities.Languages;
import com.mmadej.entities.Repository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class StatisticsService {

    private final RestTemplate restTemplate;

    public StatisticsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public  MultiValueMap<String, String> getHeaders(){

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        //application usses environmental variables
        String username = System.getenv("GITHUB_USER");
        String password = System.getenv("GITHUB_PASSWORD");
        if(username == null || password == null )
            return null;

        String authorization = String.format("%s:%s", username, password);
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(authorization.getBytes()));
        return headers;
    }


    public List<Repository> getRepositoryList(String user){

        String getRepositoriesUrl  = "https://api.github.com/users/{users}/repos?page={pageNr}";
        HttpEntity<Object> requestEntity = new HttpEntity<>(getHeaders());

        ResponseEntity<Repository[]> responseEntity = restTemplate.exchange(getRepositoriesUrl, HttpMethod.GET, requestEntity ,Repository[].class, user,1);
        List<Repository> totalRepositoryList = new LinkedList<>(Arrays.asList(responseEntity.getBody()));

        List<Repository> repositoryList = new LinkedList<>();
        if(responseEntity.getHeaders().get("Link") != null) {

            String link = responseEntity.getHeaders()
                                        .get("Link")
                                        .get(0);

            String[] strArr = link.split(" ");
            String lastPage = strArr[2];

            int index1 = lastPage.lastIndexOf("page=");
            int index2 = lastPage.lastIndexOf(">;");
            Integer lastPageNumber = Integer.valueOf(lastPage.substring(index1 + 5, index2));

            for (int i = 2; i <= lastPageNumber; i++) {
                responseEntity = restTemplate.exchange(getRepositoriesUrl, HttpMethod.GET, requestEntity, Repository[].class, user, i);
                repositoryList.addAll(Arrays.asList(responseEntity.getBody()));
            }
        }

        totalRepositoryList.addAll(repositoryList);
        return totalRepositoryList;
    }


    public Languages sumLanguageBytes(String user, List<Repository> arr){

        Languages languages = new Languages();
        Map<String,Long> languagesMap = languages.getLanguagesMap();
        for(Repository repo : arr){
           Languages repoLanguages =  getLanguagesBytes(user, repo.getName());
           Map <String,Long> repoLanguagesMap = repoLanguages.getLanguagesMap();

           for(String key : repoLanguagesMap.keySet()){
               if(languagesMap.containsKey(key))
                   languagesMap.put(key, languagesMap.get(key) + repoLanguagesMap.get(key));
               else
                   languagesMap.put(key, repoLanguagesMap.get(key));
           }
        }
        return languages;
    }


    public Languages getLanguagesBytes(String user, String repo){

        String getNumberOfBytesUrl = "https://api.github.com/repos/{user}/{repo}/languages";
        HttpEntity<Object> requestEntity = new HttpEntity<>(getHeaders());
        ResponseEntity<Languages> responseEntity = restTemplate.exchange(getNumberOfBytesUrl, HttpMethod.GET, requestEntity , Languages.class, user,repo);
        Languages languages = responseEntity.getBody();
        return languages;
    }
}


