package service;

import model.Event;
import model.Member;

/**
 * Created by mihkel on 11.04.2018.
 */
public interface MemberService {
    Member add(Event event, Member newMember);
    Member save(Member member, Member updatedMember);
    Member remove(Member member);
    void loadMembers(Event event);
    void calculateMembersMoney(Event event);
}
