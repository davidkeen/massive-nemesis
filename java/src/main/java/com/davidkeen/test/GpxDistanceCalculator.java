package com.davidkeen.test;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class GpxDistanceCalculator {

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
     * An mutable result container to keep track of the total distance and the previous point in the track.
     */
    private static class Result {
        private Point previousPoint;
        private double totalDistance = 0.0;
    }

    /**
     * A custom Collector which calculates the distance of a GPS track using the haversine formula.
     */
    public static class GpxCollector implements Collector<Point, Result, Double> {

        public static double haversineDistance(Point pointA, Point pointB) {
            double deltaLat = Math.toRadians(pointB.lat - pointA.lat);
            double deltaLong = Math.toRadians(pointB.lon - pointA.lon);
            double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(Math.toRadians(pointA.lat)) *
                    Math.cos(Math.toRadians(pointB.lat)) * Math.pow(Math.sin(deltaLong / 2), 2);
            double greatCircleDistance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return 3958.761 * greatCircleDistance;
        }

        @Override
        public Supplier<Result> supplier() {
            return Result::new;
        }

        @Override
        public BiConsumer<Result, Point> accumulator() {
            return (accumulator, entry) -> {
                if (accumulator.previousPoint != null) {
                    accumulator.totalDistance += haversineDistance(accumulator.previousPoint, entry);
                }
                accumulator.previousPoint = entry;
            };
        }

        @Override
        public BinaryOperator<Result> combiner() {

            // Should not be processed in a parallel stream
            return null;
        }

        @Override
        public Function<Result, Double> finisher() {
            return accumulator -> accumulator.totalDistance;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

    public static void main(String[] args) {
        List<Point> track = new ImmutableList.Builder<Point>()
                .add(new Point(51.168437004089355, -0.648922920227051))
                .add(new Point(51.16805076599121, -0.64918041229248))
                .add(new Point(51.16757869720459, -0.64995288848877))
                .build();
        double distance = track.stream().collect(new GpxCollector());
        System.out.println("Total distance: " + distance + " miles");
    }
}
