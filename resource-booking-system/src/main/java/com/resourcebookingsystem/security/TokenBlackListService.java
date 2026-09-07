package com.resourcebookingsystem.security;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlackListService {
    // Thread-safe map holding blacklisted tokens and their expiration dates
    private final Map<String, Date> blacklist =new ConcurrentHashMap<>();

    public void blacklistToken(String token, Date expirationDate){
        blacklist.put(token,expirationDate);
    }
    public boolean isBlacklisted(String token){
        Date expirationDate=blacklist.get(token);
        if(expirationDate == null){
            return false;
        }
        // Auto-purge if the token has naturally expired
        if(expirationDate.before(new Date())){
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
