package io.elice.shoppingmall.entityTest;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.entity.MemberRegister;
import io.elice.shoppingmall.member.UserMembership;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MemberTest {


    @Test
    public void MemberToMemberDtoTest(){
        Member member = new Member(1L, "displayName1", "username1", "email", "password", "010-1234-5678", 1, MemberAuthority.ADMIN.name(), UserMembership.BRONZE.name());
        MemberRegister userDto = new MemberRegister(member);

        assertThat(member.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(member.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(member.getDisplayName()).isEqualTo(userDto.getDisplayName());
        assertThat(member.getPhone()).isEqualTo(userDto.getPhone());
        assertThat(member.getGender()).isEqualTo(userDto.getGender());
        assertThat(member.getAdmin()).isEqualTo(userDto.getAdmin().name());
    }

    @Test
    public void MemberDtoToMemberTest(){
        MemberRegister userDto = new MemberRegister("displayName1", "username1", "email", "password", "010-1234-5678", 1, MemberAuthority.ADMIN);
        Member member = userDto.toUserEntity();

        assertThat(member.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(member.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(member.getDisplayName()).isEqualTo(userDto.getDisplayName());
        assertThat(member.getPhone()).isEqualTo(userDto.getPhone());
        assertThat(member.getGender()).isEqualTo(userDto.getGender());
        assertThat(member.getAdmin()).isEqualTo(userDto.getAdmin().name());
    }
}
