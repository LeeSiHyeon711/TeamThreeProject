package org.lsh.teamthreeproject.repository;

import org.lsh.teamthreeproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.nickname = :nickname")
    Optional<User> findByNickname(@Param("nickname") String nickname);
    Optional<User> findByLoginId(String loginId);
    @Query("SELECT u FROM User u WHERE u.nickname like %:nickname%")
    List<User> findByNicknameContaining(String nickname);

}
