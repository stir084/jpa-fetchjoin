package com.example.jpafetchjoin.repository;

import com.example.jpafetchjoin.domain.Member;
import com.example.jpafetchjoin.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public List<Member> findAllFetchJoin() {
        return em.createQuery("select m from Member m join m.orders o where o.item ='apple'", Member.class) //join fetch Member가 아니다.
                .getResultList();
    }
    //https://stackoverflow.com/questions/5816417/how-to-properly-express-jpql-join-fetch-with-where-clause-as-jpa-2-criteriaq/5819631#5819631
    //이거 왜 컬렉션에 조건 걸었는데 apple banana orange 다 나와??
    //select member0_.member_id as member_i1_0_, member0_.name as name2_0_
    // from Member member0_ inner join orders orders1_
    // on member0_.member_id=orders1_.member_id
    // where orders1_.item='apple'
    // inner join만
    //지연로딩으로 인해 Member부터 가져오고 Order가 필요할 떄 lazy loading이 발생해서 3개가 다 나오는 듯 하다.
    //JPQL에서 콜렉션 조인은 모든 데이터를 다 가져오나?

    //select member0_.member_id as member_i1_0_, member0_.name as name2_0_
    // from Member member0_ inner join orders orders1_
    // on member0_.member_id=orders1_.member_id

    //JPQL과 Fetch join 다시 강의 듣기..
    https://www.inflearn.com/questions/15876
    https://loosie.tistory.com/750
    https://stackoverflow.com/questions/5816417/how-to-properly-express-jpql-join-fetch-with-where-clause-as-jpa-2-criteriaq/5819631#5819631
    http://java-persistence-performance.blogspot.com/2012/04/objects-vs-data-and-filtering-join.html

    //일반 SQL쿼리는 on절이 필요하지만 JPQL은 ON절 없이도 on이 자동으로 묵시적 조인이 발생한다.
    //묵시적 조인은 그냥 다른 객체 갖다 쓸 때 조인 발생하는거고
    //"또한 관계는 매핑에서 관계의 조인 열에 정의된 외래 키에 의해 항상 조인되기 때문에 JPQL에는 ON 절이 없습니다." http://java-persistence-performance.blogspot.com/2012/04/objects-vs-data-and-filtering-join.html
    //ON이없는 이유는 위와 같다.

   /* Select e from Employee e
    join e.phones p
    where p.areaCode = '613'*/
    //  return em.createQuery("select m from Member m fetch join m.orders o where m.id = 2 and o.id >= 5", Member.class) //join fetch Member가 아니다.
}