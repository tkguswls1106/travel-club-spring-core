package io.namoosori.travelclub.spring.service.logic;

import io.namoosori.travelclub.spring.aggregate.club.CommunityMember;
import io.namoosori.travelclub.spring.aggregate.club.TravelClub;
import io.namoosori.travelclub.spring.service.MemberService;
import io.namoosori.travelclub.spring.service.sdo.MemberCdo;
import io.namoosori.travelclub.spring.shared.NameValueList;
import io.namoosori.travelclub.spring.store.MemberStore;
import io.namoosori.travelclub.spring.util.exception.MemberDuplicationException;
import io.namoosori.travelclub.spring.util.exception.NoSuchClubException;
import io.namoosori.travelclub.spring.util.exception.NoSuchMemberException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceLogic implements MemberService {  // 로직들은 거의 ClubServiceLogic과 유사하므로 참고하자.

    private MemberStore memberStore;
    // 아마 @Autowired 써도 되는듯 하다?
    public MemberServiceLogic(MemberStore memberStore) {
        this.memberStore = memberStore;  // 등호(=) 왼쪽의 this.memberStore은 private MemberStore memberStore;의 memberStore 이고, 등호(=) 오른쪽의 memberStore은 public MemberServiceLogic(MemberStore memberStore)의 매개변수인 memberStore 이다.
    }

    @Override
    public String registerMember(MemberCdo member) {  // ClubServiceLogic의 registerClub 메소드에서는 단순하게 Club을 등록하는 로직으로 구현을 했지만,
                                                      // 여기 MemberServiceLogic의 registerMember 메소드에서는 위와는 좀 다르게, 동일한 email을 갖는 Member는 저장되지않게하며 등록하는 로직으로 구현해보려고한다.
        String email = member.getEmail();
        CommunityMember foundedMember = memberStore.retrieveByEmail(email);  // 아마 CommunityMember foundedMember = memberStore.retrieveByEmail(member.getEmail()); 과 같을것이다.

        if(foundedMember != null) {  // 만약 동일한 email을 갖는 멤버가 이미 데이터베이스에 존재한다면
            throw new MemberDuplicationException("Member already exists with email : " + email);
        }
        // 여기 밑부분은 else 부분이다.
        foundedMember = new CommunityMember(member.getEmail(), member.getName(), member.getPhoneNumber());
        foundedMember.setNickName(member.getNickName());
        foundedMember.setBirthDay(member.getBirthDay());
        // CommunityMember 클래스의 CommunityMember(~) 메소드의 주석 설명을 다시 읽고오면 어떤걸 get으로 어떤걸 set으로 사용하여 저장해야하는지 알수있을것이다.

        foundedMember.checkValidation();

        return memberStore.create(foundedMember);
        /*
        아마 위의 return문처럼 적던가, 아니면 강의처럼
        memberStore.create(foundedMember);
        return foundedMember.getId();
        이렇게 적던가 아마도 상관없을듯 하다.
         */
    }

    @Override
    public CommunityMember findMemberById(String memberId) {
        return memberStore.retrieve(memberId);
    }

    @Override
    public CommunityMember findMemberByEmail(String memberEmail) {
        return memberStore.retrieveByEmail(memberEmail);
    }

    @Override
    public List<CommunityMember> findMembersByName(String name) {
        return memberStore.retrieveByName(name);
    }

    @Override
    public void modifyMember(String memberId, NameValueList nameValueList) {  // 수정하는 용도이다. modifyMember 메소드의 매개변수의 첫번째인자: 변경하고싶은 Member의 id값, 두번째인자: 변경할 내용값
        CommunityMember targetMember = memberStore.retrieve(memberId);  // id값으로 변경하고자하는 Member객체를 찾아, targetMember 이라는 새로운 Member객체에 할당시킴.
        if(targetMember == null) {  // 만약 위의 코드에서 id값으로 검색한 Member객체가 존재하지않을경우, 예외처리를  발생시켜주고자함.
            throw new NoSuchMemberException("No such member with id : " + memberId);
        }
        // 여기 밑부분이 else문 부분임
        targetMember.modifyValues(nameValueList);  // CommunityMember 클래스 내의 modifyValues라는 객체 필드 내부값 수정 메소드를 호출하여, 데이터베이스가 아닌 먼저 객체의 정보를 수정해준다.
        memberStore.update(targetMember);  // 값이변경된 Member객체인 targetMember을 매개변수로 보내줌으로써, 추후에 스토어의 Map에 있는 데이터도 변경되게끔 해준다.
    }

    @Override
    public void removeMember(String memberId) {
        if(!memberStore.exists(memberId)) {  // 삭제하기전, 먼저 데이터베이스에 해당 id가 존재하는지(=해당 id를 가진 Member객체 데이터가 존재하는지) 확인을 해주어야한다.
            // 앞에 느낌표!가 붙어있으므로, 존재한다는 True가 반환되면 False가 되므로, 즉, if문은 '해당 데이터가 존재하지않는다면' 이라는 조건문이 된다.
            throw new NoSuchMemberException("No such member with id : " + memberId);
        }
        // 여기 밑부분이 else문 부분임
        memberStore.delete(memberId);
    }
}
