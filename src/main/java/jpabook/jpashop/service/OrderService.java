package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     * */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회(Member, Item)
        Member member = memberRepository.findOne(memberId);
        Item item= itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
      //  delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        //createOrderItem()을 이용하라고 하기 위해 OrderItem 에 @NoArgsConstructor를 달아두었다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        //Order 객체에서 orderItems, delivery 는 cascade 옵션으로 달려있다. 
        //그래서 order 만 persist() 되어도, 저 둘은 자동으로 같이 persist() 됨.
        //Order 만 orderItems, delivery 를 사용하고 persist() 라이프사이클도 같으므로, cascade 의 범위가 될 수 있다고 본다.
        
        return order.getId();
    }

    /** 
     * 주문 취소
     * */
    @Transactional
    public void cancelOrder(Long orderId) {

        //주문 엔터티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();

        /* 도메인 모델 패턴 (JPA, ORM)
        * 주문 서비스의 주문과 주문 취소 메서드를 보면 비즈니스 로직 대부분이 엔티티에 있다. 
        * 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.
        * 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 도메인 모델 패턴이다.
        * 유지보수가 용이하다.
        * */

        /* 트랜잭션 스크립트 패턴 (기존)
        * 반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분 의 비즈니스 로직을 처리하는 것
        * 문맥에 맞는 걸 사용하면 되고, 한 프로젝트내에서도 두 패턴이 양립해서 존재하기도 한다.
        * */
    }
    
    /**
     * 주문 검색
     * */
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
