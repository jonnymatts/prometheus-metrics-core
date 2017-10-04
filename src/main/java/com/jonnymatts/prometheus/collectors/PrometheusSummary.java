package com.jonnymatts.prometheus.collectors;

import com.jonnymatts.prometheus.configuration.QuantileConfiguration;
import com.jonnymatts.prometheus.configuration.SummaryConfiguration;
import io.prometheus.client.Collector;
import io.prometheus.client.Summary;
import io.prometheus.client.Summary.Builder;

import java.util.List;

public class PrometheusSummary {
    private final Summary summary;

    public PrometheusSummary(String name,
                             String description,
                             String... labels) {
        this(new SummaryConfiguration(null, null, null, name, description, labels));
    }

    public PrometheusSummary(SummaryConfiguration configuration) {
        this(Summary.build(), configuration);
    }

    public PrometheusSummary(Builder builder,
                             SummaryConfiguration configuration) {
        builder.name(configuration.getName())
                .help(configuration.getDescription())
                .labelNames(configuration.getLabels());

        final List<QuantileConfiguration> quantiles = configuration.getQuantiles();
        if(quantiles != null) {
            quantiles.forEach(q -> builder.quantile(q.getQuantile(), q.getError()));
        }

        final Integer ageBuckets = configuration.getAgeBuckets();
        if(ageBuckets != null) {
            builder.ageBuckets(ageBuckets);
        }

        final Long maxAgesSeconds = configuration.getMaxAgeSeconds();
        if(maxAgesSeconds != null) {
            builder.maxAgeSeconds(maxAgesSeconds);
        }

        this.summary = builder.create();
    }

    public PrometheusSummary(Summary summary) {
        this.summary = summary;
    }

    public Summary.Child labels(String... labels) {
        return summary.labels(labels);
    }

    public void startTimer(String beanName, String attributeName) {
        labels(beanName, attributeName).startTimer();
    }

    public void time(String beanName, String attributeName, Runnable runnable) {
        labels(beanName, attributeName).time(runnable);
    }

    public void observe(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).observe(amount);
    }

    public List<Collector.MetricFamilySamples> collect() {
        return summary.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return summary.describe();
    }

    public PrometheusSummary register() {
        summary.register();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrometheusSummary that = (PrometheusSummary) o;

        return summary != null ? summary.equals(that.summary) : that.summary == null;
    }

    @Override
    public int hashCode() {
        return summary != null ? summary.hashCode() : 0;
    }
}