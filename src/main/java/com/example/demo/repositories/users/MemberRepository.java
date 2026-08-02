package com.example.demo.repositories.users;

import com.example.demo.models.users.Member;
import com.example.demo.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUser(User user);
}