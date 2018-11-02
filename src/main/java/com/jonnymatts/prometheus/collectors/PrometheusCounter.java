package com.jonnymatts.prometheus.collectors;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Counter.Child;

import java.util.List;

public class PrometheusCounter {
    private final Counter counter;

    public PrometheusCounter(String name, String description, String... labels) {
        this.counter = Counter.build()
                .name(name)
                .help(description)
                .labelNames(labels)
                .create();
    }

    public PrometheusCounter(Counter counter) {
        this.counter = counter;
    }

    public void inc(String beanName, String attributeName) {
        inc(beanName, attributeName, 1);
    }

    public void inc(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).inc(amount);
    }

    public double get(String... labels) {
        return labels(labels).get();
    }

    public Child labels(String... labels) {
        return counter.labels(labels);
    }

    public List<Collector.MetricFamilySamples> collect() {
        return counter.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return counter.describe();
    }

    public PrometheusCounter register() {
        counter.register();
        return this;
    }

    public PrometheusCounter register(CollectorRegistry registry) {
        counter.register(registry);
        return this;
    }
}