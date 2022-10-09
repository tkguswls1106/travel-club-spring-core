package io.namoosori.travelclub.spring.store.mapstore;

import io.namoosori.travelclub.spring.aggregate.club.CommunityMember;
import io.namoosori.travelclub.spring.store.MemberStore;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberMapStore implements MemberStore {  // 로직들은 거의 ClubMapStore과 유사하므로 참고하자.

    private Map<String, CommunityMember> memberMap;

    public MemberMapStore() {
        this.memberMap = new LinkedHashMap<>();
    }

    @Override
    public String create(CommunityMember member) {  // 새로운 멤버 저장
        memberMap.put(member.getId(), member);
        return member.getId();
    }

    @Override
    public CommunityMember retrieve(String memberId) {  // id값으로 멤버 검색
        return memberMap.get(memberId);
    }  // 주의사항: 이 메소드는 memberMap의 키값중 키인 id로 검색하기때문에 람다스트림이나 for문같은 반복문 없이 사용 가능하지만,
       // 만약 memberMap의 키값중 값인 CommunityMember의 필드정보로 검색해야하는 메소드라면 람다스트림이나 for문같은 반복문을 사용하여 검색해야한다.

    @Override
    public CommunityMember retrieveByEmail(String email) {  // email로 멤버 검색
        CommunityMember targetMember = null;
        for(CommunityMember member : memberMap.values()) {
            if(member.getEmail().equals(email)) {
                targetMember = member;
                break;  // 찾았다면 중복허용 어차피 안되니까 지체하지말고 break로 for문 끝내고 나와서, 찾은 멤버를 return으로 반환하자.
            }
        }
        return targetMember;  // for문 다 돌을돌안 해당하는 멤버를 하나도 못찾았다면, 그대로 null값 반환함.
    }  // 주의사항: 이 메소드는 memberMap의 키값중 값인 CommunityMember의 필드정보로 검색해야하는 메소드이므로, 람다스트림이나 for문같은 반복문을 사용해야하며,
       // 메소드 반환자료형이 CommunityMember 이므로, 중복허용을 하지않아 중복들을 리스트로 묶을필요가 없으므로, 메소드 반환자료형이 List<CommunityMember>가 아닌, CommunityMember인 것이다.
       // 만약 중복허용이라면 중복목록을 리스트로 묶어 반환해주어야하므로, 메소드 반환자료형이 CommunityMember가 아닌, List<CommunityMember> 이었을것이다.
    /*
    // < 위의 retrieveByEmail메소드의 반환값이 null이 확실하게 아닐경우, Optional없이 람다 사용 방법 >
    @Override
    public CommunityMember retrieveByEmail(String email) {
        return memberMap.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findAny();  // 필터링된 그중에서 가장 먼저 탐색된 요소를 반환함. 즉, 중복허용 안하고 찾자마자 바로 반환.
    }
     */
    /*
    // < 위의 retrieveByEmail메소드의 반환값이 null일 가능성이 있을경우, Optional 사용하고 람다 사용 방법 >
    @Override
    public Optional<CommunityMember> retrieveByEmail(String email) {  // 단, 나중에 Optional로 감싸진 반환값이 null인지 아닌지 확인하려면 ifPresent 같은걸로 서비스든 어디서든간에 확인을 해주어야한다.
        return memberMap.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findAny();  // 필터링된 그중에서 가장 먼저 탐색된 요소를 반환함. 즉, 중복허용 안하고 찾자마자 바로 반환.
    }
     */

    @Override
    public List<CommunityMember> retrieveByName(String name) {  // 이름으로 멤버 검색하고, 중복허용해서 같은이름 멤버들 전부 리스트로 묶어 반환.
        return memberMap.values().stream()
                .filter(member -> member.getName().equals(name))
                .collect(Collectors.toList());
    }  // 주의사항: 이 메소드는 memberMap의 키값중 값인 CommunityMember의 필드정보로 검색해야하는 메소드이므로, 람다스트림이나 for문같은 반복문을 사용해야하며,
       // 메소드 반환자료형이 List<CommunityMember> 이므로, 중복허용하여 중복목록을 리스트로 묶어 반환해준다.
       // 만약 중복허용이 아니라면 중복들을 리스트로 묶을필요가 없으므로, 메소드 반환자료형이 List<CommunityMember>가 아닌, CommunityMember 이었을것이다.

    @Override
    public void update(CommunityMember member) {  // 업데이트(수정) 용도인데, 참고로 매개변수 CommunityMember member 객체 정보는 업데이트 된 데이터로 넘어오는것이다.
        memberMap.put(member.getId(), member);  // Map<String, CommunityMember>
    }

    @Override
    public void delete(String memberId) {  // 멤버 삭제 용도
        memberMap.remove(memberId);
    }

    @Override
    public boolean exists(String memberId) {  // store 저장소에 해당 clubId를 가지고있는지 확인하는 용도이다.
        return Optional.ofNullable(memberMap.get(memberId)).isPresent();
        // 아마 return memberMap.containsKey(memberId); 이걸로도 사용 가능할듯 하다.
    }
}
