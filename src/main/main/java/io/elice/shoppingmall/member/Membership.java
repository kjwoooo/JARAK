package io.elice.shoppingmall.member;

public enum Membership {
    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD");

    private final String name;
    private Membership(String name){
        this.name = name;
    }
}
