package jpastudy.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // Member와 N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)      //FetchType.LAZY : 지연로딩 //FetchType.EAGER : 즉시로딩 (끝이 One인 것의 Default / 실전에서 JPQL에서 N+1 문제가 생길 수 있다. 가급적이면 절대 쓰지 않도록.)
    @JoinColumn(name = "member_id")   //FK 설정 = 주인 설정 _ FK 있는 쪽이 주인(오너) _ 주인 쪽만 FK 관리(등록, 수정)
    private Member member;

    // Delivery와 1:1 관계
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")   //FK
    private Delivery delivery;

    // OrderItem과 1:N 관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 날짜&시간
    private LocalDateTime orderDate;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 연관관계 메서드
    // Order & Member
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    // Order & Delivery
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    // Order & OrderItem
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //== 비즈니스 로직 : 주문생성 메서드==//
    public static Order createOrder (Member member, Delivery delivery,OrderItem... orderItems) {
        Order order = new Order();
        // Order & Member 연결
        order.setMember(member);
        // Order & Delivery 연결
        order.setDelivery(delivery);

        // Order & OrderItem 연결
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        // 주문 상태
        order.setStatus(OrderStatus.ORDER);
        // 주문 날짜
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직 : 주문 취소 ==//
    public void cancel() {
        // 배송 상태가 완료되었다면 주문취소 처리가 안되도록
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        // 주문 상태 : 취소
        this.setStatus(OrderStatus.CANCEL);
        // 주문 취소 -> 재고수량 증가
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==비즈니스 로직 : 전체 주문 가격 조회 ==//
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
