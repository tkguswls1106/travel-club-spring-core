package io.namoosori.travelclub.spring.service.logic;

import io.namoosori.travelclub.spring.aggregate.club.TravelClub;
import io.namoosori.travelclub.spring.service.ClubService;
import io.namoosori.travelclub.spring.service.sdo.TravelClubCdo;
import io.namoosori.travelclub.spring.shared.NameValueList;
import io.namoosori.travelclub.spring.store.ClubStore;
import io.namoosori.travelclub.spring.store.mapstore.ClubMapStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceLogic implements ClubService {

    private ClubStore clubStore;  // ClubStore 인터페이스 타입의 필드(변수) 선언.
    // 아마 @Autowired 써도 되는듯 하다?
    public ClubServiceLogic(ClubStore clubStore) {  // ClubServiceLogic 은 ClubMapStore 을 알아야하는 관계이기때문에 이렇게 적어준다.
        this.clubStore = clubStore;
        // 아마 이렇게 적은 이유는, ClubServiceLogic과 ClubMapStore은 ClubStore 인터페이스를 사이에 두고 느슨한 결합을 유지해야하기때문에, DI에 ClubStore 인터페이스를 사용한것같다.
        // 이렇게하면, 장점은 ClubStore 인터페이스를 사이에 두고있어, ClubServiceLogic 입장에서는 ClubStore 인터페이스를 참조하고있는 객체가 ClubMapStore던지 아니면 다른 추후에 변경된 DB든지간에 알아서 DI주입이 될테니 상관없어진다는 것이다.
        // 즉, 나중에 DB 종류를 변경할때 다른 코드는 건드리지않고 그대로 써도 되기때문에 편해진다.
    }

    @Override
    public String registerClub(TravelClubCdo club) {  // Club 등록 용도이다.
        TravelClub newClub = new TravelClub(club.getName(), club.getIntro());
        newClub.checkValidation();  // 처음에 설정해둔 글자 길이 등등이 맞는지 확인하는 용도. 만약 틀리면 Club이 생성되지않게 한다.
        return clubStore.create(newClub);  // clubStore 객체는 ClubMapStore의 인스턴스이때문에 이렇게 create를 호출해서 데이터를 넘겨줘서 사용할것이다.
    }

    @Override
    public TravelClub findClubById(String id) {
        return null;
    }

    @Override
    public List<TravelClub> findClubsByName(String name) {
        return null;
    }

    @Override
    public void modify(String clubId, NameValueList nameValues) {

    }

    @Override
    public void remove(String clubId) {

    }
}
