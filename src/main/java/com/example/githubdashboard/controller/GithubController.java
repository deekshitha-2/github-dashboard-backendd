package com.example.githubdashboard.controller;

import com.example.githubdashboard.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/github")
@CrossOrigin(origins = "*")
public class GithubController {

    @Autowired
    private GithubService service;

    // 🔹 GET commits data
    @GetMapping("/commits/{username}")
    public Map<String, Integer> getCommits(@PathVariable String username) {
        return service.getCommits(username);
    }

    // 🔹 DOWNLOAD CSV (FINAL WORKING VERSION)
    @GetMapping("/download/{username}")
    public ResponseEntity<String> downloadCSV(@PathVariable String username) {

        Map<String, Integer> data = service.getCommits(username);

        StringBuilder csv = new StringBuilder();
        csv.append("Repository,Commits\n");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            csv.append(entry.getKey())
                    .append(",")
                    .append(entry.getValue())
                    .append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=github-data.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csv.toString());
    }
}