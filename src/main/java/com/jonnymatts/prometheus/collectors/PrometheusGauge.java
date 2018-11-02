package com.jonnymatts.prometheus.collectors;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge.Child;

import java.util.List;

public class PrometheusGauge {
    private final Gauge gauge;

    public PrometheusGauge(String name, String description, String... labels) {
        this.gauge = Gauge.build()
                .name(name)
                .help(description)
                .labelNames(labels)
                .create();
    }

    public PrometheusGauge(Gauge gauge) {
        this.gauge = gauge;
    }

    public void inc(String beanName, String attributeName) {
        inc(beanName, attributeName, 1);
    }

    public void inc(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).inc(amount);
    }

    public void dec(String beanName, String attributeName) {
        dec(beanName, attributeName, 1);
    }

    public void dec(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).dec(amount);
    }

    public void set(String beanName, String attributeName, double amount) {
        labels(beanName, attributeName).set(amount);
    }

    public Child labels(String... labels) {
        return gauge.labels(labels);
    }

    public List<Collector.MetricFamilySamples> collect() {
        return gauge.collect();
    }

    public List<Collector.MetricFamilySamples> describe() {
        return gauge.describe();
    }

    public PrometheusGauge register() {
        gauge.register();
        return this;
    }

    public PrometheusGauge register(CollectorRegistry registry) {
        gauge.register(registry);
        return this;
    }
}