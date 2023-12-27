package juwoncode.commonblogproject.repository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberRepositoryTests {
    @Autowired
    MemberRepository memberRepository;
}
