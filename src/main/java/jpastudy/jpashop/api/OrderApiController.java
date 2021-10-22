package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * toMany 관계 성능 최적화
 * Order -> OrderItem -> Item
 */

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;


    /**
     * 엔티티를 API에 직접 노출하는 방식
     * N + 1 문제 발생
     * Order 1번
     * Member, Delivery N번 (Order Row 수 만큼)
     * OrderItem N번 (Order Row 수 만큼)
     * Item N번 (Order Row 수 만큼)
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName()); //Lazy 강제초기화
        }
        return all;
    }

    /**
     * 엔티티를 DTO로 변환해서 노출하는 방식
     * N + 1 문제 발생
     *
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream()     //Stream<Order>
                .map(order -> new OrderDto(order))      //Stream<OrderDto>
                .collect(Collectors.toList());      //List<OrderDto>
    }

    /**
     * 엔티티를 DTO로 변환해서 노출, Fetch Join을 사용해서 성능 최적화
     * 문제점 : toMany 의존관계 객체들의 페이징 처리가 안된다
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 엔티티를 DTO로 변환해서 노출, Fetch Join을 사용해서 성능 최적화
     * ToMany 관계인 엔티티를 가져올 때 페이징 처리 안되는 문제를 해결하기 위해
     * ToOne 관계인 엔티티는 Fetch Join으로 가져오고
     * ToMany 관계인 엔티티는 Hibernate.default_batch_fetch_size 설정하기
     */
    @GetMapping("/api/v3.2/orders")
    public List<OrderDto> ordersV3_Paging(@RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "limit", defaultValue = "1") int limit) {
        List<Order> orderList = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orderList.stream()      //Stream<Order>
                .map(order -> new OrderDto(order))      //Stream<OrderDto>
                .collect(Collectors.toList());       //List<OrderDto
    }

    //응답 요청에 사용할 DTO Inner Class 선언
    @Data
    static class OrderItemDto {
        private String itemName; //상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();       // Lazy Loading 초기화
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    } //static class OrderItemDto

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();     // Lazy Loading 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();     // Lazy Loading 초기화

            orderItems = order.getOrderItems()      // Lazy Loading 초기화
                    .stream()     //Stream<OrderItem>
                    .map(orderItem -> new OrderItemDto(orderItem))      //Stream<OrderItemDto>
                    .collect(toList());      //List<OrderItemDto>
        }
    } //static class OrderDto

}
