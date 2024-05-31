package io.elice.shoppingmall.member;

public enum MemberAuthority {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;
    private MemberAuthority(String name){
        this.name = name;
    }
}
