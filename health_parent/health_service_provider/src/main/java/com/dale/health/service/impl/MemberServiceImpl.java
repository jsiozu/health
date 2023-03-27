package com.dale.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dale.health.dao.MemberDao;
import com.dale.health.pojo.Member;
import com.dale.health.service.MemberService;
import com.dale.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            String s = MD5Utils.md5(password);
            member.setPassword(s);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) throws ParseException {
        List<Integer> l = new ArrayList<>();
        for (String month : months) {
            String date_str = month + "-10";
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(date_str));
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            // 获取当月最后一天
            String lastDayOfMonth = sdf.format(calendar.getTime());
            Integer memberCountBeforeDate = memberDao.findMemberCountBeforeDate(lastDayOfMonth);
            l.add(memberCountBeforeDate);
        }
        return l;
    }

}
