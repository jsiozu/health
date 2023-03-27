package com.dale.health.service;

import com.dale.health.pojo.Member;

import java.text.ParseException;
import java.util.List;

public interface MemberService {

    public Member findByTelephone(String telephone);

    public void add(Member member);

    public List<Integer> findMemberCountByMonths(List<String> months) throws ParseException;

}
