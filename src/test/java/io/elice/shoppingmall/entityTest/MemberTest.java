package io.elice.shoppingmall.entityTest;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.entity.MemberDTO;
import io.elice.shoppingmall.member.UserMembership;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MemberTest {


    @Test
    public void MemberToMemberDtoTest(){
        Member member = new Member(1L, "displayName1", "user1", "password", "010-1234-5678", 1, MemberAuthority.ADMIN.name(), UserMembership.BRONZE.name());
        MemberDTO userDto = new MemberDTO(member);

        assertThat(member.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(member.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(member.getDisplayName()).isEqualTo(userDto.getDisplayName());
        assertThat(member.getPhoneNumber()).isEqualTo(userDto.getPhoneNumber());
        assertThat(member.getGender()).isEqualTo(userDto.getGender());
        assertThat(member.getAdmin()).isEqualTo(userDto.getAdmin().name());
        assertThat(member.getMembership()).isEqualTo(userDto.getMembership().name());
    }

    @Test
    public void MemberDtoToMemberTest(){
        MemberDTO userDto = new MemberDTO("displayName1", "user1", "password", "010-1234-5678", 1, MemberAuthority.ADMIN, UserMembership.BRONZE);
        Member member = userDto.toEntity();

        assertThat(member.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(member.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(member.getDisplayName()).isEqualTo(userDto.getDisplayName());
        assertThat(member.getPhoneNumber()).isEqualTo(userDto.getPhoneNumber());
        assertThat(member.getGender()).isEqualTo(userDto.getGender());
        assertThat(member.getAdmin()).isEqualTo(userDto.getAdmin().name());
        assertThat(member.getMembership()).isEqualTo(userDto.getMembership().name());
    }
}
