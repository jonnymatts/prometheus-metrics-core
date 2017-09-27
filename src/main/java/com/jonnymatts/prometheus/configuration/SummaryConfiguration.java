package com.jonnymatts.prometheus.configuration;

import java.util.Arrays;
import java.util.List;

public class SummaryConfiguration {
    private List<QuantileConfiguration> quantiles;
    private Integer ageBuckets;
    private Long maxAgeSeconds;
    private String name;
    private String description;
    private String[] labels;

    public SummaryConfiguration() {}

    public SummaryConfiguration(List<QuantileConfiguration> quantiles, Integer ageBuckets, Long maxAgeSeconds, String name, String description, String... labels) {
        this.quantiles = quantiles;
        this.ageBuckets = ageBuckets;
        this.maxAgeSeconds = maxAgeSeconds;
        this.name = name;
        this.description = description;
        this.labels = labels;
    }

    public List<QuantileConfiguration> getQuantiles() {
        return quantiles;
    }

    public void setQuantiles(List<QuantileConfiguration> quantiles) {
        this.quantiles = quantiles;
    }

    public Integer getAgeBuckets() {
        return ageBuckets;
    }

    public void setAgeBuckets(int ageBuckets) {
        this.ageBuckets = ageBuckets;
    }

    public Long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public void setMaxAgeSeconds(long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
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

        SummaryConfiguration that = (SummaryConfiguration) o;

        if(quantiles != null ? !quantiles.equals(that.quantiles) : that.quantiles != null) return false;
        if(ageBuckets != null ? !ageBuckets.equals(that.ageBuckets) : that.ageBuckets != null) return false;
        if(maxAgeSeconds != null ? !maxAgeSeconds.equals(that.maxAgeSeconds) : that.maxAgeSeconds != null) return false;
        if(name != null ? !name.equals(that.name) : that.name != null) return false;
        if(description != null ? !description.equals(that.description) : that.description != null) return false;
        return Arrays.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        int result = quantiles != null ? quantiles.hashCode() : 0;
        result = 31 * result + (ageBuckets != null ? ageBuckets.hashCode() : 0);
        result = 31 * result + (maxAgeSeconds != null ? maxAgeSeconds.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(labels);
        return result;
    }
}