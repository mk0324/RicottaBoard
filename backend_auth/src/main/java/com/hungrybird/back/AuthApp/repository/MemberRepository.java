package com.hungrybird.back.AuthApp.repository;

import com.hungrybird.back.AuthApp.model.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

    Optional<List<Member>> findAllByChannelId(String channelId);

    Optional<Member> findByEmailAndChannelId(String email, String channelId);
}

