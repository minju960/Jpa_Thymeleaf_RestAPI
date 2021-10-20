package jpastudy.jpashop.domain;

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

    //==비지니스 로직 : 주문상품 생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        // OrderItem & Item 연결
        orderItem.setItem(item);
        // 주문한 상품 가격 저장
        orderItem.setOrderPrice(orderPrice);
        // 주문한 상품 수량 저장
        orderItem.setCount(count);
        // 상품의 재고수량 감소
        item.removeStock(count);
        return orderItem;
    }
    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        // 주문이 취소되면 재고수량 증가
        getItem().addStock(this.count);
    }
    //==비즈니스 로직 : 주문상품 전체 가격 조회 ==//
    public int getTotalPrice() {
        // 주문상품 가격 * 수량
        return getOrderPrice() * getCount();
    }
}
