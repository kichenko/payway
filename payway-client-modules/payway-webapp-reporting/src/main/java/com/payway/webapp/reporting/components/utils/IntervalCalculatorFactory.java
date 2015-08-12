/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.components.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * IntervalCalculatorFactory
 *
 * @author Sergey Kichenko
 * @created 05.08.2015
 */
public class IntervalCalculatorFactory {

    public enum IntervalCalculatorType {

        Today,
        TodayWhole,
        Yesterday,
        ThisWeek,
        LastWeek,
        ThisMonth,
        LastMonth,
        ThisQuarter,
        LastQuarter,
        ThisYear,
        LastYear,
    }

    public static interface Interval {

        Date getBeginDate();

        Date getEndDate();
    }

    public static interface IntervalCalculator {

        Interval getInterval(Calendar current);

        Interval getInterval();
    }

    private final static class IntervalImpl implements Interval {

        private final Date begin;
        private final Date end;

        public IntervalImpl(Date begin, Date end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public Date getBeginDate() {
            return begin;
        }

        @Override
        public Date getEndDate() {
            return end;
        }
    }

    public static abstract class AbstractIntervalCalculator implements IntervalCalculator {

        protected void setMidnight(Calendar date) {
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
        }

        @Override
        public Interval getInterval() {
            return getInterval(Calendar.getInstance());
        }
    }

    public static class Today extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin = (Calendar) current.clone();
            setMidnight(begin);

            return new IntervalImpl(begin.getTime(), current.getTime());
        }
    }

    public static class TodayWhole extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin, end;

            begin = (Calendar) current.clone();
            setMidnight(begin);

            end = (Calendar) begin.clone();
            end.add(Calendar.DATE, 1);
            setMidnight(end);
            end.add(Calendar.SECOND, -1);

            return new IntervalImpl(begin.getTime(), end.getTime());
        }

    }

    public static class Yesterday extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin, end;

            begin = (Calendar) current.clone();
            setMidnight(begin);
            begin.add(Calendar.DATE, -1);

            end = (Calendar) begin.clone();
            end.add(Calendar.DATE, 1);

            return new IntervalImpl(begin.getTime(), end.getTime());
        }

    }

    public static class ThisWeek extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin = (Calendar) current.clone();

            setMidnight(begin);
            begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            if (begin.after(current)) {
                begin.add(Calendar.DATE, -7);
            }

            return new IntervalImpl(begin.getTime(), current.getTime());
        }

    }

    public static class LastWeek extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin, end;
            begin = (Calendar) current.clone();

            setMidnight(begin);
            begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            if (begin.after(current)) {
                begin.add(Calendar.DATE, -7);
            }

            begin.add(Calendar.DATE, -7);

            end = (Calendar) begin.clone();
            end.add(Calendar.DATE, 7);

            return new IntervalImpl(begin.getTime(), end.getTime());
        }
    }

    public static class ThisMonth extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin = (Calendar) current.clone();
            setMidnight(begin);
            begin.set(Calendar.DAY_OF_MONTH, 1);

            return new IntervalImpl(begin.getTime(), current.getTime());
        }
    }

    public static class LastMonth extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin, end;
            begin = (Calendar) current.clone();
            setMidnight(begin);

            begin.set(Calendar.DAY_OF_MONTH, 1);
            begin.add(Calendar.MONTH, -1);

            end = (Calendar) begin.clone();
            end.add(Calendar.MONTH, 1);

            return new IntervalImpl(begin.getTime(), end.getTime());
        }

    }

    public static class ThisQuarter extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            int qb;
            Calendar begin = (Calendar) current.clone();

            setMidnight(begin);
            qb = begin.get(Calendar.MONTH) / 3;
            begin.set(Calendar.MONTH, 3 * qb);
            begin.set(Calendar.DAY_OF_MONTH, 1);

            return new IntervalImpl(begin.getTime(), current.getTime());
        }
    }

    public static class LastQuarter extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            int qb;
            Calendar begin, end;

            begin = (Calendar) current.clone();

            setMidnight(begin);
            begin.set(Calendar.DAY_OF_MONTH, 1);
            qb = begin.get(Calendar.MONTH) / 3;
            begin.set(Calendar.MONTH, 3 * qb);
            begin.add(Calendar.MONTH, -3);

            end = (Calendar) begin.clone();
            end.add(Calendar.MONTH, 3);

            return new IntervalImpl(begin.getTime(), end.getTime());
        }
    }

    public static class ThisYear extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin = (Calendar) current.clone();

            setMidnight(begin);
            begin.set(Calendar.DAY_OF_YEAR, 1);

            return new IntervalImpl(begin.getTime(), current.getTime());
        }
    }

    public static class LastYear extends AbstractIntervalCalculator {

        @Override
        public Interval getInterval(Calendar current) {

            Calendar begin, end;

            begin = (Calendar) current.clone();
            setMidnight(begin);
            begin.set(Calendar.DAY_OF_YEAR, 1);
            begin.add(Calendar.YEAR, -1);

            end = (Calendar) begin.clone();
            end.add(Calendar.YEAR, 1);

            return new IntervalImpl(begin.getTime(), end.getTime());
        }
    }

    private static final Map<IntervalCalculatorType, IntervalCalculator> calculators = new HashMap<>();

    static {
        calculators.put(IntervalCalculatorType.Today, new Today());
        calculators.put(IntervalCalculatorType.TodayWhole, new TodayWhole());
        calculators.put(IntervalCalculatorType.Yesterday, new Yesterday());
        calculators.put(IntervalCalculatorType.ThisWeek, new ThisWeek());
        calculators.put(IntervalCalculatorType.LastWeek, new LastWeek());
        calculators.put(IntervalCalculatorType.ThisMonth, new ThisMonth());
        calculators.put(IntervalCalculatorType.LastMonth, new LastMonth());
        calculators.put(IntervalCalculatorType.ThisQuarter, new ThisQuarter());
        calculators.put(IntervalCalculatorType.LastQuarter, new LastQuarter());
        calculators.put(IntervalCalculatorType.ThisYear, new ThisYear());
        calculators.put(IntervalCalculatorType.LastYear, new LastYear());
    }

    public static IntervalCalculator getIntervalCalculator(IntervalCalculatorType kind) {
        return calculators.get(kind);
    }
}
