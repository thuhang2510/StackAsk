package com.hang.stackask.service.implement;

import com.hang.stackask.data.VerificationTokenData;
import com.hang.stackask.entity.VerificationToken;
import com.hang.stackask.exception.VerificationTokenNotFoundException;
import com.hang.stackask.repository.VerificationTokenRepository;
import com.hang.stackask.service.interfaces.IVerificationTokenService;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenServiceImp implements IVerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    public String create(Long userId){
        String token = RandomString.make(55);

        VerificationToken verificationToken = new VerificationToken(token, userId);
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public VerificationTokenData getByToken(String token){
        VerificationToken verificationToken = verificationTokenRepository.getVerificationTokenByToken(token);

        if (verificationToken == null)
            throw new VerificationTokenNotFoundException("verification token not found");

        VerificationTokenData verificationTokenData = modelMapper.map(verificationToken, VerificationTokenData.class);
        return verificationTokenData;
    }
}
