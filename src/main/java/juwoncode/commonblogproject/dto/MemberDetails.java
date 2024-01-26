package juwoncode.commonblogproject.dto;

import juwoncode.commonblogproject.vo.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
public class MemberDetails implements UserDetails {
    private String username;
    private String password;
    private String email;
    private RoleType role;
    private boolean enabled;

    /**
     * 회원 권한을 담고 있는 List 인스턴스를 반환한다.
     * @return
     *      회원 권한 List 인스턴스.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getRole()));
        return authorities;
    }

    /**
     * 회원명을 반환한다.
     * @return
     *      회원명.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 회원의 비밀번호를 반환한다.
     * @return
     *      비밀번호.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 회원의 만료 여부를 반환한다. 만료된 회원은 사용할 수 없다.
     * @return
     *      회원이 만료되었다면 true, 아니라면 false를 반환한다.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 회원의 잠김 여부를 반환한다. 잠겨진 회원은 사용할 수 없다.
     * @return
     *      회원이 잠겨있다면 true, 아니라면 false를 반환한다.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 회원의 비밀번호 만료 여부를 반환한다. 만료된 회원은 비밀번호를 변경해야 한다.
     * @return
     *      비밀번호가 만료되었다면 true, 아니라면 false를 반환한다.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 회원의 활성화 여부를 반환한다. 비활성화된 회원은 사용할 수 없다.
     * @return
     *      활성화된 회원은 true, 아니라면 false를 반환한다.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
