package rmap.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import rmap.entity.Edge;
import rmap.entity.Notion;

public class NotionSearcher {

    private NotionSearcher() {
    }

    public static List<Notion> searchDepthFirst(Notion notion) {
        Stack<Notion> stack = new Stack<>();
        List<Notion> visitedNotion = new ArrayList<>();

        stack.push(notion);

        while (!stack.isEmpty()) {
            Notion popedNotion = stack.pop();
            if (visitedNotion.contains(popedNotion)) {
                continue;
            }
            visitedNotion.add(popedNotion);

            popedNotion.getEdges().stream()
                    .map(Edge::getTargetNotion)
                    .filter(n -> !visitedNotion.contains(n))
                    .forEach(n -> {
                        stack.push(n);
                    });
        }

        return visitedNotion;
    }

    public static List<List<Notion>> convertToGraphs(List<Notion> notions) {
        List<List<Notion>> graphs = new ArrayList<>();
        Set<Notion> visitedNotion = new HashSet<>();
        for (Notion notion : notions) {
            if (!visitedNotion.contains(notion)) {
                List<Notion> graph = searchDepthFirst(notion);
                graphs.add(new ArrayList<>(graph));
                visitedNotion.addAll(graph);
            }
        }
        return graphs;
    }

}
