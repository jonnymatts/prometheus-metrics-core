package com.jonnymatts.prometheus.collectors;

import com.jonnymatts.prometheus.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import com.jonnymatts.prometheus.configuration.LinearBucketConfiguration;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;

import java.util.List;

public class PrometheusHistogram {
    private final Histogram histogram;

    public PrometheusHistogram(String name,
                               String description,
                               String... labels) {
        this(new HistogramConfiguration(null, null, null, name, description, labels));
    }

    public PrometheusHistogram(HistogramConfiguration configuration) {
        this(Histogram.build(), configuration);
    }

    public PrometheusHistogram(Histogram.Builder builder,
                               HistogramConfiguration configuration) {
        builder.name(configuration.getName())
                .help(configuration.getDescription())
                .labelNames(configuration.getLabels());

        final List<Double> configurationBuckets = configuration.getBuckets();
        if(configurationBuckets != null) {
            configureBucketsForBuilder(builder, configurationBuckets);
        }

        final ExponentialBucketConfiguration exponentialBucketConfiguration = configuration.getExponentialBuckets();
        if(exponentialBucketConfiguration != null) {
            builder.exponentialBuckets(
                    exponentialBucketConfiguration.getStart(),
                    exponentialBucketConfiguration.getFactor(),
                    exponentialBucketConfiguration.getCount()
            );
        }

        final LinearBucketConfiguration linearBucketConfiguration = configuration.getLinearBuckets();
        if(linearBucketConfiguration != null) {
            builder.linearBuckets(
                    linearBucketConfiguration.getStart(),
                    linearBucketConfiguration.getWidth(),
                    linearBucketConfiguration.getCount()
            );
        }

        this.histogram = builder
                .create();
    }

    public PrometheusHistogram(Histogram histogram) {
        this.histogram = histogram;
    }

    public Histogram.Child labels(String... labels) {
        return histogram.labels(labels);
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
        return histogram.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return histogram.describe();
    }

    public PrometheusHistogram register() {
        histogram.register();
        return this;
    }

    public PrometheusHistogram register(CollectorRegistry registry) {
        histogram.register(registry);
        return this;
    }

    private void configureBucketsForBuilder(Histogram.Builder builder, List<Double> configurationBuckets) {
        builder.buckets(
                configurationBuckets.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrometheusHistogram that = (PrometheusHistogram) o;

        return histogram != null ? histogram.equals(that.histogram) : that.histogram == null;
    }

    @Override
    public int hashCode() {
        return histogram != null ? histogram.hashCode() : 0;
    }
}