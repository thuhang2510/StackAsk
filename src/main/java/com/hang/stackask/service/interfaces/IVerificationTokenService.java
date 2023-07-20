package com.hang.stackask.service.interfaces;

import com.hang.stackask.data.VerificationTokenData;

public interface IVerificationTokenService {
    VerificationTokenData getByToken(String token);
    String create(Long userId);
}
