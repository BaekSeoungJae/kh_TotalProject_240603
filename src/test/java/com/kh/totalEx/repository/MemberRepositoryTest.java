package com.kh.totalEx.repository;

import com.kh.totalEx.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 테스트")
    public void createMemberTest() {
        for(int i = 1; i <= 10; i++) {
            Member member = new Member();
            member.setName("테스트" + i);
            member.setPwd("1234" + i);
            member.setEmail("00bsj"+ i +"@naver.com");
            member.setImage("url" + i);
            member.setRegDate(LocalDateTime.now());
            memberRepository.save(member);
        }
    }

    @Test
    @DisplayName("개별 회원 정보 조회")
    public void findByEmail() {
        this.createMemberTest();
        Optional<Member> memberList = memberRepository.findByEmail("00bsj1@naver.com");
        if(memberList.isPresent()) {
            Member member1 = memberList.get();
            System.out.println("결과 : " + member1.getEmail());
        }
    }

    @Test
    @DisplayName("로그인 체크")
    public void findByEmailAndPwd() {
        this.createMemberTest();
        List<Member> members = memberRepository.findByEmailAndPwd("00bsj2@naver.com", "12342");
        if(!members.isEmpty()) System.out.println("등록된 회원 입니다.");
        else System.out.println("회원 정보가 없습니다.");
    }

    @Test
    @DisplayName("이메일로 가입 여부 확인")
    public void existsByEmail() {
        this.createMemberTest();
        boolean memberList = memberRepository.existsByEmail("00bsj1@naver.com");
        if(memberList) System.out.println("등록된 이메일 입니다.");
        else System.out.println("등록되지 않은 이메일 입니다.");
    }
}