package io.elice.shoppingmall.member;

public enum UserMembership {
    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD");

    private final String name;
    private UserMembership(String name){
        this.name = name;
    }
}
