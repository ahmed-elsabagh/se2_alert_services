package com.se2.alert.config;

import com.se2.alert.properties.AlertProperties;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {

    @Bean
    public SolrClient solrClientPCN() {
        return new HttpSolrClient.Builder(AlertProperties.solrServerUrl + "/" + AlertProperties.pcnCore).build();
    }
}
