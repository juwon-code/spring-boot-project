package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.UserEntity;
import juwoncode.commonblogproject.vo.SocialPlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsMemberByUsername(String username);

    boolean existsMemberByEmail(String email);

    boolean existsMemberByUsernameAndEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndActivated(String username, boolean activated);

    Long deleteByUsernameAndPassword(String username, String password);
}
