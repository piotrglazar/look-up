package com.piotrglazar.lookup.configuration;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.lang.invoke.MethodHandles;

@Configuration
@ComponentScan("com.piotrglazar.lookup")
public class ApplicationConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public StandardAnalyzer standardAnalyzer() {
        return new StandardAnalyzer();
    }

    @Bean
    public PropertiesFactoryBean configProperties() {
        LOG.info("Loading config properties from file: application.properties");

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("application.properties"));

        return propertiesFactoryBean;
    }

    @Bean
    public IndexWriterConfig indexWriterConfig() {
        return new IndexWriterConfig(standardAnalyzer());
    }
}
