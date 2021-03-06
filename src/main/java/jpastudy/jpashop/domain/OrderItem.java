package jpastudy.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpastudy.jpashop.domain.item.Item;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    // Order와 N:1 관계
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")      //FK
    private Order order;

    // 주문 가격
    private int orderPrice;

    // 주문 수량
    private int count;

    // Item과 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")       //FK
    private Item item;

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count);
        return orderItem;
    }
    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        getItem().addStock(count);
    }
    //==비즈니스 로직 : 주문상품 전체 가격 조회 ==//
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
