package io.namoosori.travelclub.spring.service.logic;

import io.namoosori.travelclub.spring.aggregate.club.CommunityMember;
import io.namoosori.travelclub.spring.service.MemberService;
import io.namoosori.travelclub.spring.service.sdo.MemberCdo;
import io.namoosori.travelclub.spring.shared.NameValueList;
import io.namoosori.travelclub.spring.store.MemberStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceLogic implements MemberService {

    private MemberStore memberStore;
    // 아마 @Autowired 써도 되는듯 하다?
    public MemberServiceLogic(MemberStore memberStore) {
        this.memberStore = memberStore;  // 등호(=) 왼쪽의 this.memberStore은 private MemberStore memberStore;의 memberStore 이고, 등호(=) 오른쪽의 memberStore은 public MemberServiceLogic(MemberStore memberStore)의 매개변수인 memberStore 이다.
    }

    @Override
    public String registerMember(MemberCdo member) {
        return null;
    }

    @Override
    public CommunityMember findMemberById(String memberId) {
        return null;
    }

    @Override
    public CommunityMember findMemberByEmail(String memberEmail) {
        return null;
    }

    @Override
    public List<CommunityMember> findMembersByName(String name) {
        return null;
    }

    @Override
    public void modifyMember(String memberId, NameValueList member) {

    }

    @Override
    public void removeMember(String memberId) {

    }
}
