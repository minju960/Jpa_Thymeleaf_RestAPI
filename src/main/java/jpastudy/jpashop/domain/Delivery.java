package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    // Order와 1:1 관계
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // 배달 상태
    @Enumerated(EnumType.STRING)        //문자의 형태로 저장되게 하는 것 -> 직관적
    private DeliveryStatus status;
}
