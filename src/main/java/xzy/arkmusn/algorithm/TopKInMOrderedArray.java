package xzy.arkmusn.algorithm;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * find max K numbers in M ordered array. size of each array is N
 *
 * input: M ordered array, each size is N.<br/>
 * output: the max K numbers in all M arrays.<br/>
 * T(n) = O(logN)<br/>
 * S(n) = O(N)
 *
 * @author Eric.Chan
 * @since 1.0
 */
public class TopKInMOrderedArray {
    /**
     * init size of each list
     */
    private static final int N = 100;
    /**
     * init count of lists
     */
    private static final int M = 50;
    /**
     * how much result we need
     */
    private static final int K = 10;

    public static void main(String[] args) {
        final var input = generate();

        final var heap = new PriorityQueue<Node>(M, Comparator.reverseOrder());
        final var result = new ArrayList<Node>(K);

        // put every list max value to heap, and record index of list
        for (int i = 0; i < M; i++) {
            final var list = input.get(i);
            heap.add(new Node(i, pollLast(list)));
        }

        for (int i = 0; i < K; i++) {
            final var node = Optional.ofNullable(heap.poll()).orElseThrow();
            final var from = node.getFrom();
            final var list = input.get(from);

            result.add(node);
            heap.add(new Node(from, pollLast(list)));
        }

        System.out.println("result:");
        System.out.println(result.stream().map(Node::toString).collect(Collectors.joining("\n")));
    }

    private static List<List<Integer>> generate() {
        final var totalList = IntStream.range(1, 1 + N * M).boxed().collect(Collectors.toList());
        Collections.shuffle(totalList);

        final var resultList = new ArrayList<List<Integer>>(M);
        for (int i = 0; i < M; i++) {
            final var list = new ArrayList<Integer>();
            list.addAll(0, totalList.subList(i * N, (i + 1) * N));
            Collections.sort(list);
            resultList.add(list);
            System.out.println(String.format("index: %d, list: [%s]", i, list.stream().map(String::valueOf).collect(Collectors.joining(","))));
        }
        return resultList;
    }

    private static int pollLast(List<Integer> list) {
        final var integer = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return integer;
    }

    @Data
    @Accessors(chain = true)
    private static class Node implements Comparable<Node> {
        /**
         * index of lists
         */
        private final int from;
        /**
         * actual value
         */
        private final int value;

        @Override
        public int compareTo(TopKInMOrderedArray.Node o) {
            return value - o.value;
        }
    }
}
