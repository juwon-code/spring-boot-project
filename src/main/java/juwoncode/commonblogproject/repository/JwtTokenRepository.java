package juwoncode.commonblogproject.repository;

import juwoncode.commonblogproject.domain.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findJwtTokenByRefreshToken(String refreshToken);

    boolean existsRefreshTokenByRefreshTokenAndExpired(String refreshToken, boolean expired);
}
