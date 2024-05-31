package io.elice.shoppingmall.security;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[Log] PrincipalDetailsService.loadUsername 실행");
        Member member = memberRepository.findByUsername(username).orElseThrow(()->{
            return new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        });

        return new PrincipalDetails(member);
    }
}
