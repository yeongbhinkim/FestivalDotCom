package com.googoo.festivaldotcom.domain.festivalApi.presentation;

import com.googoo.festivaldotcom.domain.festivalApi.service.OpenAiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ImageController {
    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/generateImage")
    public ResponseEntity<String> generateImage(@RequestBody String prompt) {
        try {
            String imageUrl = openAiService.generateImage(prompt);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating image : " + e.getMessage());
        }
    }
}
