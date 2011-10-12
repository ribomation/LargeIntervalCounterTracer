package com.ribomation.large_interval_counter;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

import com.wily.introscope.agent.IAgent;
import com.wily.introscope.agent.stat.IIntegerFluctuatingCounterDataAccumulator;
import com.wily.introscope.agent.trace.ASingleInstanceTracerFactory;
import com.wily.introscope.agent.trace.InvocationData;
import com.wily.introscope.agent.trace.ProbeIdentification;
import com.wily.introscope.agent.trace.ReentrancyLevel;
import com.wily.introscope.spec.server.KIntroscopeConstants;
import com.wily.util.feedback.IModuleFeedbackChannel;
import com.wily.util.feedback.Module;
import com.wily.util.heartbeat.ITimestampedRunnable;
import com.wily.util.heartbeat.IntervalHeartbeat;
import com.wily.util.properties.AttributeListing;
import com.wily.util.StringUtils;

/**
 * A tracer-type similar to PerIntervalCounter, but for large intervals such as minutes and hours.
 *
 */
public class LargeIntervalCounterTracer extends ASingleInstanceTracerFactory implements ITimestampedRunnable {
    private static final Module                         module           = new Module("LargeIntervalCounterTracer");

    private static final String                         INTERVAL_KEY     = "interval";
    private static final String                         PATTERN_KEY      = "metricPattern";
    private static final String                         PULS_KEY         = "pulsInterval";

    private static final String                         INTERVAL_DEFAULT = "1m";
    private static final String                         PATTERN_DEFAULT  = "Calls / {0}";
    private static final int                            PULS_DEFAULT     = KIntroscopeConstants.kAgentDefaultHarvestingPeriodInMillis;;

    private AtomicInteger                               currentCount     = new AtomicInteger();
    private SlottedCounter                              counters;
    private IIntegerFluctuatingCounterDataAccumulator   metric;
    private IModuleFeedbackChannel                      log;
    private ReentrancyLevel                             reentrancy;

    public LargeIntervalCounterTracer(IAgent agent, AttributeListing params, ProbeIdentification id, Object sample) {
        super(agent, params, id, sample);
        log = agent.IAgent_getModuleFeedback();

        String          resourceName = extractMetricResource( getFormattedName() );  // DekaNet|Services|{classname}
        String          pattern      = getTracerParameterValue(PATTERN_KEY, PATTERN_DEFAULT);
        IntegerUnit     interval     = IntegerUnit.create( getTracerParameterValue(INTERVAL_KEY, INTERVAL_DEFAULT) );
        int             pulsInterval = getTracerParameterValueAsInt(PULS_KEY, PULS_DEFAULT);
        String          metricName   = MessageFormat.format(pattern, new Object[] {interval.toString()});
        metricName                   = resourceName + ":" + metricName;

        reentrancy = calculateReentrancyLevel(ReentrancyLevel.kNone);
        metric     = getDataAccumulatorFactory().safeGetIntegerFluctuatingCounterDataAccumulator(metricName);
        counters   = createSlottedCounter(interval.getValue(), pulsInterval);
        agent.IAgent_getCommonHeartbeat().addBehavior(
                this,
                module.getName(),
                IntervalHeartbeat.kActive,
                pulsInterval,
                IntervalHeartbeat.kRunFirst
        );

        log.info(module, "Initialized: metric="+metricName);
    }

    protected String    getTracerParameterValue(String name, String defaultValue) {
        String v = getParameter(name);
        if (StringUtils.isEmpty(v)) return defaultValue;
        return v.trim();
    }

    protected int    getTracerParameterValueAsInt(String name, int defaultValue) {
        String v = getParameter(name);
        if (StringUtils.isEmpty(v)) return defaultValue;
        return Integer.parseInt( v.trim() );
    }

    public void ITracer_startTrace(int i, InvocationData data) {
        //nothing
    }

    public void ITracer_finishTrace(int i, InvocationData data) {
        currentCount.incrementAndGet();
    }

    public ReentrancyLevel ITracerFactory_getReentrancyLevel() {
        return reentrancy;
    }

    public boolean ITracerFactory_isShutoff() {
        return metric.IDataAccumulator_isShutOff();
    }

    protected String    extractMetricResource(String metric) {
        int  colonPos = metric.lastIndexOf(':');
        if (colonPos > 0) return metric.substring(0, colonPos);
        return metric;
    }

    protected SlottedCounter    createSlottedCounter(int interval, int puls) {
        return new SlottedCounter(interval / puls);
    }

    public void ITimestampedRunnable_execute(long l) {
        if (this.ITracerFactory_isShutoff()) return;

        metric.IIntegerCounterDataAccumulator_setValue( (int) metricValue()  );
    }

    protected synchronized long     metricValue() {
        counters.push( currentCount.getAndSet(0) );
        return counters.value();
    }

}
