package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              //주문회원

    /* cascade : persist()를 전파한다.
    * 따라서 em.persist(order) 를 하면, em.persist(orderItemsA)를 따로 안 해줘도 됨
    * */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;          //배송정보

    private LocalDateTime orderDate;    //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status;         //주문상태(ORDER, CANCEL)

    /* 연관관계 편의 메서드.
    * : 양방향 관계일 때 미리 셋팅해주는 것. 편리하다. */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    
    //원래대로 라면, 아래처럼 각각의 객체가 본인의 필드를 본인이 set 하고 add 해야 함. 근데 까먹을 수 있음
//    public static void main(String[] args) {
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
//    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    /* 생성 메서드
    * 실무라면 OrderItem 도 DTO 에 담아서 훨씬 복잡하게 오겠지. */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();

        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    /* 비즈니스 로직 */
    /**
     * 주문 취소
     * */
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        //이렇게만 해두면 JPA 가 알아서 dirty check 하여 update 쿼리를 날림!
        this.setStatus(OrderStatus.CANCEL);             //취소상태로 변경
        
        for(OrderItem orderItem : orderItems) {         //물건들 재고수량을 다 원상복구
            orderItem.cancel();
        }
    }

    /* 조회 로직 */
    /**
     * 전체 주문 가격 조회
     * */
    public int getTotalPrice() {
        //현재 Item 가격이 아니라 order 당시의 가격이어야 하므로,
        //OrderItem 에 getTotalPrice() 메소드를 만들어서 받아온다.
        
        //원래 형태
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;

        //stream() 사용
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

}
