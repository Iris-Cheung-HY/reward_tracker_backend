// package com.rewardtracker.backend;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class BackendApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(BackendApplication.class, args);
// 	}

// }

package com.rewardtracker.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BackendApplication {
	@RequestMapping("/")  // ← 新增 Hello endpoint
    public String home() {
        return "Hello Docker World - Reward Tracker Backend";
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

