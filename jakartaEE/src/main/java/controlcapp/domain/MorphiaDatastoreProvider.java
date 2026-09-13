package controlcapp.domain;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import com.mongodb.client.MongoClients;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MorphiaDatastoreProvider {
    private final Datastore datastore;

    @Inject
    public MorphiaDatastoreProvider(
            @ConfigProperty(name = "mongodb.host") String mongoUri,
            @ConfigProperty(name = "mongodb.database") String dbName
    ) {
        // Crea il client MongoDB
        var mongoClient = MongoClients.create(mongoUri);
        // Crea il datastore Morphia
        this.datastore = Morphia.createDatastore(mongoClient, dbName);

    }

    @Produces
    @ApplicationScoped
    public Datastore produceDatastore() {
        return datastore;
    }
}


