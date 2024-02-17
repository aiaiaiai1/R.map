package rmap.service;

import java.util.ArrayList;
import java.util.List;
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

}
