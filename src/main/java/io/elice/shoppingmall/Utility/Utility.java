package io.elice.shoppingmall.Utility;


import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class Utility {
    public final String SECRET_KEY = "sdagxcs56d4gxc65g48asfafssg4xc685sasadg84a46asd46w8e4684sadggsdg654xc";
    public final String JWT_COOKIE_NAME = "jwtToken";

    public final long EXPIRE_TIME_MS = 1000*60*60;

    public final int JWT_COOKIE_MAX_AGE = 60 * 60 * 24;
}
