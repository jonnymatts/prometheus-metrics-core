package com.jonnymatts.prometheus.collectors;

import com.jonnymatts.prometheus.configuration.ExponentialBucketConfiguration;
import com.jonnymatts.prometheus.configuration.HistogramConfiguration;
import com.jonnymatts.prometheus.configuration.LinearBucketConfiguration;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;
import io.prometheus.client.Histogram.Builder;
import io.prometheus.client.Histogram.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrometheusHistogramTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private CollectorRegistry collectorRegistry;
    @Mock private Histogram histogram;
    @Mock private Child histogramChild;
    @Mock private Builder histogramBuilder;
    @Mock private Runnable runnable;

    private PrometheusHistogram prometheusHistogram;

    @Before
    public void setUp() throws Exception {
        prometheusHistogram = new PrometheusHistogram(histogram);

        when(prometheusHistogram.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(histogramChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final PrometheusHistogram got = prometheusHistogram.register();

        assertThat(got).isEqualTo(prometheusHistogram);

        verify(histogram).register();
    }

    @Test
    public void registerCallsRegisterWithSuppliedRegister() throws Exception {
        final PrometheusHistogram got = prometheusHistogram.register(collectorRegistry);

        assertThat(got).isEqualTo(prometheusHistogram);

        verify(histogram).register(collectorRegistry);
    }

    @Test
    public void observeCallsObserveWithTheCorrectLabels() throws Exception {
        prometheusHistogram.observe(BEAN_NAME, ATTRIBUTE_NAME, 100d);

        verify(histogramChild).observe(100d);
    }

    @Test
    public void startTimerCallsGetWithTheCorrectLabels() throws Exception {
        prometheusHistogram.startTimer(BEAN_NAME, ATTRIBUTE_NAME);

        verify(histogramChild).startTimer();
    }

    @Test
    public void timeCallsGetWithTheCorrectLabels() throws Exception {
        prometheusHistogram.time(BEAN_NAME, ATTRIBUTE_NAME, runnable);

        verify(histogramChild).time(runnable);
    }

    @Test
    public void constructorAppliesConfigurationCorrectly() throws Exception {
        when(histogramBuilder.name("my_histogram")).thenReturn(histogramBuilder);
        when(histogramBuilder.help("description")).thenReturn(histogramBuilder);
        when(histogramBuilder.labelNames("label1", "label2")).thenReturn(histogramBuilder);
        when(histogramBuilder.buckets(0.001d, 0.01d, 0.1d, 1d)).thenReturn(histogramBuilder);
        when(histogramBuilder.exponentialBuckets(0.001d, 10d, 5)).thenReturn(histogramBuilder);
        when(histogramBuilder.linearBuckets(0.01d, 30d, 10)).thenReturn(histogramBuilder);
        when(histogramBuilder.create()).thenReturn(histogram);

        final PrometheusHistogram got = new PrometheusHistogram(
                histogramBuilder,
                new HistogramConfiguration(
                        asList(0.001d, 0.01d, 0.1d, 1d),
                        new ExponentialBucketConfiguration(0.001d, 10d, 5),
                        new LinearBucketConfiguration(0.01d, 30d, 10),
                        "my_histogram",
                        "description",
                        "label1", "label2"
                )
        );

        assertThat(got).isEqualTo(new PrometheusHistogram(histogram));

        verify(histogramBuilder).buckets(0.001d, 0.01d, 0.1d, 1d);
        verify(histogramBuilder).exponentialBuckets(0.001d, 10d, 5);
        verify(histogramBuilder).linearBuckets(0.01d, 30d, 10);
    }
}