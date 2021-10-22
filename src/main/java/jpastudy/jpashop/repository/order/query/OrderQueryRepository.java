package jpastudy.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * 1:N 관계(컬렉션)를 제외한 Order, Member, Delivery를 한번에 조회
     * : ToOne 관계인 Member, Delivery 만 조회
     * : OrderItem은 조회하지 않음
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpastudy.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    } //findOrders

    /**
     * 1:N 관계인 orderItems 조회
     * Order, OrderItem, Item 조회
     * : ToMany 관계인 OrderItem과 Item을 조회해서 DTO 저장
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpastudy.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    } //findOrderItems

    /**
     * OrderQueryDto 가 참조하는 OrderItemQueryDto를 채워넣기
     * @return
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        //ToOne 관계인 객체를 한번에 조회
        List<OrderQueryDto> orderQueryDtos = findOrders();
        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        //루프를 돌면서 ToMany 관계인 객체를 저장
        orderQueryDtos.forEach(o -> {
            List<OrderItemQueryDto> orderItemQueryDtos = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItemQueryDtos);
        });
        return orderQueryDtos;
    } //findOrderQueryDtos
}//class


