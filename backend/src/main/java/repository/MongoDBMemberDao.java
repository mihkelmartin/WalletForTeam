package repository;

import model.Event;
import model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;


/**
 * Created by mihkel on 6.04.2018.
 */

@Repository
public class MongoDBMemberDao implements MemberDao {

    @Autowired
    @Qualifier("mongoOperations")
    private MongoOperations mongoOps;

    @Override
    public void add(Member member) {
        save(member);
    }

    @Override
    public void save(Member member) {
        mongoOps.save(member.getEvent());
    }

    @Override
    public void remove(Member member) {
        save(member);
    }

    @Override
    public void loadMembers(Event event) {

    }
}
