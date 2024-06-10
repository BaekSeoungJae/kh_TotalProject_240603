package com.kh.totalEx.repository;

import com.kh.totalEx.constant.ItemSellStatus;
import com.kh.totalEx.entity.Item;
import com.kh.totalEx.entity.Member;
import com.kh.totalEx.entity.Order;
import com.kh.totalEx.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Slf4j
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상품에 대한 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return item;
    }
    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();
        for(int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItemList().add(orderItem);
        }
            orderRepository.saveAndFlush(order);

            em.clear();

            // 주문 조회
            Order saveOrder = orderRepository.findById(order.getId()).orElseThrow(EntityExistsException::new);
            log.info(String.valueOf(saveOrder.getOrderItemList().size()));
        }

    public Order createOrder(){
        Order order = new Order();

        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItemList().add(orderItem);
        }

        Member member = new Member();
        member.setName("백승재");
        member.setEmail("00bsj@naver.com");
        member.setRegDate(LocalDateTime.now());
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }
    @Test
    @DisplayName("고아객체 제거 테스트") // 테스트케이스 이름 설정
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItemList().remove(0);
        em.flush(); // 즉시 데이터베이스에 반영
        log.info(String.valueOf(order.getOrderItemList().size()));
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItemList().get(0).getId();
        em.flush();
        em.clear();
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        log.info("Order class : " + orderItem.getOrder().getClass());
    }
}
