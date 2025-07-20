//package com.apis.postgrestesting.controller;
//
//import com.apis.postgrestesting.entity.UrlEntity;
//import com.apis.postgrestesting.service.UrlService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/url")
//public class UrlController {
//
//    @Autowired
//    private UrlService urlService;
//
//    @PostMapping("/add")
//    public UrlEntity addUrl(@RequestBody UrlEntity url) {
//        return urlService.addUrl(url);
//    }
//
//    @GetMapping("/all")
//    public List<UrlEntity> getAllUrlsByUser(@RequestParam("userId") Long userId) {
//        return urlService.getAllUrlsByUserId(userId);
//    }
//
//    @GetMapping("/{id}")
//    public UrlEntity getUrlById(@PathVariable Long id) {
//        return urlService.getUrlById(id)
//                .orElseThrow(() -> new RuntimeException("URL not found"));
//    }
//
//    @PutMapping("/update/{id}")
//    public UrlEntity updateUrl(@PathVariable Long id, @RequestBody UrlEntity updatedUrl) {
//        return urlService.updateUrl(id, updatedUrl);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String deleteUrl(@PathVariable Long id) {
//        return urlService.deleteUrl(id);
//    }
//}








//
//
//
//package com.apis.postgrestesting.controller;
//
//import com.apis.postgrestesting.entity.UrlEntity;
//import com.apis.postgrestesting.service.UrlService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/url")
//public class UrlController {
//
//    @Autowired
//    private UrlService urlService;
//
//    @PostMapping("/add")
//    public UrlEntity addUrl(@RequestBody Map<String, Object> requestMap) {
//        return urlService.addUrlFromMap(requestMap);
//    }
//
//    @GetMapping("/all")
//    public List<UrlEntity> getAllUrlsByUser(@RequestParam("userId") Long userId) {
//        return urlService.getAllUrlsByUserId(userId);
//    }
//
//    @GetMapping("/{id}")
//    public UrlEntity getUrlById(@PathVariable Long id) {
//        return urlService.getUrlById(id)
//                .orElseThrow(() -> new RuntimeException("URL not found"));
//    }
//
//    @PutMapping("/update/{id}")
//    public UrlEntity updateUrl(@PathVariable Long id, @RequestBody Map<String, Object> requestMap) {
//        return urlService.updateUrlFromMap(id, requestMap);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String deleteUrl(@PathVariable Long id) {
//        return urlService.deleteUrl(id);
//    }
//}
//










//











package com.apis.postgrestesting.controller;

import com.apis.postgrestesting.entity.UrlEntity;
import com.apis.postgrestesting.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    // ✅ Add URL
    @PostMapping("/add")
    public UrlEntity addUrl(HttpServletRequest request,
                            @RequestBody Map<String, Object> requestMap) {
        String token = request.getHeader("token");
        return urlService.addUrl(token, requestMap);
    }

    // ✅ Get all URLs for logged-in user
    @GetMapping("/all")
    public List<UrlEntity> getAllUrls(HttpServletRequest request) {
        String token = request.getHeader("token");
        return urlService.getAllUrlsByUser(token);
    }

    // ✅ Get specific URL by ID
    @GetMapping("/{id}")
    public UrlEntity getUrlById(HttpServletRequest request,
                                @PathVariable Long id) {
        String token = request.getHeader("token");
        return urlService.getUrlByIdForUser(token, id);
    }

    // ✅ Update URL by ID
    @PutMapping("/update/{id}")
    public UrlEntity updateUrl(HttpServletRequest request,
                               @PathVariable Long id,
                               @RequestBody Map<String, Object> requestMap) {
        String token = request.getHeader("token");
        return urlService.updateUrl(token, id, requestMap);
    }

    // ✅ Delete URL by ID
    @DeleteMapping("/delete/{id}")
    public String deleteUrl(HttpServletRequest request,
                            @PathVariable Long id) {
        String token = request.getHeader("token");
        return urlService.deleteUrl(token, id);
    }
}







