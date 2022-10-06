package io.namoosori.travelclub.spring.store.mapstore;

import io.namoosori.travelclub.spring.aggregate.club.CommunityMember;
import io.namoosori.travelclub.spring.store.MemberStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberMapStore implements MemberStore {

    @Override
    public String create(CommunityMember member) {
        return null;
    }

    @Override
    public CommunityMember retrieve(String memberId) {
        return null;
    }

    @Override
    public CommunityMember retrieveByEmail(String email) {
        return null;
    }

    @Override
    public List<CommunityMember> retrieveByName(String name) {
        return null;
    }

    @Override
    public void update(CommunityMember member) {

    }

    @Override
    public void delete(String email) {

    }

    @Override
    public boolean exists(String memberId) {
        return false;
    }
}
