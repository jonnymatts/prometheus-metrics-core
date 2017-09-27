package com.jonnymatts.prometheus.collectors;

import io.prometheus.client.Counter;
import io.prometheus.client.Counter.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrometheusCounterTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock
    private Counter counter;
    @Mock
    private Child counterChild;

    private PrometheusCounter prometheusCounter;

    @Before
    public void setUp() throws Exception {
        prometheusCounter = new PrometheusCounter(counter);

        when(prometheusCounter.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(counterChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final PrometheusCounter got = prometheusCounter.register();

        assertThat(got).isEqualTo(prometheusCounter);

        verify(counter).register();
    }

    @Test
    public void incCallsIncWithTheCorrectLabels() throws Exception {
        prometheusCounter.inc(BEAN_NAME, ATTRIBUTE_NAME);

        verify(counterChild).inc(1.0);
    }

    @Test
    public void getCallsGetWithTheCorrectLabels() throws Exception {
        when(counterChild.get()).thenReturn(100d);

        final double got = prometheusCounter.get(BEAN_NAME, ATTRIBUTE_NAME);

        assertThat(got).isEqualTo(100d);
    }
}