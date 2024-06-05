package com.kh.totalEx.repository;

import com.kh.totalEx.entity.Cart;
import com.kh.totalEx.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
@Transactional // 데이터베이스의 논리적인 작업 단위
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {
    @Autowired // 스프링 컨테이너에서 해당 빈에 해당하는 의존성을 주입 받음
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em; // JPA 의 EntityManager 을 사용하겠다는 의미 (의존성 주입을 받음)

    // 회원 엔티티 생성
    public Member createMemberInfo() {
        Member member = new Member();
        member.setEmail("00bsj@naver.com");
        member.setPwd("1234");
        member.setName("고양이사육사");
        member.setRegDate(LocalDateTime.now());
        return member;
    }

    @Test
    @DisplayName("장바구니와 회원정보 매핑 테스트")
    public void findCartAndMemberTest() {
        Member member = createMemberInfo();
        memberRepository.save(member);
        Cart cart = new Cart();
        cart.setCartName("사줘잉");
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); // 데이터베이스에 강제 반영
        em.clear(); // 영속성 켄텍스트를 비움

//        Cart saveCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
//        log.info(saveCart.getMember().getEmail());
        Optional<Cart> saveCart = cartRepository.findById(cart.getId());
        if(saveCart.isPresent()) {
            Cart testCart = saveCart.get();
            log.info(testCart.getMember().getEmail());
        }
    }
}