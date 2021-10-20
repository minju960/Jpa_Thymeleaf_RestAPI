package jpastudy.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded   // 포함하는 쪽
    private Address address;

    // Order와 1:N 관계
    @OneToMany(mappedBy = "member")     //member를 참조한다
    private List<Order> orders = new ArrayList<>();

}
