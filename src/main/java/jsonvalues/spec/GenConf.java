package jsonvalues.spec;

import fun.tuple.Pair;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

record GenConf(Pair<Integer, Integer> arraySize,
               Pair<Integer, Integer> mapSize,
               Pair<Integer, Integer> keyMapLength,
               Pair<Integer, Integer> stringLength,
               Pair<Integer, Integer> intSize,
               Pair<Long, Long> longSize,
               Pair<Double, Double> doubleSize,
               Pair<BigDecimal, BigDecimal> bigDecSize,
               Pair<BigInteger, BigInteger> bigIntSize,
               Pair<Integer, Integer> binaryLength,
               Pair<Instant, Instant> instantSize,
               int optionalProbability,
               int nullableProbability){

}