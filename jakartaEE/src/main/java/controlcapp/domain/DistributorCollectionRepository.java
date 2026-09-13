package controlcapp.domain;

import dev.morphia.Datastore;
import dev.morphia.query.filters.Filters;
import com.mongodb.client.result.DeleteResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DistributorCollectionRepository {
    private Datastore datastore;
    protected DistributorCollectionRepository(){}
    @Inject
    public DistributorCollectionRepository(Datastore datastore) {
        this.datastore = datastore;
    }


    public void save(DistributorDocument distributor) {
        datastore.save(distributor);
    }

    public void saveAll(List<DistributorDocument> distributors) {
        datastore.save(distributors);
    }

    public DistributorDocument findById(Long id) {
        return datastore.find(DistributorDocument.class)
                .filter(Filters.eq("_id", id))
                .first();
    }

    public List<DistributorDocument> findByIds(List<Long> ids) {
        return datastore.find(DistributorDocument.class)
                .filter(Filters.in("_id", ids))
                .stream()
                .toList();
    }

    public List<DistributorDocument> findAll() {
        return datastore.find(DistributorDocument.class)
                .stream()
                .toList();
    }


    public boolean deleteById(Long id) {
        DeleteResult result = datastore.find(DistributorDocument.class)
                .filter(Filters.eq("_id", id))
                .delete();
        return result.getDeletedCount() > 0;
    }

    public long deleteAll(List<DistributorDocument> documents) {
        if (documents == null || documents.isEmpty()) {
            return 0;
        }
        List<Long> ids = documents.stream()
                .map(DistributorDocument::getId)
                .toList();

        DeleteResult result = datastore.find(DistributorDocument.class)
                .filter(Filters.in("_id", ids))
                .delete();
        return result.getDeletedCount();
    }
}
