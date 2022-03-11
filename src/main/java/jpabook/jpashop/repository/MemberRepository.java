package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
//@RequiredArgsConstructor
public class MemberRepository {

    /* 스프링 부트가 생성한 엔티티 매니저를 주입받음 */
    @PersistenceContext
    private EntityManager em;

    //스프링 데이터 JPA 를 사용하여 생성자 주입 방식으로 받을 수도 있다.
    //왜냐하면 스프링 데이터 JPA 에서 @PersistenceContext 말고 @Autowired 어노테이션으로도 받을 수 있기 해놨기 때문임
    //그래서 생성자 주입이 가능해진 것임 (MemberService 주석 확인할 것)
    //아예 생성자도 안 적고 @RequiredArgsConstructor 로도 퉁칠 수 있다.
    //private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    /* 한 건 검색 */
    public Member findOne(Long id) {
        return em.find(Member.class, id);   //(타입, PK) 입력
    }

    /* 전체 검색 */
    public List<Member> findAll() {
        //jpql은 from 의 대상이 테이블이 아니라, 엔티티가 된다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /* 이름으로 검색 */
    public List<Member> findByName(String name) {
        //파라미터 바인딩 사용
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

    }

}
