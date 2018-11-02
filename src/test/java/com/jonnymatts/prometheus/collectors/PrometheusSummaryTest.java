package com.jonnymatts.prometheus.collectors;

import com.jonnymatts.prometheus.configuration.QuantileConfiguration;
import com.jonnymatts.prometheus.configuration.SummaryConfiguration;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Summary;
import io.prometheus.client.Summary.Builder;
import io.prometheus.client.Summary.Child;
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
public class PrometheusSummaryTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private CollectorRegistry collectorRegistry;
    @Mock private Summary summary;
    @Mock private Builder summaryBuilder;
    @Mock private Child summaryChild;
    @Mock private Runnable runnable;

    private PrometheusSummary prometheusSummary;

    @Before
    public void setUp() throws Exception {
        prometheusSummary = new PrometheusSummary(summary);

        when(prometheusSummary.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(summaryChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final PrometheusSummary got = prometheusSummary.register();

        assertThat(got).isEqualTo(prometheusSummary);

        verify(summary).register();
    }

    @Test
    public void registerCallsRegisterWithSuppliedRegister() throws Exception {
        final PrometheusSummary got = prometheusSummary.register(collectorRegistry);

        assertThat(got).isEqualTo(prometheusSummary);

        verify(summary).register(collectorRegistry);
    }

    @Test
    public void observeCallsObserveWithTheCorrectLabels() throws Exception {
        prometheusSummary.observe(BEAN_NAME, ATTRIBUTE_NAME, 100d);

        verify(summaryChild).observe(100d);
    }

    @Test
    public void startTimerCallsGetWithTheCorrectLabels() throws Exception {
        prometheusSummary.startTimer(BEAN_NAME, ATTRIBUTE_NAME);

        verify(summaryChild).startTimer();
    }

    @Test
    public void timeCallsGetWithTheCorrectLabels() throws Exception {
        prometheusSummary.time(BEAN_NAME, ATTRIBUTE_NAME, runnable);

        verify(summaryChild).time(runnable);
    }

    @Test
    public void constructorAppliesConfigurationCorrectly() throws Exception {
        when(summaryBuilder.name("my_summary")).thenReturn(summaryBuilder);
        when(summaryBuilder.help("description")).thenReturn(summaryBuilder);
        when(summaryBuilder.labelNames("label1", "label2")).thenReturn(summaryBuilder);
        when(summaryBuilder.quantile(0.001d, 1)).thenReturn(summaryBuilder);
        when(summaryBuilder.quantile(0.01d, 2)).thenReturn(summaryBuilder);
        when(summaryBuilder.quantile(0.1d, 3)).thenReturn(summaryBuilder);
        when(summaryBuilder.ageBuckets(100)).thenReturn(summaryBuilder);
        when(summaryBuilder.maxAgeSeconds(200)).thenReturn(summaryBuilder);
        when(summaryBuilder.create()).thenReturn(summary);

        final PrometheusSummary got = new PrometheusSummary(
                summaryBuilder,
                new SummaryConfiguration(
                        asList(
                                new QuantileConfiguration(0.001d, 1d),
                                new QuantileConfiguration(0.01d, 2d),
                                new QuantileConfiguration(0.1d, 3d)
                                ),
                        100,
                        200L,
                        "my_summary",
                        "description",
                        "label1", "label2"
                        )
        );

        assertThat(got).isEqualTo(new PrometheusSummary(summary));

        verify(summaryBuilder).quantile(0.001d, 1);
        verify(summaryBuilder).quantile(0.01d, 2);
        verify(summaryBuilder).quantile(0.1d, 3);
        verify(summaryBuilder).ageBuckets(100);
        verify(summaryBuilder).maxAgeSeconds(200);
    }
}