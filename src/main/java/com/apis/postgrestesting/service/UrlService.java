//package com.apis.postgrestesting.service;
//
//import com.apis.postgrestesting.entity.UrlEntity;
//import com.apis.postgrestesting.repository.UrlRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UrlService {
//
//    @Autowired
//    private UrlRepository urlRepository;
//
//    public UrlEntity addUrl(UrlEntity url) {
//        return urlRepository.save(url);
//    }
//
//    public List<UrlEntity> getAllUrlsByUserId(Long userId) {
//        return urlRepository.findByUserId(userId);
//    }
//
//    public Optional<UrlEntity> getUrlById(Long id) {
//        return urlRepository.findById(id);
//    }
//
//    public UrlEntity updateUrl(Long id, UrlEntity newUrlData) {
//        UrlEntity existingUrl = urlRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("URL not found"));
//
//        existingUrl.setUrlName(newUrlData.getUrlName());
//        existingUrl.setUrlDescription(newUrlData.getUrlDescription());
//        existingUrl.setUrlCategory(newUrlData.getUrlCategory());
//        existingUrl.setUrlLink(newUrlData.getUrlLink());
//        existingUrl.setUserId(newUrlData.getUserId());
//
//        return urlRepository.save(existingUrl);
//    }
//
//    public String deleteUrl(Long id) {
//        urlRepository.deleteById(id);
//        return "URL deleted successfully";
//    }
//}

//
//
//package com.apis.postgrestesting.service;
//
//import com.apis.postgrestesting.entity.UrlEntity;
//import com.apis.postgrestesting.repository.UrlRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class UrlService {
//
//    @Autowired
//    private UrlRepository urlRepository;
//
//    // Save URL from Map
//    public UrlEntity addUrlFromMap(Map<String, Object> map) {
//        UrlEntity url = new UrlEntity();
//        url.setUrlName((String) map.get("urlName"));
//        url.setUrlDescription((String) map.get("urlDescription"));
//        url.setUrlCategory((String) map.get("urlCategory"));
//        url.setUrlLink((String) map.get("urlLink"));
//        url.setUserId(Long.parseLong(map.get("userId").toString()));
//        return urlRepository.save(url);
//    }
//
//
//    // Get all URLs for a given userId
//    public List<UrlEntity> getAllUrlsByUserId(Long userId) {
//        return urlRepository.findByUserId(userId);
//    }
//
//    // Get a URL by its ID
//    public Optional<UrlEntity> getUrlById(Long id) {
//        return urlRepository.findById(id);
//    }
//
//    // Update URL from Map
//    public UrlEntity updateUrlFromMap(Long id, Map<String, Object> map) {
//        UrlEntity existingUrl = urlRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("URL not found"));
//
//
//        existingUrl.setUrlName((String) map.get("urlName"));
//        existingUrl.setUrlDescription((String) map.get("urlDescription"));
//        existingUrl.setUrlCategory((String) map.get("urlCategory"));
//        existingUrl.setUrlLink((String) map.get("urlLink"));
//        existingUrl.setUserId(Long.parseLong(map.get("userId").toString()));
//
//        return urlRepository.save(existingUrl);
//    }
//
//    // Delete URL by ID
//    public String deleteUrl(Long id) {
//        urlRepository.deleteById(id);
//        return "URL deleted successfully";
//    }
//}










package com.apis.postgrestesting.service;

import com.apis.postgrestesting.entity.UrlEntity;
import com.apis.postgrestesting.repository.UrlRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private final String secretKey = "This is a secret key This is a secret key This is a secret key This is a secret key"; // match with login JWT key

//    // ✅ Extract user ID from token
//    public Long extractUserId(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
//            return Long.valueOf(claims.get("userId").toString());
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid token");
//        }
    //}

    public Long extractUserId(String token) {
        try {
            Key signingKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.valueOf(claims.get("userId").toString());
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }


    // ✅ Add new URL
    public UrlEntity addUrl(String token, Map<String, Object> map) {
        Long userId = extractUserId(token);
        UrlEntity url = new UrlEntity();

        url.setUrlName((String) map.get("urlName"));
        url.setUrlDescription((String) map.get("urlDescription"));
        url.setUrlCategory((String) map.get("urlCategory"));
        url.setUrlLink((String) map.get("urlLink"));
        url.setUserId(userId);
        url.setCreatedAt(LocalDateTime.now());
        url.setUpdatedAt(LocalDateTime.now());

        return urlRepository.save(url);
    }

    // ✅ Get all URLs for user
    public List<UrlEntity> getAllUrlsByUser(String token) {
        Long userId = extractUserId(token);
        return urlRepository.findByUserId(userId);
    }

    // ✅ Get specific URL by ID (if it belongs to user)
    public UrlEntity getUrlByIdForUser(String token, Long id) {
        Long userId = extractUserId(token);
        UrlEntity url = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        if (!url.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return url;
    }

    // ✅ Update URL (if it belongs to user)
    public UrlEntity updateUrl(String token, Long id, Map<String, Object> map) {
        Long userId = extractUserId(token);
        UrlEntity existingUrl = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        if (!existingUrl.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        existingUrl.setUrlName((String) map.get("urlName"));
        existingUrl.setUrlDescription((String) map.get("urlDescription"));
        existingUrl.setUrlCategory((String) map.get("urlCategory"));
        existingUrl.setUrlLink((String) map.get("urlLink"));
        existingUrl.setUpdatedAt(LocalDateTime.now());

        return urlRepository.save(existingUrl);
    }

    // ✅ Delete URL (if it belongs to user)
    public String deleteUrl(String token, Long id) {
        Long userId = extractUserId(token);
        UrlEntity url = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        if (!url.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        urlRepository.delete(url);
        return "URL deleted successfully";
    }
}











