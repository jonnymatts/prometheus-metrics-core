package com.jonnymatts.prometheus.configuration;

import java.util.Arrays;
import java.util.List;

public class HistogramConfiguration {
    private List<Double> buckets;
    private ExponentialBucketConfiguration exponentialBuckets;
    private LinearBucketConfiguration linearBuckets;
    private String name;
    private String description;
    private String[] labels;

    public HistogramConfiguration() {}

    public HistogramConfiguration(List<Double> buckets,
                                  ExponentialBucketConfiguration exponentialBuckets,
                                  LinearBucketConfiguration linearBuckets,
                                  String name,
                                  String description,
                                  String... labels) {
        this.buckets = buckets;
        this.exponentialBuckets = exponentialBuckets;
        this.linearBuckets = linearBuckets;
        this.name = name;
        this.description = description;
        this.labels = labels;
    }

    public List<Double> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Double> buckets) {
        this.buckets = buckets;
    }

    public ExponentialBucketConfiguration getExponentialBuckets() {
        return exponentialBuckets;
    }

    public void setExponentialBuckets(ExponentialBucketConfiguration exponentialBuckets) {
        this.exponentialBuckets = exponentialBuckets;
    }

    public LinearBucketConfiguration getLinearBuckets() {
        return linearBuckets;
    }

    public void setLinearBuckets(LinearBucketConfiguration linearBuckets) {
        this.linearBuckets = linearBuckets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        HistogramConfiguration that = (HistogramConfiguration) o;

        if(buckets != null ? !buckets.equals(that.buckets) : that.buckets != null) return false;
        if(exponentialBuckets != null ? !exponentialBuckets.equals(that.exponentialBuckets) : that.exponentialBuckets != null)
            return false;
        if(linearBuckets != null ? !linearBuckets.equals(that.linearBuckets) : that.linearBuckets != null) return false;
        if(name != null ? !name.equals(that.name) : that.name != null) return false;
        if(description != null ? !description.equals(that.description) : that.description != null) return false;
        return Arrays.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        int result = buckets != null ? buckets.hashCode() : 0;
        result = 31 * result + (exponentialBuckets != null ? exponentialBuckets.hashCode() : 0);
        result = 31 * result + (linearBuckets != null ? linearBuckets.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(labels);
        return result;
    }
}
