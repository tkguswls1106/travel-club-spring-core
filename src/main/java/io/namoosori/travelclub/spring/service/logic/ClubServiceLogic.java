package io.namoosori.travelclub.spring.service.logic;

import io.namoosori.travelclub.spring.aggregate.club.TravelClub;
import io.namoosori.travelclub.spring.service.ClubService;
import io.namoosori.travelclub.spring.service.sdo.TravelClubCdo;
import io.namoosori.travelclub.spring.shared.NameValueList;
import io.namoosori.travelclub.spring.store.ClubStore;
import io.namoosori.travelclub.spring.store.mapstore.ClubMapStore;
import io.namoosori.travelclub.spring.util.exception.NoSuchClubException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceLogic implements ClubService {

    private ClubStore clubStore;  // ClubStore 인터페이스 타입의 필드(변수) 선언.
    // 아마 @Autowired 써도 되는듯 하다?
    public ClubServiceLogic(ClubStore clubStore) {  // ClubServiceLogic 은 ClubMapStore 을 알아야하는 관계이기때문에 이렇게 적어준다.
        this.clubStore = clubStore;  // 등호(=) 왼쪽의 this.clubStore은 private ClubStore clubStore;의 clubStore 이고, 등호(=) 오른쪽의 clubStore은 public ClubServiceLogic(ClubStore clubStore)의 매개변수인 clubStore 이다.
        // 아마 이렇게 적은 이유는, ClubServiceLogic과 ClubMapStore은 ClubStore 인터페이스를 사이에 두고 느슨한 결합을 유지해야하기때문에, DI에 ClubStore 인터페이스를 사용한것같다.
        // 이렇게하면, 장점은 ClubStore 인터페이스를 사이에 두고있어, ClubServiceLogic 입장에서는 ClubStore 인터페이스를 참조하고있는 객체가 ClubMapStore던지 아니면 다른 추후에 변경된 DB든지간에 알아서 DI주입이 될테니 상관없어진다는 것이다.
        // 즉, 나중에 DB 종류를 변경할때 다른 코드는 건드리지않고 그대로 써도 되기때문에 편해진다.
    }

    @Override
    public String registerClub(TravelClubCdo club) {  // Club 등록 용도이다. 서비스는 스토어의 데이터베이스 접근 메소드를 호출해주는 중간 다리 역할이라는 사실을 잊지말자.
        TravelClub newClub = new TravelClub(club.getName(), club.getIntro());
        newClub.checkValidation();  // 처음에 설정해둔 글자 길이 등등이 맞는지 확인하는 용도. 만약 틀리면 Club이 생성되지않게 한다.
        return clubStore.create(newClub);  // clubStore 객체는 ClubMapStore의 인스턴스이때문에 이렇게 create를 호출해서 데이터를 넘겨줘서 사용할것이다.  // 서비스 목적에 가장 부합한 스토어의 데이터베이스 접근 메소드 호출.
    }

    @Override
    public TravelClub findClubById(String id) {  // 파라미터로 넘어오는 id값으로 Club을 찾는 용도이다. 서비스는 스토어의 데이터베이스 접근 메소드를 호출해주는 중간 다리 역할이라는 사실을 잊지말자.
        return clubStore.retrieve(id);  // Map에 등록된 id에 해당하는 Club 객체를 반환해와서 return해줌.  // 서비스 목적에 가장 부합한 스토어의 데이터베이스 접근 메소드 호출.
    }

    @Override
    public List<TravelClub> findClubsByName(String name) {  // name값으로, 동일한 이름의 club객체들까지를 모두 찾아 리스트로 묶어 반환해줌. 서비스는 스토어의 데이터베이스 접근 메소드를 호출해주는 중간 다리 역할이라는 사실을 잊지말자.
        return clubStore.retrieveByName(name);  // 서비스 목적에 가장 부합한 스토어의 데이터베이스 접근 메소드 호출.
    }

    @Override
    public void modify(String clubId, NameValueList nameValues) {  // 수정하는 용도이다. modify 메소드의 매개변수의 첫번째인자: 변경하고싶은 Club의 id값, 두번째인자: 변경할 내용값
                                                                   // 서비스는 스토어의 데이터베이스 접근 메소드를 호출해주는 중간 다리 역할이라는 사실을 잊지말자.
        TravelClub foundedClub = clubStore.retrieve(clubId);  // id값으로 변경하고자하는 Club객체를 찾아, foundedClub 이라는 새로운 Club객체에 할당시킴.
        if(foundedClub == null) {  // 만약 위의 코드에서 id값으로 검색한 Club객체가 존재하지않을경우, 예외처리를  발생시켜주고자함.
            throw new NoSuchClubException("No such club with id : " + clubId);
        }
        // 여기 밑부분이 else문 부분임
        foundedClub.modifyValues(nameValues);  // TravelClub 클래스 내의 modifyValues라는 객체 필드 내부값 수정 메소드를 호출하여, 데이터베이스가 아닌 먼저 객체의 정보를 수정해준다.
        clubStore.update(foundedClub);  // 값이변경된 Club객체인 foundedClub을 매개변수로 보내줌으로써, 추후에 스토어의 Map에 있는 데이터도 변경되게끔 해준다.  // 서비스 목적에 가장 부합한 스토어의 데이터베이스 접근 메소드 호출.
    }

    @Override
    public void remove(String clubId) {  // 삭제하는 용도이다. 서비스는 스토어의 데이터베이스 접근 메소드를 호출해주는 중간 다리 역할이라는 사실을 잊지말자.
        if(!clubStore.exists(clubId)) {  // 삭제하기전, 먼저 데이터베이스에 해당 id가 존재하는지(=해당 id를 가진 Club객체 데이터가 존재하는지) 확인을 해주어야한다.
                                         // 앞에 느낌표!가 붙어있으므로, 존재한다는 True가 반환되면 False가 되므로, 즉, if문은 '해당 데이터가 존재하지않는다면' 이라는 조건문이 된다.
            throw new NoSuchClubException("No such club with id : " + clubId);
        }
        // 여기 밑부분이 else문 부분임
        clubStore.delete(clubId);  // 서비스 목적에 가장 부합한 스토어의 데이터베이스 접근 메소드 호출.
    }
}
