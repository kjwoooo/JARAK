package io.elice.shoppingmall.security.oautho;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.Membership;
import io.elice.shoppingmall.member.entity.LoginInfo;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.repository.LoginInfoRepository;
import io.elice.shoppingmall.member.repository.MemberRepository;
import io.elice.shoppingmall.member.service.LoginInfoService;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.security.PrincipalDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final LoginInfoService loginInfoService;
    private final PasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");

        LoginInfo loginInfo;

        try{
            loginInfo = loginInfoService.findByProviderId(providerId);
            Member member = memberService.findByLoginInfo(loginInfo);
            return new PrincipalDetails(member, oAuth2User.getAttributes());
        } catch(CustomException e){
            loginInfo = new LoginInfo();

            loginInfo.setEmail(oAuth2User.getAttribute("email"));
            loginInfo.setProvider(provider);
            loginInfo.setProviderId(providerId);

            Member member = new Member();
            member.setDisplayName(oAuth2User.getAttribute("name"));
            member.setUsername(provider + "_" + providerId);
            member.setAuthority(MemberAuthority.USER.name());
            member.setMembership(Membership.BRONZE.name());
            member.setLoginInfo(loginInfo);

            loginInfoService.save(loginInfo);
            memberService.save(member);
        }

        return super.loadUser(userRequest);
    }
}
