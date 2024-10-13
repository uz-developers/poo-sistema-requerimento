package com.exemplo.util;

import java.util.HashMap;

public class Mapper {
    public static HashMap<String, String> toJsonUser(String name, String nickname, String email, String username, String passwd) {
        HashMap<String, String> user = new HashMap<>();
        user.put("name", name);
        user.put("nickname", nickname);
        user.put("email", email);
        user.put("passwd", passwd);
        user.put("username", username);
        return user;
    }
    
}
