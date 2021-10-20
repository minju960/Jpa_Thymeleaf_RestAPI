package jpastudy.jpashop.domain;

import jpastudy.jpashop.repository.MemberRepository;
import jpastudy.jpashop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("boot");
        //When
        Long saveId = memberService.join(member);
        //Then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복회원_예외() throws Exception {
        Member member1 = new Member();
        member1.setName("boot");

        Member member2 = new Member();
        member2.setName("boot");

        // Exception 검증
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });

        // 예외 메세지 비교
        assertEquals("이미 존재하는 회원입니다.", exception.getMessage());
    }


}
