package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /* cascade : persist()를 전파한다.
    * 따라서 em.persist(order) 를 하면, em.persist(orderItemsA)를 따로 안 해줘도 됨
    * */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;    //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태(ORDER, CANCEL)

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
}
