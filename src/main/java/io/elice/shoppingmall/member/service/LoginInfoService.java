package io.elice.shoppingmall.member.service;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.LoginInfo;
import io.elice.shoppingmall.member.repository.LoginInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginInfoService {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoder encoder;

    public LoginInfo findByProviderId(String providerId){
        return loginInfoRepository.findByProviderId(providerId).orElseThrow(()->
            new CustomException(ErrorCode.NOT_FOUND_PROVIDER_ID));
    }

    public LoginInfo save(LoginInfo loginInfo){
        return loginInfoRepository.save(loginInfo);
    }

    public void existsEmail(String email){
        if(loginInfoRepository.existsByEmail(email))
            throw new CustomException(ErrorCode.EXIST_EMAIL);
    }

    public void matchPassword(LoginInfo loginInfo, String password){
        if(!encoder.matches(password, loginInfo.getPassword()))
            throw new CustomException(ErrorCode.MEMBER_PASSWROD_WRONG);
    }
}
