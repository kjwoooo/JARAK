package io.elice.shoppingmall.security;

import io.elice.shoppingmall.member.entity.Member;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;

    private Map<String, Object> attribute;

    public PrincipalDetails(Member member){
        this.member = member;
    }

    public PrincipalDetails(Member member, Map<String, Object> attribute){
        this.member = member;
        this.attribute = attribute;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() ->
            member.getAothority()
        );

        return collection;
    }

    @Override
    public String getPassword() {
        return member.getLoginInfo().getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
