package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    private int orderPrice;     //주문가격
    private int count;          //주문수량

    //@NoArgsConstructor 이하 내용과 같은 내용이다. 
    // 직접 Setter 를 사용하지 말라고 경고할 수 있음. JPA 에서 protected~ 는 쓰지 말라는 거임
    //protected OrderItem() {}      

    /* 생성 메서드 */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);            //할인될 수 도 있으니, item.getPrice()를 하지 않는 것임.
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    /* 비즈니스 로직 */
    public void cancel() {
        getItem().addStock(count);      //재고수량 원복
    }

    /* 조회 로직 */
    /**
     * 주문상품 전체 가격 조회
     * */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
