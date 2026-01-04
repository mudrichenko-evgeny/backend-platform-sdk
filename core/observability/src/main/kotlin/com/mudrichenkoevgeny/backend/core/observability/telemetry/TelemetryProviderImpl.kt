package com.mudrichenkoevgeny.backend.core.observability.telemetry

import com.mudrichenkoevgeny.backend.core.common.error.model.ErrorId
import com.mudrichenkoevgeny.backend.core.common.logs.AppLogger
import com.mudrichenkoevgeny.backend.core.observability.config.model.ObservabilityConfig
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.semconv.ServiceAttributes
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelemetryProviderImpl @Inject constructor(
    observabilityConfig: ObservabilityConfig,
    private val appLogger: AppLogger
) : TelemetryProvider {

//    private val resource: Resource = Resource.getDefault()
//        .merge(Resource.create(Attributes.of(ServiceAttributes.SERVICE_NAME, observabilityConfig.telemetryServiceName)))
//    private val spanExporter: OtlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder()
//        .setEndpoint(observabilityConfig.telemetryEndpoint)
//        .build()
//    private val tracerProvider: SdkTracerProvider = SdkTracerProvider.builder()
//        .setResource(resource)
//        .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
//        .build()
//    private val metricExporter: OtlpGrpcMetricExporter = OtlpGrpcMetricExporter.builder()
//        .setEndpoint(observabilityConfig.telemetryEndpoint)
//        .build()
//    private val meterProvider: SdkMeterProvider = SdkMeterProvider.builder()
//        .setResource(resource)
//        .registerMetricReader(
//            PeriodicMetricReader.builder(metricExporter)
//                .setInterval(Duration.ofSeconds(observabilityConfig.metricIntervalSeconds))
//                .build()
//        )
//        .build()
//
//    private val openTelemetrySdk: OpenTelemetrySdk = OpenTelemetrySdk.builder()
//        .setTracerProvider(tracerProvider)
//        .setMeterProvider(meterProvider)
//        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
//        .buildAndRegisterGlobal()
//    override val openTelemetry: OpenTelemetry = openTelemetrySdk
//    override val tracer: Tracer = openTelemetry.getTracer(observabilityConfig.telemetryServiceName)
//    override val meter: Meter = meterProvider.get(observabilityConfig.telemetryServiceName)
//    override val prometheusMeterRegistry: PrometheusMeterRegistry = PrometheusMeterRegistry(
//        PrometheusConfig.DEFAULT
//    )

    override val openTelemetry: OpenTelemetry = GlobalOpenTelemetry.get()
    override val tracer: Tracer = openTelemetry.getTracer(observabilityConfig.telemetryServiceName)
    override val meter: Meter = openTelemetry.getMeter(observabilityConfig.telemetryServiceName)
    override val prometheusMeterRegistry: PrometheusMeterRegistry = PrometheusMeterRegistry(
        PrometheusConfig.DEFAULT
    )

    init {
        JvmMemoryMetrics().bindTo(prometheusMeterRegistry)
        JvmGcMetrics().bindTo(prometheusMeterRegistry)
        JvmThreadMetrics().bindTo(prometheusMeterRegistry)
        ProcessorMetrics().bindTo(prometheusMeterRegistry)
        ClassLoaderMetrics().bindTo(prometheusMeterRegistry)
    }

    override fun warmup() {
        tracer.spanBuilder(SPAN_WARMUP).startSpan().end()
    }


    companion object {
        const val SPAN_WARMUP = "warmup"
    }
}