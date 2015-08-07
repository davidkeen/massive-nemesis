package com.davidkeen.test;

import com.google.common.collect.ImmutableList;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class GpxDistance {

    public static void main(String[] args) {
        List<Point> track = new ImmutableList.Builder<Point>()
                .add(new Point(51.168437004089355, -0.648922920227051))
                .add(new Point(51.16805076599121, -0.64918041229248))
                .add(new Point(51.16757869720459, -0.64995288848877))
                .build();

        double distance = track.stream().collect(new GpxCollector());
        System.out.println(distance);
    }

    /**
     * An immutable class representing a geographic point.
     */
    public static class Point {
        public final double lat;
        public final double lon;

        public Point(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    /**
     * A custom Collector which calculates the distance of a GPS track using the haversine formula.
     */
    public static class GpxCollector implements Collector<Point, Calculator, Double> {

        @Override
        public Supplier<Calculator> supplier() {
            return Calculator::new;
        }

        @Override
        public BiConsumer<Calculator, Point> accumulator() {
            return (accumulator, entry) -> {
                if (accumulator.point != null) {
                    accumulator.total += Calculator.haversineDistance(accumulator.point, entry);
                }
                accumulator.point = entry;
            };
        }

        /**
         * Called when the stream is parallel.
         *
         * @return
         *
         * TODO: How can we omit this? It doesn't make sense for this to be used non-sequentially.
         */
        @Override
        public BinaryOperator<Calculator> combiner() {
            return null;
        }

        @Override
        public Function<Calculator, Double> finisher() {
            return accumulator -> accumulator.total;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

    /**
     * An accumulator class to keep track of the total distance
     * and the previous point in the track.
     *
     * TODO: Should this class be private?
     */
    public static class Calculator {
        private Point point;
        private double total = 0.0;

        // TODO: Should this be in the Collector class?
        public static double haversineDistance(Point pointA, Point pointB) {
            double deltaLat = Math.toRadians(pointB.lat - pointA.lat);
            double deltaLong = Math.toRadians(pointB.lon - pointA.lon);
            double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(Math.toRadians(pointA.lat)) *
                    Math.cos(Math.toRadians(pointB.lat)) * Math.pow(Math.sin(deltaLong / 2), 2);
            double greatCircleDistance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return 3958.761 * greatCircleDistance;
        }

    }
}
