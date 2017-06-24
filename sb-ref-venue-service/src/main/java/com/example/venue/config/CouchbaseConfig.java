package com.example.venue.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.query.Consistency;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Aniruth Parthasarathy
 */
@Configuration
@EnableCouchbaseRepositories(basePackages = "com.example.venue.db.repository")
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${spring.couchbase.bootstrap-hosts}")
    public String BOOTSTRAP_HOSTS;
    @Value("${spring.couchbase.bucket.name}")
    public String BUCKET_NAME;
    @Value("${spring.couchbase.bucket.password}")
    public String BUCKET_PASSWORD;


    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList(BOOTSTRAP_HOSTS);
    }

    @Override
    protected String getBucketName() {
        return BUCKET_NAME;
    }

    @Override
    protected String getBucketPassword() {
        return BUCKET_PASSWORD;
    }

    @Override
    protected Consistency getDefaultConsistency() {
        return Consistency.READ_YOUR_OWN_WRITES;
    }

    @Override
    public String typeKey() {
        return "docType";
    }
}
