package repository;

import model.Event;
import model.Member;

/**
 * Created by mihkel on 6.04.2018.
 */
public interface MemberDao extends GeneralDao<Member> {
    void loadMembers(Event event);
}
