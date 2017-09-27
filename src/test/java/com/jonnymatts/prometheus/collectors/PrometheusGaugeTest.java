package com.jonnymatts.prometheus.collectors;

import io.prometheus.client.Gauge;
import io.prometheus.client.Gauge.Child;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrometheusGaugeTest {

    private static final String BEAN_NAME = "bean_name";
    private static final String ATTRIBUTE_NAME = "attribute_name";

    @Mock private Gauge gauge;
    @Mock private Child gaugeChild;

    private PrometheusGauge prometheusGauge;

    @Before
    public void setUp() throws Exception {
        prometheusGauge = new PrometheusGauge(gauge);

        when(prometheusGauge.labels(BEAN_NAME, ATTRIBUTE_NAME)).thenReturn(gaugeChild);
    }

    @Test
    public void registerCallsRegister() throws Exception {
        final PrometheusGauge got = prometheusGauge.register();

        assertThat(got).isEqualTo(prometheusGauge);

        verify(gauge).register();
    }

    @Test
    public void incCallsIncWithTheCorrectLabels() throws Exception {
        prometheusGauge.inc(BEAN_NAME, ATTRIBUTE_NAME);

        verify(gaugeChild).inc(1.0);
    }

    @Test
    public void decCallsDecWithTheCorrectLabels() throws Exception {
        prometheusGauge.dec(BEAN_NAME, ATTRIBUTE_NAME);

        verify(gaugeChild).dec(1.0);
    }

    @Test
    public void setCallsSetWithTheCorrectLabels() throws Exception {
        prometheusGauge.set(BEAN_NAME, ATTRIBUTE_NAME, 100);

        verify(gaugeChild).set(100);
    }
}