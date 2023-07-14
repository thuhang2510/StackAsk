package com.hang.stackask.utils;

import com.hang.stackask.data.UserData;
import com.hang.stackask.service.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtFilterUtil {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserService iUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilterUtil.class);

    public UserData getUserFromJwt(String bearerToken){
        UserData result = null;

        try {
            String jwt = getInfoAfterStringBearer(bearerToken);

            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {
                Long userId = jwtUtil.getUserIdFromJWT(jwt);
                result = iUserService.getById(userId);
            }
        } catch (Exception ex) {
            LOGGER.error("failed", ex);
        }

        return result;
    }

    private String getInfoAfterStringBearer(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
