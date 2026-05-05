package com.example.githubdashboard.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GithubService {

    private final String token = System.getenv("GITHUB_TOKEN");

    public Map<String, Integer> getCommits(String username) {

        Map<String, Integer> commitData = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String repoUrl = "https://api.github.com/users/" + username + "/repos";

            System.out.println("Fetching repos for: " + username);

            ResponseEntity<List> repoResponse =
                    restTemplate.exchange(repoUrl, HttpMethod.GET, entity, List.class);

            List<Map<String, Object>> repos = repoResponse.getBody();

            if (repos == null) {
                System.out.println("No repos found");
                return commitData;
            }

            for (Map<String, Object> repo : repos) {

                String repoName = (String) repo.get("name");

                System.out.println("Repo: " + repoName);

                String commitUrl = "https://api.github.com/repos/" + username + "/" + repoName + "/commits";

                ResponseEntity<List> commitResponse =
                        restTemplate.exchange(commitUrl, HttpMethod.GET, entity, List.class);

                List<Map<String, Object>> commits = commitResponse.getBody();

                int count = (commits != null) ? commits.size() : 0;

                commitData.put(repoName, count);
            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED:");
            e.printStackTrace();
        }

        return commitData;
    }
}