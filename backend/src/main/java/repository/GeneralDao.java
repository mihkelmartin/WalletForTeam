package repository;

/**
 * Created by mihkel on 13.04.2018.
 */
public interface GeneralDao<ModelClass> {
    void add(ModelClass modelClass);
    void save(ModelClass modelClass);
    void remove(ModelClass modelClass);
}
